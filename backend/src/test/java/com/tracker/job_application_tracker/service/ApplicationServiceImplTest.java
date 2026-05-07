package com.tracker.job_application_tracker.service;

import com.tracker.job_application_tracker.dto.ApplicationDTO;
import com.tracker.job_application_tracker.dto.CreateApplicationRequest;
import com.tracker.job_application_tracker.dto.UpdateApplicationRequest;
import com.tracker.job_application_tracker.enums.ApplicationStatus;
import com.tracker.job_application_tracker.exception.ResourceNotFoundException;
import com.tracker.job_application_tracker.model.Application;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.repository.ApplicationRepository;
import com.tracker.job_application_tracker.service.impl.ApplicationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/*
 * Tests unitaires pour ApplicationServiceImpl.
 *
 * Principe : on teste uniquement la logique du service.
 * On MOCK tout ce qui est extérieur (repository, autres services)
 * pour que chaque test soit isolé et rapide.
 *
 * Structure de chaque test : AAA
 *   Arrange — préparer les données et configurer les mocks
 *   Act     — appeler la méthode à tester
 *   Assert  — vérifier le résultat
 */
@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    // === MOCKS — objets simulés, ne touchent pas la vraie DB ===

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    private StatusHistoryService statusHistoryService;

    // === CLASSE TESTÉE — reçoit les mocks ci-dessus automatiquement ===

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    // === DONNÉES COMMUNES AUX TESTS ===

    private User user;
    private Application application;

    @BeforeEach
    void setUp() {
        // Un utilisateur fictif réutilisé dans tous les tests
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        // Une candidature fictive réutilisée dans tous les tests
        application = new Application();
        application.setId(1L);
        application.setCompany("Google");
        application.setPosition("Software Engineer");
        application.setApplicationDate(LocalDate.of(2026, 1, 15));
        application.setCurrentStatus(ApplicationStatus.SENT);
    }

    // =========================================================
    // getAllApplications
    // =========================================================

    @Test
    void getAllApplications_shouldReturnMappedDTOs() {
        // Arrange
        when(applicationRepository.findByUserIdOrderByApplicationDateDesc(1L))
                .thenReturn(List.of(application));
        when(documentService.getDocumentsByApplicationId(1L)).thenReturn(List.of());
        when(statusHistoryService.getHistoryByApplicationId(1L)).thenReturn(List.of());

        // Act
        List<ApplicationDTO> result = applicationService.getAllApplications(user);

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCompany()).isEqualTo("Google");
        assertThat(result.get(0).getPosition()).isEqualTo("Software Engineer");
    }

    @Test
    void getAllApplications_shouldReturnEmptyListWhenNoApplications() {
        // Arrange
        when(applicationRepository.findByUserIdOrderByApplicationDateDesc(1L))
                .thenReturn(List.of());

        // Act
        List<ApplicationDTO> result = applicationService.getAllApplications(user);

        // Assert
        assertThat(result).isEmpty();
    }

    // =========================================================
    // getApplicationById
    // =========================================================

    @Test
    void getApplicationById_shouldReturnDTOWhenFound() {
        // Arrange
        when(applicationRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(application));
        when(documentService.getDocumentsByApplicationId(1L)).thenReturn(List.of());
        when(statusHistoryService.getHistoryByApplicationId(1L)).thenReturn(List.of());

        // Act
        ApplicationDTO result = applicationService.getApplicationById(user, 1L);

        // Assert
        assertThat(result.getCompany()).isEqualTo("Google");
        assertThat(result.getCurrentStatus()).isEqualTo(ApplicationStatus.SENT);
    }

    @Test
    void getApplicationById_shouldThrowWhenNotFound() {
        // Arrange — aucune candidature trouvée pour cet id
        when(applicationRepository.findByIdAndUserId(99L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert — on s'attend à une exception
        assertThatThrownBy(() -> applicationService.getApplicationById(user, 99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // =========================================================
    // createApplication
    // =========================================================

    @Test
    void createApplication_shouldSaveAndReturnDTO() {
        // Arrange
        CreateApplicationRequest request = new CreateApplicationRequest(
                "Google", "Software Engineer", LocalDate.of(2026, 1, 15), ApplicationStatus.SENT, null
        );
        when(applicationRepository.save(any(Application.class))).thenReturn(application);
        when(documentService.getDocumentsByApplicationId(1L)).thenReturn(List.of());
        when(statusHistoryService.getHistoryByApplicationId(1L)).thenReturn(List.of());

        // Act
        ApplicationDTO result = applicationService.createApplication(user, request);

        // Assert — le DTO est bien retourné et save() a bien été appelé
        assertThat(result.getCompany()).isEqualTo("Google");
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void createApplication_shouldRecordInitialStatusHistory() {
        // Arrange
        CreateApplicationRequest request = new CreateApplicationRequest(
                "Google", "Software Engineer", LocalDate.of(2026, 1, 15), ApplicationStatus.SENT, null
        );
        when(applicationRepository.save(any(Application.class))).thenReturn(application);
        when(documentService.getDocumentsByApplicationId(any())).thenReturn(List.of());
        when(statusHistoryService.getHistoryByApplicationId(any())).thenReturn(List.of());

        // Act
        applicationService.createApplication(user, request);

        // Assert — l'historique de statut doit être créé à la création
        verify(statusHistoryService, times(1))
                .createStatusHistory(eq(1L), eq(ApplicationStatus.SENT), anyString());
    }

    // =========================================================
    // updateApplication
    // =========================================================

    @Test
    void updateApplication_shouldRecordHistoryWhenStatusChanges() {
        // Arrange — on change le statut de SENT à INTERVIEW
        UpdateApplicationRequest request = new UpdateApplicationRequest();
        request.setCurrentStatus(ApplicationStatus.INTERVIEW);

        when(applicationRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(any())).thenReturn(application);
        when(documentService.getDocumentsByApplicationId(any())).thenReturn(List.of());
        when(statusHistoryService.getHistoryByApplicationId(any())).thenReturn(List.of());

        // Act
        applicationService.updateApplication(user, 1L, request);

        // Assert — un changement de statut doit créer une entrée d'historique
        verify(statusHistoryService, times(1))
                .createStatusHistory(any(), eq(ApplicationStatus.INTERVIEW), anyString());
    }

    @Test
    void updateApplication_shouldNotRecordHistoryWhenStatusUnchanged() {
        // Arrange — le nouveau statut est identique à l'ancien (SENT → SENT)
        UpdateApplicationRequest request = new UpdateApplicationRequest();
        request.setCurrentStatus(ApplicationStatus.SENT);

        when(applicationRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(application));
        when(applicationRepository.save(any())).thenReturn(application);
        when(documentService.getDocumentsByApplicationId(any())).thenReturn(List.of());
        when(statusHistoryService.getHistoryByApplicationId(any())).thenReturn(List.of());

        // Act
        applicationService.updateApplication(user, 1L, request);

        // Assert — pas d'historique si le statut n'a pas changé
        verify(statusHistoryService, never()).createStatusHistory(any(), any(), any());
    }

    @Test
    void updateApplication_shouldThrowWhenNotFound() {
        // Arrange
        when(applicationRepository.findByIdAndUserId(99L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> applicationService.updateApplication(user, 99L, new UpdateApplicationRequest()))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // =========================================================
    // deleteApplication
    // =========================================================

    @Test
    void deleteApplication_shouldDeleteDocumentsThenApplication() {
        // Arrange
        when(applicationRepository.findByIdAndUserId(1L, 1L))
                .thenReturn(Optional.of(application));

        // Act
        applicationService.deleteApplication(user, 1L);

        // Assert — les documents doivent être supprimés AVANT la candidature
        verify(documentService, times(1)).deleteAllDocumentsByApplicationId(1L);
        verify(applicationRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteApplication_shouldThrowAndNotDeleteWhenNotFound() {
        // Arrange
        when(applicationRepository.findByIdAndUserId(99L, 1L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> applicationService.deleteApplication(user, 99L))
                .isInstanceOf(ResourceNotFoundException.class);

        // Rien ne doit être supprimé si la candidature n'existe pas
        verify(applicationRepository, never()).deleteById(any());
        verify(documentService, never()).deleteAllDocumentsByApplicationId(any());
    }
}

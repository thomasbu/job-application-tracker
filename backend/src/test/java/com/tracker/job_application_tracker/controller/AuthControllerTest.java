package com.tracker.job_application_tracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.job_application_tracker.dto.*;
import com.tracker.job_application_tracker.exception.GlobalExceptionHandler;
import com.tracker.job_application_tracker.exception.UserAlreadyExistsException;
import com.tracker.job_application_tracker.model.ConfirmationToken;
import com.tracker.job_application_tracker.model.RefreshToken;
import com.tracker.job_application_tracker.model.User;
import com.tracker.job_application_tracker.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Tests du AuthController en mode standalone.
 *
 * On utilise MockMvc sans charger le contexte Spring complet :
 * plus rapide, aucune dépendance base de données ou Spring Security.
 * On vérifie le comportement HTTP (status code, body JSON)
 * en mockant toutes les dépendances du controller.
 */
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    // === MOCKS des dépendances du controller ===

    @Mock private UserService userService;
    @Mock private JwtService jwtService;
    @Mock private EmailService emailService;
    @Mock private ConfirmationTokenService confirmationTokenService;
    @Mock private RefreshTokenService refreshTokenService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // MockMvc standalone : charge uniquement le controller + le GlobalExceptionHandler
        mockMvc = MockMvcBuilders
                .standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        objectMapper = new ObjectMapper();
    }

    // =========================================================
    // POST /api/auth/register
    // =========================================================

    @Test
    void register_shouldReturn201WithMessageOnSuccess() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("thomas@example.com");
        request.setPassword("Password123!");
        request.setFirstName("Thomas");
        request.setLastName("Bulens");

        User user = new User("thomas@example.com", "hashed", "Thomas", "Bulens");
        ConfirmationToken token = new ConfirmationToken();
        token.setToken("confirm-token-abc");

        when(userService.createUser(any(RegisterRequest.class))).thenReturn(user);
        when(confirmationTokenService.createToken(user)).thenReturn(token);
        doNothing().when(emailService).sendConfirmationEmail(anyString(), anyString());

        // Act & Assert
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").exists());

        // Vérifie que l'email de confirmation a bien été envoyé
        verify(emailService, times(1)).sendConfirmationEmail(anyString(), eq("confirm-token-abc"));
    }

    @Test
    void register_shouldReturn409WhenEmailAlreadyExists() throws Exception {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setEmail("existing@example.com");
        request.setPassword("Password123!");
        request.setFirstName("Thomas");
        request.setLastName("Bulens");

        when(userService.createUser(any()))
                .thenThrow(new UserAlreadyExistsException("Email already in use"));

        // Act & Assert — l'API doit retourner 409 Conflict
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // =========================================================
    // POST /api/auth/login
    // =========================================================

    @Test
    void login_shouldReturn200WithTokensOnSuccess() throws Exception {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("thomas@example.com");
        request.setPassword("Password123!");

        User user = new User("thomas@example.com", "hashed", "Thomas", "Bulens");
        user.setEnabled(true);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token-xyz");

        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("thomas@example.com");

        when(userService.findByEmail("thomas@example.com")).thenReturn(user);
        doNothing().when(userService).verifyPassword(any(), anyString());
        when(jwtService.generateToken(anyString())).thenReturn("access-token-abc");
        when(refreshTokenService.createRefreshToken(user)).thenReturn(refreshToken);
        when(userService.convertToDTO(user)).thenReturn(userDTO);

        // Act & Assert
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-abc"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-xyz"))
                .andExpect(jsonPath("$.user.email").value("thomas@example.com"));
    }

    @Test
    void login_shouldReturn403WhenEmailNotConfirmed() throws Exception {
        // Arrange — l'utilisateur existe mais n'a pas confirmé son email
        LoginRequest request = new LoginRequest();
        request.setEmail("thomas@example.com");
        request.setPassword("Password123!");

        User user = new User("thomas@example.com", "hashed", "Thomas", "Bulens");
        user.setEnabled(false); // email non confirmé

        when(userService.findByEmail("thomas@example.com")).thenReturn(user);

        // Act & Assert — l'API doit retourner 403 Forbidden
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());

        // Le token ne doit jamais être généré si l'email n'est pas confirmé
        verify(jwtService, never()).generateToken(anyString());
    }

    // =========================================================
    // POST /api/auth/logout
    // =========================================================

    @Test
    void logout_shouldReturn200AndInvalidateToken() throws Exception {
        // Arrange
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("refresh-token-xyz");

        User user = new User("thomas@example.com", "hashed", "Thomas", "Bulens");
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token-xyz");
        refreshToken.setUser(user);

        when(refreshTokenService.findByToken("refresh-token-xyz")).thenReturn(refreshToken);
        doNothing().when(refreshTokenService).deleteByUser(user);

        // Act & Assert
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").exists());

        // Le token de refresh doit être supprimé à la déconnexion
        verify(refreshTokenService, times(1)).deleteByUser(user);
    }
}

import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ApplicationService } from './application';
import { Application, CreateApplicationRequest } from '../models/application';
import { ApplicationStatus } from '../enums/application-status';

/*
 * Tests unitaires pour ApplicationService.
 *
 * On utilise HttpTestingController pour intercepter les appels HTTP
 * sans toucher un vrai serveur. Chaque test vérifie :
 *   - que la bonne requête HTTP est envoyée (méthode + URL)
 *   - que le résultat est bien traité
 *   - que le cache se comporte correctement
 */
describe('ApplicationService', () => {
  let service: ApplicationService;
  let httpMock: HttpTestingController;

  const API_URL = 'http://localhost:8080/api/applications';

  // Données fictives réutilisées dans les tests
  const mockApplications: Application[] = [
    { id: 1, company: 'Google', position: 'Dev', applicationDate: '2026-01-15', currentStatus: ApplicationStatus.SENT },
    { id: 2, company: 'Meta', position: 'Engineer', applicationDate: '2026-01-20', currentStatus: ApplicationStatus.INTERVIEW },
  ];

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ApplicationService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(ApplicationService);
    httpMock = TestBed.inject(HttpTestingController);

    // Remet le cache à zéro avant chaque test
    service.invalidateCache();
  });

  afterEach(() => {
    // Vérifie qu'aucune requête HTTP inattendue n'est restée en attente
    httpMock.verify();
  });

  // =========================================================
  // getAllApplications
  // =========================================================

  it('should fetch all applications from API on cache miss', () => {
    // ARRANGE + ACT
    service.getAllApplications().subscribe(result => {
      // ASSERT
      expect(result).toHaveLength(2);
      expect(result[0].company).toBe('Google');
    });

    // Intercepte la requête et simule la réponse
    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('GET');
    req.flush(mockApplications);
  });

  it('should return cached data on second call within TTL', () => {
    // ARRANGE — premier appel qui remplit le cache
    service.getAllApplications().subscribe();
    httpMock.expectOne(API_URL).flush(mockApplications);

    // ACT — deuxième appel dans les 30s (cache hit)
    service.getAllApplications().subscribe(result => {
      // ASSERT — les données viennent du cache
      expect(result).toHaveLength(2);
    });

    // Aucune requête HTTP ne doit être faite cette fois
    httpMock.expectNone(API_URL);
  });

  it('should invalidate cache after createApplication', () => {
    // ARRANGE — on remplit le cache
    service.getAllApplications().subscribe();
    httpMock.expectOne(API_URL).flush(mockApplications);

    // ACT — on crée une candidature (doit invalider le cache)
    const newApp: CreateApplicationRequest = {
      company: 'Apple',
      position: 'Designer',
      applicationDate: '2026-02-01',
      currentStatus: ApplicationStatus.SENT,
    };
    service.createApplication(newApp).subscribe();
    httpMock.expectOne(API_URL).flush({ id: 3, ...newApp });

    // ASSERT — le prochain getAllApplications doit refaire un appel HTTP
    service.getAllApplications().subscribe();
    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('GET');
    req.flush(mockApplications);
  });

  // =========================================================
  // getApplicationById
  // =========================================================

  it('should fetch a single application by id', () => {
    const mockApp = mockApplications[0];

    service.getApplicationById(1).subscribe(result => {
      expect(result.id).toBe(1);
      expect(result.company).toBe('Google');
    });

    const req = httpMock.expectOne(`${API_URL}/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockApp);
  });

  // =========================================================
  // createApplication
  // =========================================================

  it('should POST to create an application', () => {
    const newApp: CreateApplicationRequest = {
      company: 'Apple',
      position: 'Designer',
      applicationDate: '2026-02-01',
      currentStatus: ApplicationStatus.SENT,
    };
    const created: Application = { id: 3, ...newApp };

    service.createApplication(newApp).subscribe(result => {
      expect(result.id).toBe(3);
      expect(result.company).toBe('Apple');
    });

    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newApp); // vérifie ce qui est envoyé
    req.flush(created);
  });

  // =========================================================
  // deleteApplication
  // =========================================================

  it('should DELETE an application by id', () => {
    service.deleteApplication(1).subscribe();

    const req = httpMock.expectOne(`${API_URL}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should invalidate cache after deleteApplication', () => {
    // ARRANGE — on remplit le cache
    service.getAllApplications().subscribe();
    httpMock.expectOne(API_URL).flush(mockApplications);

    // ACT — suppression
    service.deleteApplication(1).subscribe();
    httpMock.expectOne(`${API_URL}/1`).flush(null);

    // ASSERT — cache invalidé, prochain appel refait une requête HTTP
    service.getAllApplications().subscribe();
    const req = httpMock.expectOne(API_URL);
    expect(req.request.method).toBe('GET');
    req.flush([mockApplications[1]]);
  });
});

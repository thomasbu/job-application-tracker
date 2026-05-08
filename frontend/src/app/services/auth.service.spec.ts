import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { AuthService } from './auth';
import { TokenService } from './token';
import { AuthResponse } from '../models/auth/auth-response';

/*
 * Tests unitaires pour AuthService.
 *
 * AuthService combine des appels HTTP (login, logout)
 * et de la logique locale (isAuthenticated, getCurrentUser).
 * On utilise le vrai TokenService avec localStorage (nettoyé avant chaque test).
 */
describe('AuthService', () => {
  let service: AuthService;
  let tokenService: TokenService;
  let httpMock: HttpTestingController;

  const API_URL = 'http://localhost:8080/api/auth';

  // Crée un faux JWT avec une expiration configurable
  // Le payload JWT est base64({"exp": timestamp, "sub": email})
  function fakeJwt(expiredInMs: number): string {
    const payload = {
      exp: Math.floor((Date.now() + expiredInMs) / 1000),
      sub: 'test@example.com',
    };
    return `header.${btoa(JSON.stringify(payload))}.signature`;
  }

  beforeEach(() => {
    // Nettoyage localStorage avant chaque test pour l'isolation
    localStorage.clear();

    TestBed.configureTestingModule({
      providers: [
        AuthService,
        TokenService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(AuthService);
    tokenService = TestBed.inject(TokenService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    localStorage.clear();
    httpMock.verify();
  });

  // =========================================================
  // login
  // =========================================================

  it('should POST to login and store tokens', () => {
    // ARRANGE
    const loginRequest = { email: 'thomas@example.com', password: 'Password123!' };
    const mockResponse: AuthResponse = {
      accessToken: fakeJwt(3600000),
      refreshToken: 'refresh-token-xyz',
      user: { id: 1, email: 'thomas@example.com', firstName: 'Thomas', lastName: 'Bulens', role: 'USER' as const },
    };

    // ACT
    service.login(loginRequest).subscribe(response => {
      // ASSERT — la réponse est bien retournée
      expect(response.accessToken).toBeTruthy();
      expect(response.user.email).toBe('thomas@example.com');
    });

    const req = httpMock.expectOne(`${API_URL}/login`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(loginRequest);
    req.flush(mockResponse);

    // ASSERT — les tokens sont bien sauvegardés dans localStorage
    expect(tokenService.getAccessToken()).toBeTruthy();
    expect(tokenService.getRefreshToken()).toBe('refresh-token-xyz');
  });

  it('should update currentUser signal after login', () => {
    const loginRequest = { email: 'thomas@example.com', password: 'Password123!' };
    const mockResponse: AuthResponse = {
      accessToken: fakeJwt(3600000),
      refreshToken: 'refresh-xyz',
      user: { id: 1, email: 'thomas@example.com', firstName: 'Thomas', lastName: 'Bulens', role: 'USER' as const },
    };

    service.login(loginRequest).subscribe();
    httpMock.expectOne(`${API_URL}/login`).flush(mockResponse);

    // ASSERT — le signal currentUser est bien mis à jour
    expect(service.getCurrentUser()?.email).toBe('thomas@example.com');
  });

  // =========================================================
  // logout
  // =========================================================

  it('should POST to logout and clear tokens', () => {
    // ARRANGE — on simule un user connecté
    tokenService.setTokens(fakeJwt(3600000), 'refresh-xyz');

    // ACT
    service.logout().subscribe();
    httpMock.expectOne(`${API_URL}/logout`).flush({ message: 'Logged out' });

    // ASSERT — tokens supprimés du localStorage
    expect(tokenService.getAccessToken()).toBeNull();
    expect(tokenService.getRefreshToken()).toBeNull();
  });

  it('should clear currentUser signal after logout', () => {
    // ARRANGE — on simule un user connecté
    const user = { id: 1, email: 'thomas@example.com', firstName: 'Thomas', lastName: 'Bulens', role: 'USER' as const };
    tokenService.setUser(user);
    tokenService.setTokens(fakeJwt(3600000), 'refresh-xyz');

    service.logout().subscribe();
    httpMock.expectOne(`${API_URL}/logout`).flush({});

    // ASSERT — le signal est réinitialisé à null
    expect(service.getCurrentUser()).toBeNull();
  });

  // =========================================================
  // isAuthenticated
  // =========================================================

  it('should return true when a valid non-expired token exists', () => {
    // ARRANGE — token valide pour 1 heure
    tokenService.setTokens(fakeJwt(3600000), 'refresh-xyz');

    expect(service.isAuthenticated()).toBe(true);
  });

  it('should return false when no token exists', () => {
    localStorage.clear();
    expect(service.isAuthenticated()).toBe(false);
  });

  it('should return false when token is expired', () => {
    // ARRANGE — token expiré depuis 1 heure
    tokenService.setTokens(fakeJwt(-3600000), 'refresh-xyz');

    expect(service.isAuthenticated()).toBe(false);
  });

  // =========================================================
  // getCurrentUser
  // =========================================================

  it('should return null when no user is stored', () => {
    // localStorage est vidé dans beforeEach, donc getCurrentUser() doit être null
    expect(service.getCurrentUser()).toBeNull();
  });
});

import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

import { TokenService } from './token';
import { AuthResponse } from '../models/auth/auth-response';
import { LoginRequest } from '../models/auth/login-request';
import { RegisterRequest } from '../models/auth/register-request';
import { User } from '../models/auth/user';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth';

  // Signal réactif pour le user actuel
  private _currentUser = signal<User | null>(null);

  // Expose en lecture seule
  currentUser = this._currentUser.asReadonly();

  constructor(private http: HttpClient, private tokenService: TokenService) {
    // Charge le user au démarrage depuis localStorage
    const storedUser = this.tokenService.getUser();
    this._currentUser.set(storedUser);
  }

  register(request: RegisterRequest): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, request);
  }

  confirmEmail(token: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/confirm`, { params: { token } });
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request).pipe(
      tap((response) => {
        this.tokenService.setTokens(response.accessToken, response.refreshToken);
        this.tokenService.setUser(response.user);
        this._currentUser.set(response.user); // ← MET À JOUR LE SIGNAL
      })
    );
  }

  logout(): Observable<any> {
    return this.http.post(`${this.apiUrl}/logout`, {}).pipe(
      tap(() => {
        this.tokenService.removeTokens();
        this._currentUser.set(null); // ← RÉINITIALISE LE SIGNAL
      })
    );
  }

  refreshToken(): Observable<{ accessToken: string }> {
    const refreshToken = this.tokenService.getRefreshToken();
    return this.http.post<{ accessToken: string }>(`${this.apiUrl}/refresh`, { refreshToken }).pipe(
      tap((response) => {
        localStorage.setItem('access_token', response.accessToken);
      })
    );
  }

  forgotPassword(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgot-password`, { email });
  }

  resetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-password`, { token, newPassword });
  }

  isAuthenticated(): boolean {
    const token = this.tokenService.getAccessToken();
    if (!token) return false;
    return !this.tokenService.isTokenExpired(token);
  }

  getCurrentUser(): User | null {
    return this._currentUser();
  }

  clearSession(): void {
    this.tokenService.removeTokens();
    this._currentUser.set(null); // ← RÉINITIALISE LE SIGNAL
  }
}

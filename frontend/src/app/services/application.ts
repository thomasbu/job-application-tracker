import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, tap, delay } from 'rxjs';
import {
  Application,
  CreateApplicationRequest,
  UpdateApplicationRequest,
} from '../models/application';
import { ApplicationStatus } from '../enums/application-status';

@Injectable({
  providedIn: 'root',
})
export class ApplicationService {
  private apiUrl = 'http://localhost:8080/api/applications';

  // Cache simple
  private applicationsCache = signal<Application[] | null>(null);
  private cacheTimestamp = 0;
  private readonly CACHE_TTL = 30000; // 30 secondes

  constructor(private http: HttpClient) {}

  getAllApplications(): Observable<Application[]> {
    const now = Date.now();

    // Cache hit
    if (this.applicationsCache() && now - this.cacheTimestamp < this.CACHE_TTL) {
      console.log('📦 Cache hit - returning cached data');
      return of(this.applicationsCache()!).pipe(delay(0)); // ← CORRIGÉ
    }

    // Cache miss
    console.log('🌐 Cache miss - fetching from API');
    return this.http.get<Application[]>(this.apiUrl).pipe(
      tap((data) => {
        this.applicationsCache.set(data);
        this.cacheTimestamp = now;
      })
    );
  }

  getApplicationsByStatus(status: ApplicationStatus): Observable<Application[]> {
    const params = new HttpParams().set('status', status);
    return this.http.get<Application[]>(this.apiUrl, { params });
  }

  getApplicationById(id: number): Observable<Application> {
    return this.http.get<Application>(`${this.apiUrl}/${id}`);
  }

  createApplication(request: CreateApplicationRequest): Observable<Application> {
    return this.http
      .post<Application>(this.apiUrl, request)
      .pipe(tap(() => this.invalidateCache()));
  }

  updateApplication(id: number, request: UpdateApplicationRequest): Observable<Application> {
    return this.http
      .put<Application>(`${this.apiUrl}/${id}`, request)
      .pipe(tap(() => this.invalidateCache()));
  }

  deleteApplication(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`).pipe(tap(() => this.invalidateCache()));
  }

  /**
   * Invalide le cache
   * À appeler après create/update/delete
   */
  invalidateCache(): void {
    console.log('🗑️ Cache invalidated');
    this.applicationsCache.set(null);
    this.cacheTimestamp = 0;
  }
}

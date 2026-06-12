import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, of, tap, delay, map } from 'rxjs';
import {
  Application,
  CreateApplicationRequest,
  PageResponse,
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
      return of(this.applicationsCache()!).pipe(delay(0));
    }

    // Cache miss — size=1000 to load all and keep client-side sort/filter/pagination
    console.log('🌐 Cache miss - fetching from API');
    const params = new HttpParams().set('page', '0').set('size', '1000');
    return this.http.get<PageResponse<Application>>(this.apiUrl, { params }).pipe(
      map((page) => page.content),
      tap((data) => {
        this.applicationsCache.set(data);
        this.cacheTimestamp = now;
      })
    );
  }

  getApplicationsByStatus(status: ApplicationStatus): Observable<Application[]> {
    const params = new HttpParams().set('status', status).set('page', '0').set('size', '1000');
    return this.http.get<PageResponse<Application>>(this.apiUrl, { params }).pipe(
      map((page) => page.content)
    );
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

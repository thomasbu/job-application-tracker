import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { StudySession, CreateStudySessionRequest } from '../models/interview-prep/study-session';

@Injectable({ providedIn: 'root' })
export class StudySessionService {
  private apiUrl = 'http://localhost:8080/api/study-sessions';

  constructor(private http: HttpClient) {}

  getAll(): Observable<StudySession[]> {
    return this.http.get<StudySession[]>(this.apiUrl);
  }

  getById(id: number): Observable<StudySession> {
    return this.http.get<StudySession>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateStudySessionRequest): Observable<StudySession> {
    return this.http.post<StudySession>(this.apiUrl, request);
  }

  update(id: number, request: CreateStudySessionRequest): Observable<StudySession> {
    return this.http.put<StudySession>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getTotalMinutesThisWeek(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/week`);
  }

  getTotalMinutesThisMonth(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/month`);
  }
}

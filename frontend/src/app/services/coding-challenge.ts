import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  CodingChallenge,
  CreateCodingChallengeRequest,
  UpdateCodingChallengeRequest,
  ChallengeStatus,
} from '../models/interview-prep/coding-challenge';

@Injectable({ providedIn: 'root' })
export class CodingChallengeService {
  private apiUrl = 'http://localhost:8080/api/challenges';

  constructor(private http: HttpClient) {}

  getAll(): Observable<CodingChallenge[]> {
    return this.http.get<CodingChallenge[]>(this.apiUrl);
  }

  getByStatus(status: ChallengeStatus): Observable<CodingChallenge[]> {
    return this.http.get<CodingChallenge[]>(`${this.apiUrl}/status/${status}`);
  }

  getById(id: number): Observable<CodingChallenge> {
    return this.http.get<CodingChallenge>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateCodingChallengeRequest): Observable<CodingChallenge> {
    return this.http.post<CodingChallenge>(this.apiUrl, request);
  }

  update(id: number, request: UpdateCodingChallengeRequest): Observable<CodingChallenge> {
    return this.http.put<CodingChallenge>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  markCompleted(id: number): Observable<CodingChallenge> {
    return this.http.post<CodingChallenge>(`${this.apiUrl}/${id}/complete`, {});
  }
}

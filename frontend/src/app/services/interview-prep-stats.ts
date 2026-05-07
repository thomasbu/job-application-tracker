import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InterviewPrepStats } from '../models/interview-prep/stats';

@Injectable({ providedIn: 'root' })
export class InterviewPrepStatsService {
  private apiUrl = 'http://localhost:8080/api/interview-prep/stats';

  constructor(private http: HttpClient) {}

  getStats(): Observable<InterviewPrepStats> {
    return this.http.get<InterviewPrepStats>(this.apiUrl);
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Skill, CreateSkillRequest, UpdateSkillLevelRequest } from '../models/interview-prep/skill';

@Injectable({ providedIn: 'root' })
export class SkillService {
  private apiUrl = 'http://localhost:8080/api/skills';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Skill[]> {
    return this.http.get<Skill[]>(this.apiUrl);
  }

  getById(id: number): Observable<Skill> {
    return this.http.get<Skill>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateSkillRequest): Observable<Skill> {
    return this.http.post<Skill>(this.apiUrl, request);
  }

  updateLevel(id: number, request: UpdateSkillLevelRequest): Observable<Skill> {
    return this.http.put<Skill>(`${this.apiUrl}/${id}/level`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

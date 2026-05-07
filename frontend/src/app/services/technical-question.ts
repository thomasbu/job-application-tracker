import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  TechnicalQuestion,
  CreateTechnicalQuestionRequest,
  UpdateTechnicalQuestionRequest,
} from '../models/interview-prep/technical-question';
import { Category } from '../models/interview-prep/enums';

@Injectable({ providedIn: 'root' })
export class TechnicalQuestionService {
  private apiUrl = 'http://localhost:8080/api/questions';

  constructor(private http: HttpClient) {}

  getAll(): Observable<TechnicalQuestion[]> {
    return this.http.get<TechnicalQuestion[]>(this.apiUrl);
  }

  getByCategory(category: Category): Observable<TechnicalQuestion[]> {
    return this.http.get<TechnicalQuestion[]>(`${this.apiUrl}/category/${category}`);
  }

  getById(id: number): Observable<TechnicalQuestion> {
    return this.http.get<TechnicalQuestion>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateTechnicalQuestionRequest): Observable<TechnicalQuestion> {
    return this.http.post<TechnicalQuestion>(this.apiUrl, request);
  }

  update(id: number, request: UpdateTechnicalQuestionRequest): Observable<TechnicalQuestion> {
    return this.http.put<TechnicalQuestion>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateConfidence(id: number, confidence: number): Observable<TechnicalQuestion> {
    return this.http.put<TechnicalQuestion>(`${this.apiUrl}/${id}/confidence`, { confidence });
  }
}

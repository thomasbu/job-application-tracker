import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FlashCard, CreateFlashCardRequest, UpdateFlashCardRequest } from '../models/interview-prep/flashcard';
import { Category } from '../models/interview-prep/enums';

@Injectable({ providedIn: 'root' })
export class FlashCardService {
  private apiUrl = 'http://localhost:8080/api/flashcards';

  constructor(private http: HttpClient) {}

  getAll(): Observable<FlashCard[]> {
    return this.http.get<FlashCard[]>(this.apiUrl);
  }

  getByCategory(category: Category): Observable<FlashCard[]> {
    return this.http.get<FlashCard[]>(`${this.apiUrl}/category/${category}`);
  }

  getById(id: number): Observable<FlashCard> {
    return this.http.get<FlashCard>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateFlashCardRequest): Observable<FlashCard> {
    return this.http.post<FlashCard>(this.apiUrl, request);
  }

  update(id: number, request: UpdateFlashCardRequest): Observable<FlashCard> {
    return this.http.put<FlashCard>(`${this.apiUrl}/${id}`, request);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  markReviewed(id: number, confidence: number): Observable<FlashCard> {
    return this.http.post<FlashCard>(`${this.apiUrl}/${id}/review`, { confidence });
  }
}

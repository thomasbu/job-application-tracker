import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Document } from '../models/document';

@Injectable({
  providedIn: 'root',
})
export class DocumentService {
  private apiUrl = 'http://localhost:8080/api/applications';

  constructor(private http: HttpClient) {}

  getDocuments(applicationId: number): Observable<Document[]> {
    return this.http.get<Document[]>(`${this.apiUrl}/${applicationId}/documents`);
  }

  uploadDocument(applicationId: number, file: File): Observable<Document> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Document>(`${this.apiUrl}/${applicationId}/documents`, formData);
  }

  downloadDocument(applicationId: number, documentId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${applicationId}/documents/${documentId}/download`, {
      responseType: 'blob',
    });
  }

  deleteDocument(applicationId: number, documentId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${applicationId}/documents/${documentId}`);
  }
}

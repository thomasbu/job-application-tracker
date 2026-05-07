import { Category } from './enums';

export interface StudySession {
  id: number;
  date: string;
  topic: Category;
  durationMinutes: number;
  notes: string;
  createdAt: string;
}

export interface CreateStudySessionRequest {
  date: string;
  topic: Category;
  durationMinutes: number;
  notes?: string;
}

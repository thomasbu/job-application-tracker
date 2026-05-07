import { Category } from './enums';

export interface TechnicalQuestion {
  id: number;
  question: string;
  answer: string;
  category: Category;
  confidenceLevel: number;
  lastReviewed: string | null;
  createdAt: string;
}

export interface CreateTechnicalQuestionRequest {
  question: string;
  answer: string;
  category: Category;
}

export interface UpdateTechnicalQuestionRequest {
  question: string;
  answer: string;
  category: Category;
  confidenceLevel: number;
}

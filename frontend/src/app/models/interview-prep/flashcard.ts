import { Category, Difficulty } from './enums';

export interface FlashCard {
  id: number;
  question: string;
  answer: string;
  category: Category;
  difficulty: Difficulty;
  lastReviewed: string | null;
  reviewCount: number;
  confidenceLevel: number;
  createdAt: string;
}

export interface CreateFlashCardRequest {
  question: string;
  answer: string;
  category: Category;
  difficulty: Difficulty;
}

export interface UpdateFlashCardRequest {
  question: string;
  answer: string;
  category: Category;
  difficulty: Difficulty;
  confidenceLevel: number;
}

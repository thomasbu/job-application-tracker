import { Category } from './enums';

export interface Skill {
  id: number;
  name: string;
  category: Category;
  level: number;
  notes: string;
  updatedAt: string | null;
}

export interface CreateSkillRequest {
  name: string;
  category: Category;
}

export interface UpdateSkillLevelRequest {
  level: number;
  notes?: string;
}

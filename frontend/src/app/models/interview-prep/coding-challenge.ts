import { Difficulty } from './enums';

export enum ChallengeStatus {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  COMPLETED = 'COMPLETED',
  REVIEW = 'REVIEW',
}

export const CHALLENGE_STATUS_LABELS: { [key in ChallengeStatus]: string } = {
  [ChallengeStatus.TODO]: 'To Do',
  [ChallengeStatus.IN_PROGRESS]: 'In Progress',
  [ChallengeStatus.COMPLETED]: 'Completed',
  [ChallengeStatus.REVIEW]: 'Review',
};

export interface CodingChallenge {
  id: number;
  name: string;
  platform: string;
  difficulty: Difficulty;
  status: ChallengeStatus;
  link: string;
  notes: string;
  completedAt: string | null;
  createdAt: string;
}

export interface CreateCodingChallengeRequest {
  name: string;
  platform: string;
  difficulty: Difficulty;
  link?: string;
  notes?: string;
}

export interface UpdateCodingChallengeRequest {
  name: string;
  platform: string;
  difficulty: Difficulty;
  status: ChallengeStatus;
  link?: string;
  notes?: string;
}

import { ApplicationStatus } from '../enums/application-status';

export interface StatusHistory {
  id: number;
  status: ApplicationStatus;
  changedAt: string;
  notes?: string;
}

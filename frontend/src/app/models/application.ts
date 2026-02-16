import { ApplicationStatus } from '../enums/application-status';
import { Document } from '../models/document';
import { StatusHistory } from './status-history';

export interface Application {
  id?: number;
  company: string;
  position: string;
  applicationDate: string;
  currentStatus: ApplicationStatus;
  notes?: string;

  // Documents (NEW)
  documents?: Document[];
  documentCount?: number;

  // Status History (NEW)
  statusHistory?: StatusHistory[];

  // Timestamps
  createdAt?: string;
  updatedAt?: string;
}

export interface CreateApplicationRequest {
  company: string;
  position: string;
  applicationDate: string;
  currentStatus: ApplicationStatus;
  notes?: string;
}

export interface UpdateApplicationRequest {
  company?: string;
  position?: string;
  applicationDate?: string;
  currentStatus?: ApplicationStatus;
  notes?: string;
}

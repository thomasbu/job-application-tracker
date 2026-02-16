import { Component, Input, Output, EventEmitter, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import {
  Application,
  CreateApplicationRequest,
  UpdateApplicationRequest,
} from '../../models/application';
import { ApplicationStatus, APPLICATION_STATUS_LABELS } from '../../enums/application-status';
import { ApplicationService } from '../../services/application';
import { DocumentService } from '../../services/document';
import { Document } from '../../models/document';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog'; // ← AJOUTÉ
import { StatusTimelineComponent } from '../status-timeline/status-timeline';

@Component({
  selector: 'app-application-dialog',
  standalone: true,
  imports: [CommonModule, FormsModule, ConfirmDialogComponent, StatusTimelineComponent], // ← AJOUTÉ
  templateUrl: './application-dialog.html',
  styleUrl: './application-dialog.scss',
})
export class ApplicationDialogComponent implements OnInit {
  @Input() application: Application | null = null;
  @Output() close = new EventEmitter<boolean>();

  isEdit = false;
  isLoading = signal(false);

  // Documents
  documents = signal<Document[]>([]);
  selectedFiles = signal<File[]>([]);
  isUploadingDocs = signal(false);

  // Confirm delete document (NEW)
  showConfirmDeleteDoc = signal(false);
  documentToDelete = signal<Document | null>(null);

  statuses = Object.values(ApplicationStatus);
  statusLabels = APPLICATION_STATUS_LABELS;

  // Form data
  formData = {
    company: '',
    position: '',
    applicationDate: '',
    currentStatus: ApplicationStatus.SENT,
    notes: '',
  };

  constructor(
    private applicationService: ApplicationService,
    private documentService: DocumentService
  ) {}

  ngOnInit(): void {
    this.isEdit = !!this.application;

    if (this.application) {
      this.formData = {
        company: this.application.company,
        position: this.application.position,
        applicationDate: this.application.applicationDate,
        currentStatus: this.application.currentStatus,
        notes: this.application.notes || '',
      };

      if (this.application.id) {
        this.loadDocuments();
      }
    } else {
      const today = new Date().toISOString().split('T')[0];
      this.formData.applicationDate = today;
    }
  }

  loadDocuments(): void {
    if (!this.application?.id) return;

    this.documentService.getDocuments(this.application.id).subscribe({
      next: (docs) => this.documents.set(docs),
      error: () => this.showToast('Failed to load documents', 'error'),
    });
  }

  onFilesSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const filesArray = Array.from(input.files);

      const validFiles: File[] = [];
      for (const file of filesArray) {
        if (!this.validateFile(file)) {
          input.value = '';
          return;
        }
        validFiles.push(file);
      }

      this.selectedFiles.set(validFiles);
    }
  }

  validateFile(file: File): boolean {
    const maxSize = 10 * 1024 * 1024;
    if (file.size > maxSize) {
      this.showToast('File size exceeds 10MB limit: ' + file.name, 'error');
      return false;
    }

    const allowedTypes = [
      'application/pdf',
      'image/jpeg',
      'image/png',
      'image/jpg',
      'application/msword',
      'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
    ];

    if (!allowedTypes.includes(file.type)) {
      this.showToast('File type not allowed: ' + file.name, 'error');
      return false;
    }

    return true;
  }

  clearSelectedFiles(): void {
    this.selectedFiles.set([]);
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }

  uploadSelectedFiles(): void {
    const files = this.selectedFiles();
    if (files.length === 0 || !this.application?.id) return;

    this.isUploadingDocs.set(true);
    let completed = 0;
    let failed = 0;

    files.forEach((file) => {
      this.documentService.uploadDocument(this.application!.id!, file).subscribe({
        next: () => {
          completed++;
          if (completed + failed === files.length) {
            this.finishUpload(completed, failed);
          }
        },
        error: () => {
          failed++;
          if (completed + failed === files.length) {
            this.finishUpload(completed, failed);
          }
        },
      });
    });
  }

  private finishUpload(completed: number, failed: number): void {
    this.isUploadingDocs.set(false);
    this.clearSelectedFiles();

    if (failed === 0) {
      this.showToast(`${completed} document(s) uploaded successfully`, 'success');
    } else {
      this.showToast(`${completed} uploaded, ${failed} failed`, 'error');
    }

    this.loadDocuments();
  }

  downloadDocument(doc: Document): void {
    if (!this.application?.id) return;

    this.documentService.downloadDocument(this.application.id, doc.id).subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = doc.originalFilename;
        link.click();
        window.URL.revokeObjectURL(url);
        this.showToast('Document downloaded', 'success');
      },
      error: () => this.showToast('Failed to download document', 'error'),
    });
  }

  // ← MODIFIÉ : Ouvre le confirm dialog
  deleteDocument(document: Document): void {
    this.documentToDelete.set(document);
    this.showConfirmDeleteDoc.set(true);
  }

  // ← NOUVEAU : Confirme la suppression
  confirmDeleteDocument(): void {
    const document = this.documentToDelete();
    if (!this.application?.id || !document) return;

    this.documentService.deleteDocument(this.application.id, document.id).subscribe({
      next: () => {
        this.showToast('Document deleted', 'success');
        this.loadDocuments();
        this.cancelDeleteDocument();
      },
      error: () => {
        this.showToast('Failed to delete document', 'error');
        this.cancelDeleteDocument();
      },
    });
  }

  // ← NOUVEAU : Annule la suppression
  cancelDeleteDocument(): void {
    this.showConfirmDeleteDoc.set(false);
    this.documentToDelete.set(null);
  }

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i];
  }

  onSubmit(): void {
    if (!this.isFormValid()) {
      this.showToast('Please fill in all required fields', 'error');
      return;
    }

    this.isLoading.set(true);

    if (this.isEdit && this.application?.id) {
      this.updateApplication();
    } else {
      this.createApplication();
    }
  }

  private createApplication(): void {
    const request: CreateApplicationRequest = {
      company: this.formData.company,
      position: this.formData.position,
      applicationDate: this.formData.applicationDate,
      currentStatus: this.formData.currentStatus,
      notes: this.formData.notes || undefined,
    };

    this.applicationService.createApplication(request).subscribe({
      next: (created) => {
        // Si fichiers sélectionnés, on les upload
        if (this.selectedFiles().length > 0 && created.id) {
          this.uploadFilesAfterCreation(created.id);
        } else {
          this.isLoading.set(false);
          this.showToast('Application created successfully', 'success');
          this.close.emit(true);
        }
      },
      error: () => {
        this.isLoading.set(false);
        this.showToast('Failed to create application', 'error');
      },
    });
  }

  private uploadFilesAfterCreation(applicationId: number): void {
    const files = this.selectedFiles();
    let completed = 0;
    let failed = 0;
    const total = files.length;

    this.showToast(`Uploading ${total} document(s)...`, 'success');

    files.forEach((file) => {
      this.documentService.uploadDocument(applicationId, file).subscribe({
        next: () => {
          completed++;
          if (completed + failed === total) {
            this.finishCreationWithUpload(completed, failed);
          }
        },
        error: () => {
          failed++;
          if (completed + failed === total) {
            this.finishCreationWithUpload(completed, failed);
          }
        },
      });
    });
  }

  private finishCreationWithUpload(completed: number, failed: number): void {
    this.isLoading.set(false);

    if (failed === 0) {
      this.showToast(`Application created with ${completed} document(s)!`, 'success');
    } else {
      this.showToast(`Application created. ${completed} uploaded, ${failed} failed`, 'error');
    }

    this.close.emit(true);
  }

  private updateApplication(): void {
    const request: UpdateApplicationRequest = {
      company: this.formData.company,
      position: this.formData.position,
      applicationDate: this.formData.applicationDate,
      currentStatus: this.formData.currentStatus,
      notes: this.formData.notes || undefined,
    };

    this.applicationService.updateApplication(this.application!.id!, request).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.showToast('Application updated successfully', 'success');
        this.close.emit(true); // ← Émet true pour rafraîchir la liste principale !
      },
      error: () => {
        this.isLoading.set(false);
        this.showToast('Failed to update application', 'error');
      },
    });
  }

  private isFormValid(): boolean {
    return !!(
      this.formData.company.trim() &&
      this.formData.position.trim() &&
      this.formData.applicationDate &&
      this.formData.currentStatus
    );
  }

  onOverlayMouseDown(event: MouseEvent): void {
    // Fermer seulement si le mousedown ET mouseup sont sur l'overlay
    if (event.target === event.currentTarget) {
      this.onCancel();
    }
  }

  onCancel(): void {
    this.close.emit(false);
  }

  private showToast(message: string, type: 'success' | 'error'): void {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    setTimeout(() => toast.classList.add('show'), 10);
    setTimeout(() => {
      toast.classList.remove('show');
      setTimeout(() => toast.remove(), 300);
    }, 3000);
  }
}

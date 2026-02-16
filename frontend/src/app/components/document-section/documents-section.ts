import { Component, Input, Output, EventEmitter, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Document } from '../../models/document'; // ← CORRIGÉ
import { DocumentService } from '../../services/document'; // ← CORRIGÉ

@Component({
  selector: 'app-documents-section',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './documents-section.html',
  styleUrl: './documents-section.scss',
})
export class DocumentsSectionComponent {
  @Input() applicationId!: number;
  @Input() documents: Document[] = [];
  @Output() documentsChanged = new EventEmitter<void>();

  isUploading = signal(false);
  selectedFiles = signal<FileList | null>(null);

  constructor(private documentService: DocumentService) {}

  onFilesSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFiles.set(input.files);
    }
  }

  uploadFiles(): void {
    const files = this.selectedFiles();
    if (!files || files.length === 0) return;

    this.isUploading.set(true);
    const uploadPromises: Promise<any>[] = [];

    // Upload each file
    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      const promise = this.documentService.uploadDocument(this.applicationId, file).toPromise();
      uploadPromises.push(promise);
    }

    Promise.all(uploadPromises)
      .then(() => {
        this.isUploading.set(false);
        this.selectedFiles.set(null);
        this.clearFileInput();
        this.showToast('Documents uploaded successfully', 'success');
        this.documentsChanged.emit();
      })
      .catch(() => {
        this.isUploading.set(false);
        this.showToast('Failed to upload documents', 'error');
      });
  }

  downloadDocument(doc: Document): void {
    this.documentService.downloadDocument(this.applicationId, doc.id).subscribe({
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

  deleteDocument(doc: Document): void {
    if (!confirm(`Delete "${doc.originalFilename}"?`)) return;

    this.documentService.deleteDocument(this.applicationId, doc.id).subscribe({
      next: () => {
        this.showToast('Document deleted', 'success');
        this.documentsChanged.emit();
      },
      error: () => this.showToast('Failed to delete document', 'error'),
    });
  }

  clearSelectedFiles(): void {
    this.selectedFiles.set(null);
    this.clearFileInput();
  }

  formatFileSize(bytes: number): string {
    if (bytes === 0) return '0 Bytes';
    const k = 1024;
    const sizes = ['Bytes', 'KB', 'MB', 'GB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i];
  }

  private clearFileInput(): void {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
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

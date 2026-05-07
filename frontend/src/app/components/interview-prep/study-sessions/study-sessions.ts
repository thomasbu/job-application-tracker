import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { StudySessionService } from '../../../services/study-session';
import { StudySession, CreateStudySessionRequest } from '../../../models/interview-prep/study-session';
import { Category, CATEGORY_LABELS } from '../../../models/interview-prep/enums';
import { ModalComponent } from '../../shared/modal/modal.component';

@Component({
  selector: 'app-study-sessions',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ModalComponent],
  templateUrl: './study-sessions.html',
  styleUrl: './study-sessions.scss',
})
export class StudySessionsComponent implements OnInit {
  sessions = signal<StudySession[]>([]);
  loading = signal(true);
  weeklyMinutes = signal(0);
  monthlyMinutes = signal(0);

  showForm = signal(false);
  editingSession = signal<StudySession | null>(null);
  formData = signal<CreateStudySessionRequest>({
    date: new Date().toISOString().split('T')[0],
    topic: Category.JAVA,
    durationMinutes: 30,
  });

  categories = Object.values(Category);
  categoryLabels = CATEGORY_LABELS;

  constructor(private sessionService: StudySessionService) {}

  ngOnInit(): void {
    this.loadSessions();
    this.loadStats();
  }

  loadSessions(): void {
    this.loading.set(true);
    this.sessionService.getAll().subscribe({
      next: (data) => {
        this.sessions.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showToast('Failed to load sessions', 'error');
      },
    });
  }

  loadStats(): void {
    this.sessionService.getTotalMinutesThisWeek().subscribe({
      next: (mins) => this.weeklyMinutes.set(mins),
    });
    this.sessionService.getTotalMinutesThisMonth().subscribe({
      next: (mins) => this.monthlyMinutes.set(mins),
    });
  }

  formatDuration(minutes: number): string {
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    if (h === 0) return `${m}m`;
    return m === 0 ? `${h}h` : `${h}h ${m}m`;
  }

  updateFormField(field: string, value: any): void {
    this.formData.update((d) => ({ ...d, [field]: value }));
  }

  updateDurationMinutes(value: any): void {
    this.formData.update((d) => ({ ...d, durationMinutes: +value }));
  }

  openCreateForm(): void {
    this.editingSession.set(null);
    this.formData.set({
      date: new Date().toISOString().split('T')[0],
      topic: Category.JAVA,
      durationMinutes: 30,
    });
    this.showForm.set(true);
  }

  openEditForm(session: StudySession): void {
    this.editingSession.set(session);
    this.formData.set({
      date: session.date,
      topic: session.topic,
      durationMinutes: session.durationMinutes,
      notes: session.notes,
    });
    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.editingSession.set(null);
  }

  saveSession(): void {
    const data = this.formData();
    const editing = this.editingSession();

    if (editing) {
      this.sessionService.update(editing.id, data).subscribe({
        next: () => {
          this.showToast('Session updated', 'success');
          this.closeForm();
          this.loadSessions();
          this.loadStats();
        },
        error: () => this.showToast('Failed to update', 'error'),
      });
    } else {
      this.sessionService.create(data).subscribe({
        next: () => {
          this.showToast('Session logged', 'success');
          this.closeForm();
          this.loadSessions();
          this.loadStats();
        },
        error: () => this.showToast('Failed to create', 'error'),
      });
    }
  }

  deleteSession(id: number): void {
    if (!confirm('Delete this session?')) return;
    this.sessionService.delete(id).subscribe({
      next: () => {
        this.showToast('Session deleted', 'success');
        this.loadSessions();
        this.loadStats();
      },
      error: () => this.showToast('Failed to delete', 'error'),
    });
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

import { Component, computed, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { CodingChallengeService } from '../../../services/coding-challenge';
import {
  CodingChallenge,
  CreateCodingChallengeRequest,
  ChallengeStatus,
  CHALLENGE_STATUS_LABELS,
} from '../../../models/interview-prep/coding-challenge';
import { Difficulty, DIFFICULTY_LABELS } from '../../../models/interview-prep/enums';
import { ModalComponent } from '../../shared/modal/modal.component';

@Component({
  selector: 'app-coding-challenges',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ModalComponent],
  templateUrl: './coding-challenges.html',
  styleUrl: './coding-challenges.scss',
})
export class CodingChallengesComponent implements OnInit {
  challenges = signal<CodingChallenge[]>([]);
  loading = signal(true);
  selectedStatus = signal<ChallengeStatus | 'ALL'>('ALL');

  showForm = signal(false);
  editingChallenge = signal<CodingChallenge | null>(null);
  formData = signal<CreateCodingChallengeRequest>({
    name: '',
    platform: 'LeetCode',
    difficulty: Difficulty.MEDIUM,
  });

  statuses = Object.values(ChallengeStatus);
  difficulties = Object.values(Difficulty);
  statusLabels = CHALLENGE_STATUS_LABELS;
  difficultyLabels = DIFFICULTY_LABELS;

  filteredChallenges = computed(() => {
    const status = this.selectedStatus();
    if (status === 'ALL') return this.challenges();
    return this.challenges().filter((c) => c.status === status);
  });

  stats = computed(() => {
    const all = this.challenges();
    return {
      total: all.length,
      completed: all.filter((c) => c.status === ChallengeStatus.COMPLETED).length,
      inProgress: all.filter((c) => c.status === ChallengeStatus.IN_PROGRESS).length,
      todo: all.filter((c) => c.status === ChallengeStatus.TODO).length,
    };
  });

  constructor(private challengeService: CodingChallengeService) {}

  ngOnInit(): void {
    this.loadChallenges();
  }

  loadChallenges(): void {
    this.loading.set(true);
    this.challengeService.getAll().subscribe({
      next: (data) => {
        this.challenges.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showToast('Failed to load challenges', 'error');
      },
    });
  }

  onStatusFilterChange(event: Event): void {
    this.selectedStatus.set((event.target as HTMLSelectElement).value as ChallengeStatus | 'ALL');
  }

  markCompleted(id: number): void {
    this.challengeService.markCompleted(id).subscribe({
      next: () => {
        this.showToast('Challenge completed!', 'success');
        this.loadChallenges();
      },
      error: () => this.showToast('Failed to mark complete', 'error'),
    });
  }

  openCreateForm(): void {
    this.editingChallenge.set(null);
    this.formData.set({ name: '', platform: 'LeetCode', difficulty: Difficulty.MEDIUM });
    this.showForm.set(true);
  }

  openEditForm(challenge: CodingChallenge): void {
    this.editingChallenge.set(challenge);
    this.formData.set({
      name: challenge.name,
      platform: challenge.platform,
      difficulty: challenge.difficulty,
      link: challenge.link,
      notes: challenge.notes,
    });
    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.editingChallenge.set(null);
  }

  saveChallenge(): void {
    const data = this.formData();
    const editing = this.editingChallenge();

    if (editing) {
      this.challengeService
        .update(editing.id, { ...data, status: editing.status })
        .subscribe({
          next: () => {
            this.showToast('Challenge updated', 'success');
            this.closeForm();
            this.loadChallenges();
          },
          error: () => this.showToast('Failed to update', 'error'),
        });
    } else {
      this.challengeService.create(data).subscribe({
        next: () => {
          this.showToast('Challenge created', 'success');
          this.closeForm();
          this.loadChallenges();
        },
        error: () => this.showToast('Failed to create', 'error'),
      });
    }
  }

  updateFormField(field: string, value: any): void {
    this.formData.update((d) => ({ ...d, [field]: value }));
  }

  deleteChallenge(id: number): void {
    if (!confirm('Delete this challenge?')) return;
    this.challengeService.delete(id).subscribe({
      next: () => {
        this.showToast('Challenge deleted', 'success');
        this.loadChallenges();
      },
      error: () => this.showToast('Failed to delete', 'error'),
    });
  }

  getStatusClass(status: ChallengeStatus): string {
    switch (status) {
      case ChallengeStatus.TODO: return 'badge-warn';
      case ChallengeStatus.IN_PROGRESS: return 'badge-warning';
      case ChallengeStatus.COMPLETED: return 'badge-success';
      case ChallengeStatus.REVIEW: return 'badge-accent';
      default: return 'badge-primary';
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

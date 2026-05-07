import { Component, computed, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { TechnicalQuestionService } from '../../../services/technical-question';
import {
  TechnicalQuestion,
  CreateTechnicalQuestionRequest,
} from '../../../models/interview-prep/technical-question';
import { Category, CATEGORY_LABELS } from '../../../models/interview-prep/enums';
import { ModalComponent } from '../../shared/modal/modal.component';

@Component({
  selector: 'app-technical-questions',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ModalComponent],
  templateUrl: './technical-questions.html',
  styleUrl: './technical-questions.scss',
})
export class TechnicalQuestionsComponent implements OnInit {
  questions = signal<TechnicalQuestion[]>([]);
  loading = signal(true);
  selectedCategory = signal<Category | 'ALL'>('ALL');
  expandedId = signal<number | null>(null);

  showForm = signal(false);
  editingQuestion = signal<TechnicalQuestion | null>(null);
  formData = signal<CreateTechnicalQuestionRequest>({
    question: '',
    answer: '',
    category: Category.JAVA,
  });

  categories = Object.values(Category);
  categoryLabels = CATEGORY_LABELS;

  filteredQuestions = computed(() => {
    const cat = this.selectedCategory();
    if (cat === 'ALL') return this.questions();
    return this.questions().filter((q) => q.category === cat);
  });

  constructor(private questionService: TechnicalQuestionService) {}

  ngOnInit(): void {
    this.loadQuestions();
  }

  loadQuestions(): void {
    this.loading.set(true);
    this.questionService.getAll().subscribe({
      next: (data) => {
        this.questions.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showToast('Failed to load questions', 'error');
      },
    });
  }

  onCategoryChange(event: Event): void {
    this.selectedCategory.set((event.target as HTMLSelectElement).value as Category | 'ALL');
  }

  toggleExpand(id: number): void {
    this.expandedId.set(this.expandedId() === id ? null : id);
  }

  rateConfidence(id: number, confidence: number): void {
    this.questionService.updateConfidence(id, confidence).subscribe({
      next: (updated) => {
        this.questions.update((qs) => qs.map((q) => (q.id === updated.id ? updated : q)));
      },
      error: () => this.showToast('Failed to update confidence', 'error'),
    });
  }

  openCreateForm(): void {
    this.editingQuestion.set(null);
    this.formData.set({ question: '', answer: '', category: Category.JAVA });
    this.showForm.set(true);
  }

  openEditForm(q: TechnicalQuestion): void {
    this.editingQuestion.set(q);
    this.formData.set({ question: q.question, answer: q.answer, category: q.category });
    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.editingQuestion.set(null);
  }

  saveQuestion(): void {
    const data = this.formData();
    const editing = this.editingQuestion();

    if (editing) {
      this.questionService
        .update(editing.id, { ...data, confidenceLevel: editing.confidenceLevel })
        .subscribe({
          next: () => {
            this.showToast('Question updated', 'success');
            this.closeForm();
            this.loadQuestions();
          },
          error: () => this.showToast('Failed to update', 'error'),
        });
    } else {
      this.questionService.create(data).subscribe({
        next: () => {
          this.showToast('Question created', 'success');
          this.closeForm();
          this.loadQuestions();
        },
        error: () => this.showToast('Failed to create', 'error'),
      });
    }
  }

  updateFormField(field: string, value: any): void {
    this.formData.update((d) => ({ ...d, [field]: value }));
  }

  deleteQuestion(id: number): void {
    if (!confirm('Delete this question?')) return;
    this.questionService.delete(id).subscribe({
      next: () => {
        this.showToast('Question deleted', 'success');
        this.loadQuestions();
      },
      error: () => this.showToast('Failed to delete', 'error'),
    });
  }

  getConfidenceStars(level: number): string {
    return '\u2605'.repeat(level) + '\u2606'.repeat(5 - level);
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

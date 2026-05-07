import { Component, computed, HostListener, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { FlashCardService } from '../../../services/flashcard';
import { FlashCard, CreateFlashCardRequest } from '../../../models/interview-prep/flashcard';
import { Category, Difficulty, CATEGORY_LABELS, DIFFICULTY_LABELS } from '../../../models/interview-prep/enums';
import { ModalComponent } from '../../shared/modal/modal.component';

@Component({
  selector: 'app-flashcards',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ModalComponent],
  templateUrl: './flashcards.html',
  styleUrl: './flashcards.scss',
})
export class FlashcardsComponent implements OnInit {
  flashcards = signal<FlashCard[]>([]);
  loading = signal(true);
  selectedCategory = signal<Category | 'ALL'>('ALL');
  selectedDifficulty = signal<Difficulty | 'ALL'>('ALL');

  // Study mode
  studyMode = signal(false);
  currentCardIndex = signal(0);
  isFlipped = signal(false);

  // View modal
  viewingCard = signal<FlashCard | null>(null);

  // Form
  showForm = signal(false);
  editingCard = signal<FlashCard | null>(null);
  formData = signal<CreateFlashCardRequest>({
    question: '',
    answer: '',
    category: Category.JAVA,
    difficulty: Difficulty.MEDIUM,
  });

  categories = Object.values(Category);
  difficulties = Object.values(Difficulty);
  categoryLabels = CATEGORY_LABELS;
  difficultyLabels = DIFFICULTY_LABELS;

  filteredCards = computed(() => {
    let cards = this.flashcards();
    const cat = this.selectedCategory();
    const diff = this.selectedDifficulty();

    if (cat !== 'ALL') {
      cards = cards.filter((c) => c.category === cat);
    }
    if (diff !== 'ALL') {
      cards = cards.filter((c) => c.difficulty === diff);
    }
    return cards;
  });

  currentCard = computed(() => {
    const cards = this.filteredCards();
    const idx = this.currentCardIndex();
    return cards.length > 0 ? cards[idx] : null;
  });

  constructor(private flashCardService: FlashCardService) {}

  ngOnInit(): void {
    this.loadFlashcards();
  }

  loadFlashcards(): void {
    this.loading.set(true);
    this.flashCardService.getAll().subscribe({
      next: (data) => {
        this.flashcards.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showToast('Failed to load flashcards', 'error');
      },
    });
  }

  onCategoryChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as Category | 'ALL';
    this.selectedCategory.set(value);
    this.currentCardIndex.set(0);
  }

  onDifficultyChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as Difficulty | 'ALL';
    this.selectedDifficulty.set(value);
    this.currentCardIndex.set(0);
  }

  // Study Mode
  startStudyMode(): void {
    if (this.filteredCards().length === 0) return;
    this.studyMode.set(true);
    this.currentCardIndex.set(0);
    this.isFlipped.set(false);
  }

  exitStudyMode(): void {
    this.studyMode.set(false);
    this.isFlipped.set(false);
  }

  flipCard(): void {
    this.isFlipped.update((v) => !v);
  }

  nextCard(): void {
    const max = this.filteredCards().length - 1;
    if (this.currentCardIndex() < max) {
      this.currentCardIndex.update((i) => i + 1);
      this.isFlipped.set(false);
    }
  }

  previousCard(): void {
    if (this.currentCardIndex() > 0) {
      this.currentCardIndex.update((i) => i - 1);
      this.isFlipped.set(false);
    }
  }

  rateCard(confidence: number): void {
    const card = this.currentCard();
    if (!card) return;

    this.flashCardService.markReviewed(card.id, confidence).subscribe({
      next: (updated) => {
        this.flashcards.update((cards) =>
          cards.map((c) => (c.id === updated.id ? updated : c))
        );
        this.nextCard();
      },
      error: () => this.showToast('Failed to rate card', 'error'),
    });
  }

  // View modal
  openViewModal(card: FlashCard): void {
    this.viewingCard.set(card);
  }

  closeViewModal(): void {
    this.viewingCard.set(null);
  }

  openEditFromView(): void {
    const card = this.viewingCard();
    if (!card) return;
    this.closeViewModal();
    this.openEditForm(card);
  }

  deleteCardFromView(card: FlashCard): void {
    if (!confirm('Delete this flashcard?')) return;
    this.flashCardService.delete(card.id).subscribe({
      next: () => {
        this.showToast('Flashcard deleted', 'success');
        this.closeViewModal();
        this.loadFlashcards();
      },
      error: () => this.showToast('Failed to delete flashcard', 'error'),
    });
  }

  formatDate(dateStr: string | null): string {
    if (!dateStr) return 'Never';
    return new Date(dateStr).toLocaleDateString();
  }

  // CRUD
  openCreateForm(): void {
    this.editingCard.set(null);
    this.formData.set({
      question: '',
      answer: '',
      category: Category.JAVA,
      difficulty: Difficulty.MEDIUM,
    });
    this.showForm.set(true);
  }

  openEditForm(card: FlashCard): void {
    this.editingCard.set(card);
    this.formData.set({
      question: card.question,
      answer: card.answer,
      category: card.category,
      difficulty: card.difficulty,
    });
    this.showForm.set(true);
  }

  closeForm(): void {
    this.showForm.set(false);
    this.editingCard.set(null);
  }

  saveCard(): void {
    const data = this.formData();
    const editing = this.editingCard();

    if (editing) {
      this.flashCardService
        .update(editing.id, { ...data, confidenceLevel: editing.confidenceLevel })
        .subscribe({
          next: () => {
            this.showToast('Flashcard updated', 'success');
            this.closeForm();
            this.loadFlashcards();
          },
          error: () => this.showToast('Failed to update flashcard', 'error'),
        });
    } else {
      this.flashCardService.create(data).subscribe({
        next: () => {
          this.showToast('Flashcard created', 'success');
          this.closeForm();
          this.loadFlashcards();
        },
        error: () => this.showToast('Failed to create flashcard', 'error'),
      });
    }
  }

  updateFormField(field: string, value: any): void {
    this.formData.update((d) => ({ ...d, [field]: value }));
  }

  deleteCard(id: number): void {
    if (!confirm('Delete this flashcard?')) return;
    this.flashCardService.delete(id).subscribe({
      next: () => {
        this.showToast('Flashcard deleted', 'success');
        this.loadFlashcards();
      },
      error: () => this.showToast('Failed to delete flashcard', 'error'),
    });
  }

  // Keyboard shortcuts for study mode
  @HostListener('window:keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    if (!this.studyMode()) return;

    if (event.code === 'Space') {
      event.preventDefault();
      this.flipCard();
    } else if (event.code === 'ArrowRight') {
      this.nextCard();
    } else if (event.code === 'ArrowLeft') {
      this.previousCard();
    } else if (this.isFlipped() && event.key >= '1' && event.key <= '5') {
      this.rateCard(Number(event.key));
    } else if (event.code === 'Escape') {
      this.exitStudyMode();
    }
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

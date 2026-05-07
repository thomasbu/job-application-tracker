import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { SkillService } from '../../../services/skill';
import { Skill, CreateSkillRequest } from '../../../models/interview-prep/skill';
import { Category, CATEGORY_LABELS } from '../../../models/interview-prep/enums';
import { ModalComponent } from '../../shared/modal/modal.component';

@Component({
  selector: 'app-skills',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, ModalComponent],
  templateUrl: './skills.html',
  styleUrl: './skills.scss',
})
export class SkillsComponent implements OnInit {
  skills = signal<Skill[]>([]);
  loading = signal(true);

  showForm = signal(false);
  formData = signal<CreateSkillRequest>({ name: '', category: Category.JAVA });

  categories = Object.values(Category);
  categoryLabels = CATEGORY_LABELS;

  constructor(private skillService: SkillService) {}

  ngOnInit(): void {
    this.loadSkills();
  }

  loadSkills(): void {
    this.loading.set(true);
    this.skillService.getAll().subscribe({
      next: (data) => {
        this.skills.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.showToast('Failed to load skills', 'error');
      },
    });
  }

  updateLevel(skill: Skill, level: number): void {
    this.skillService.updateLevel(skill.id, { level, notes: skill.notes }).subscribe({
      next: (updated) => {
        this.skills.update((skills) => skills.map((s) => (s.id === updated.id ? updated : s)));
      },
      error: () => this.showToast('Failed to update level', 'error'),
    });
  }

  openCreateForm(): void {
    this.formData.set({ name: '', category: Category.JAVA });
    this.showForm.set(true);
  }

  updateFormField(field: string, value: any): void {
    this.formData.update((d) => ({ ...d, [field]: value }));
  }

  closeForm(): void {
    this.showForm.set(false);
  }

  saveSkill(): void {
    this.skillService.create(this.formData()).subscribe({
      next: () => {
        this.showToast('Skill added', 'success');
        this.closeForm();
        this.loadSkills();
      },
      error: () => this.showToast('Failed to create skill', 'error'),
    });
  }

  deleteSkill(id: number): void {
    if (!confirm('Delete this skill?')) return;
    this.skillService.delete(id).subscribe({
      next: () => {
        this.showToast('Skill deleted', 'success');
        this.loadSkills();
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

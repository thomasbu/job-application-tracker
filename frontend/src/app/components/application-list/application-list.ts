import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Application } from '../../models/application';
import { ApplicationStatus } from '../../enums/application-status';
import { ApplicationService } from '../../services/application';
import { ApplicationDialogComponent } from '../application-dialog/application-dialog';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog';
import { StatusLabelPipe } from '../../pipes/status-label';
import { StatusColorPipe } from '../../pipes/status-color';

type SortColumn = 'company' | 'position' | 'applicationDate' | 'currentStatus';
type SortDirection = 'asc' | 'desc' | null;

@Component({
  selector: 'app-application-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DatePipe,
    StatusLabelPipe,
    StatusColorPipe,
    ApplicationDialogComponent,
    ConfirmDialogComponent,
  ],
  templateUrl: './application-list.html',
  styleUrl: './application-list.scss',
})
export class ApplicationListComponent implements OnInit {
  applications = signal<Application[]>([]);
  statusFilter = signal<ApplicationStatus | 'ALL'>('ALL');
  showDialog = signal(false);
  editingApplication = signal<Application | null>(null);

  // Confirmation dialog
  showConfirmDelete = signal(false);
  applicationToDelete = signal<number | null>(null);

  statuses = Object.values(ApplicationStatus);

  // Stats computed
  stats = computed(() => {
    const apps = this.applications();
    return {
      total: apps.length,
      sent: apps.filter((a) => a.currentStatus === ApplicationStatus.SENT).length,
      interview: apps.filter((a) => a.currentStatus === ApplicationStatus.INTERVIEW).length,
      rejected: apps.filter((a) => a.currentStatus === ApplicationStatus.REJECTED).length,
      accepted: apps.filter((a) => a.currentStatus === ApplicationStatus.ACCEPTED).length,
    };
  });

  // Sorting
  sortColumn = signal<SortColumn | null>(this.loadPreference('sortColumn') as SortColumn | null);
  sortDirection = signal<SortDirection>(this.loadPreference('sortDirection') as SortDirection);

  // Search
  searchTerm = signal('');

  // Pagination
  currentPage = signal(1);
  itemsPerPage = signal<number>(this.loadPreference('itemsPerPage', 10));

  // Data pipeline: applications → sorted → filtered → paginated
  sortedApplications = computed(() => {
    const apps = this.applications();
    const column = this.sortColumn();
    const direction = this.sortDirection();

    if (!column || !direction) {
      return apps;
    }

    return [...apps].sort((a, b) => {
      const valA = a[column] ?? '';
      const valB = b[column] ?? '';

      let comparison: number;
      if (column === 'applicationDate') {
        comparison = new Date(valA).getTime() - new Date(valB).getTime();
      } else {
        comparison = String(valA).localeCompare(String(valB), undefined, { sensitivity: 'base' });
      }

      return direction === 'asc' ? comparison : -comparison;
    });
  });

  filteredApplications = computed(() => {
    const apps = this.sortedApplications();
    const term = this.searchTerm().toLowerCase().trim();

    if (!term) {
      return apps;
    }

    return apps.filter(
      (app) =>
        app.company.toLowerCase().includes(term) ||
        app.position.toLowerCase().includes(term)
    );
  });

  totalPages = computed(() =>
    Math.max(1, Math.ceil(this.filteredApplications().length / this.itemsPerPage()))
  );

  paginatedApplications = computed(() => {
    const start = (this.currentPage() - 1) * this.itemsPerPage();
    return this.filteredApplications().slice(start, start + this.itemsPerPage());
  });

  constructor(private applicationService: ApplicationService) {}

  ngOnInit(): void {
    this.loadApplications();
  }

  loadApplications(): void {
    const filter = this.statusFilter();

    if (filter === 'ALL') {
      this.applicationService.getAllApplications().subscribe({
        next: (data) => this.applications.set(data),
        error: () => this.showToast('Failed to load applications', 'error'),
      });
    } else {
      this.applicationService.getApplicationsByStatus(filter).subscribe({
        next: (data) => this.applications.set(data),
        error: () => this.showToast('Failed to load applications', 'error'),
      });
    }
  }

  onFilterChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value as ApplicationStatus | 'ALL';
    this.statusFilter.set(value);
    this.currentPage.set(1);
    this.loadApplications();
  }

  // Sorting
  onSort(column: SortColumn): void {
    if (this.sortColumn() === column) {
      // Cycle: asc → desc → null
      if (this.sortDirection() === 'asc') {
        this.sortDirection.set('desc');
      } else if (this.sortDirection() === 'desc') {
        this.sortDirection.set(null);
        this.sortColumn.set(null);
      }
    } else {
      this.sortColumn.set(column);
      this.sortDirection.set('asc');
    }

    this.currentPage.set(1);
    this.savePreference('sortColumn', this.sortColumn());
    this.savePreference('sortDirection', this.sortDirection());
  }

  // Search
  onSearchInput(event: Event): void {
    const value = (event.target as HTMLInputElement).value;
    this.searchTerm.set(value);
    this.currentPage.set(1);
  }

  clearSearch(): void {
    this.searchTerm.set('');
    this.currentPage.set(1);
  }

  // Pagination
  nextPage(): void {
    if (this.currentPage() < this.totalPages()) {
      this.currentPage.update((p) => p + 1);
    }
  }

  previousPage(): void {
    if (this.currentPage() > 1) {
      this.currentPage.update((p) => p - 1);
    }
  }

  onPageSizeChange(event: Event): void {
    const value = Number((event.target as HTMLSelectElement).value);
    this.itemsPerPage.set(value);
    this.currentPage.set(1);
    this.savePreference('itemsPerPage', value);
  }

  // localStorage helpers
  private savePreference(key: string, value: unknown): void {
    try {
      localStorage.setItem(`app-tracker:${key}`, JSON.stringify(value));
    } catch {
      // silently ignore storage errors
    }
  }

  private loadPreference(key: string, fallback: any = null): any {
    try {
      const stored = localStorage.getItem(`app-tracker:${key}`);
      return stored !== null ? JSON.parse(stored) : fallback;
    } catch {
      return fallback;
    }
  }

  openCreateDialog(): void {
    this.editingApplication.set(null);
    this.showDialog.set(true);
  }

  openEditDialog(application: Application): void {
    this.editingApplication.set(application);
    this.showDialog.set(true);
  }

  closeDialog(refresh?: boolean): void {
    this.showDialog.set(false);
    this.editingApplication.set(null);
    if (refresh) {
      this.loadApplications();
    }
  }

  deleteApplication(id: number | undefined): void {
    if (!id) return;
    this.applicationToDelete.set(id);
    this.showConfirmDelete.set(true);
  }

  confirmDelete(): void {
    const id = this.applicationToDelete();
    if (!id) return;

    this.applicationService.deleteApplication(id).subscribe({
      next: () => {
        this.showToast('Application deleted successfully', 'success');
        this.loadApplications();
        this.cancelDelete();
      },
      error: () => {
        this.showToast('Failed to delete application', 'error');
        this.cancelDelete();
      },
    });
  }

  cancelDelete(): void {
    this.showConfirmDelete.set(false);
    this.applicationToDelete.set(null);
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

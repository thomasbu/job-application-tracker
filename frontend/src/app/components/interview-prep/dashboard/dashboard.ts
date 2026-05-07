import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { InterviewPrepStatsService } from '../../../services/interview-prep-stats';
import { InterviewPrepStats } from '../../../models/interview-prep/stats';

@Component({
  selector: 'app-interview-prep-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
})
export class InterviewPrepDashboardComponent implements OnInit {
  stats = signal<InterviewPrepStats | null>(null);
  loading = signal(true);

  constructor(private statsService: InterviewPrepStatsService) {}

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.statsService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      },
    });
  }
}

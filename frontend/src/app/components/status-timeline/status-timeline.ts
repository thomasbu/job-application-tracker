import { Component, Input } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { StatusHistory } from '../../models/status-history';
import { StatusLabelPipe } from '../../pipes/status-label';
import { StatusColorPipe } from '../../pipes/status-color';

@Component({
  selector: 'app-status-timeline',
  standalone: true,
  imports: [CommonModule, DatePipe, StatusLabelPipe, StatusColorPipe],
  templateUrl: './status-timeline.html',
  styleUrl: './status-timeline.scss',
})
export class StatusTimelineComponent {
  @Input() history: StatusHistory[] = [];
}

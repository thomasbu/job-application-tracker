import { Pipe, PipeTransform } from '@angular/core';
import { ApplicationStatus, APPLICATION_STATUS_LABELS } from '../enums/application-status';

@Pipe({
  name: 'statusLabel',
  standalone: true,
})
export class StatusLabelPipe implements PipeTransform {
  transform(value: any): string {
    // On cast explicitement ici pour rassurer le compilateur
    const status = value as ApplicationStatus;
    return APPLICATION_STATUS_LABELS[status] || value;
  }
}

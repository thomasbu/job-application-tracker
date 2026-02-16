import { Pipe, PipeTransform } from '@angular/core';
import { ApplicationStatus } from '../enums/application-status';

@Pipe({
  name: 'statusColor',
  standalone: true,
})
export class StatusColorPipe implements PipeTransform {
  transform(status: ApplicationStatus): string {
    const colors: { [key in ApplicationStatus]: string } = {
      [ApplicationStatus.SENT]: 'primary',
      [ApplicationStatus.INTERVIEW]: 'accent',
      [ApplicationStatus.REJECTED]: 'warn',
      [ApplicationStatus.ACCEPTED]: 'success',
    };
    return colors[status];
  }
}

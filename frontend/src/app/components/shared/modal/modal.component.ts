import { Component, EventEmitter, HostListener, Input, Output } from '@angular/core';

@Component({
  selector: 'app-modal',
  standalone: true,
  templateUrl: './modal.component.html',
  styleUrl: './modal.component.scss',
})
export class ModalComponent {
  @Input() title?: string;
  @Input() size: 'sm' | 'md' | 'lg' = 'md';
  @Input() zIndex = 1000;
  @Output() closeModal = new EventEmitter<void>();

  private isDragging = false;

  onContentMouseDown(): void {
    this.isDragging = true;
  }

  onContentMouseUp(): void {
    setTimeout(() => {
      this.isDragging = false;
    }, 50);
  }

  onBackdropClick(event: MouseEvent): void {
    if (event.target === event.currentTarget && !this.isDragging) {
      this.closeModal.emit();
    }
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    this.closeModal.emit();
  }
}

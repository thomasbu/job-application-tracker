import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { AuthService } from '../../../services/auth';
import { RegisterRequest } from '../../../models/auth/register-request';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class RegisterComponent {
  formData: RegisterRequest = { email: '', password: '', firstName: '', lastName: '' };
  confirmPassword = '';
  loading = signal(false);
  errorMessage = signal('');
  success = signal(false);
  showPassword = signal(false);

  constructor(private authService: AuthService) {}

  togglePassword(): void {
    this.showPassword.update((v) => !v);
  }

  onSubmit(): void {
    if (!this.formData.email || !this.formData.password || !this.formData.firstName || !this.formData.lastName) {
      this.errorMessage.set('Please fill in all fields');
      return;
    }
    if (this.formData.password.length < 8) {
      this.errorMessage.set('Password must be at least 8 characters');
      return;
    }
    if (this.formData.password !== this.confirmPassword) {
      this.errorMessage.set('Passwords do not match');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    this.authService.register(this.formData).subscribe({
      next: () => {
        this.loading.set(false);
        this.success.set(true);
      },
      error: (err) => {
        this.loading.set(false);
        const msg = err.error?.message || err.error || '';
        if (err.status === 409 || (typeof msg === 'string' && msg.toLowerCase().includes('already'))) {
          this.errorMessage.set('This email is already registered');
        } else {
          this.errorMessage.set('Registration failed. Please try again.');
        }
      },
    });
  }
}

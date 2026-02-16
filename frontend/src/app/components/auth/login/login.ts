import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, ActivatedRoute } from '@angular/router';

import { AuthService } from '../../../services/auth';
import { LoginRequest } from '../../../models/auth/login-request';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class LoginComponent {
  formData: LoginRequest = { email: '', password: '' };
  loading = signal(false);
  errorMessage = signal('');
  showPassword = signal(false);

  private returnUrl = '/applications';

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
  ) {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/applications';
  }

  togglePassword(): void {
    this.showPassword.update((v) => !v);
  }

  onSubmit(): void {
    if (!this.formData.email || !this.formData.password) {
      this.errorMessage.set('Please fill in all fields');
      return;
    }

    this.loading.set(true);
    this.errorMessage.set('');

    this.authService.login(this.formData).subscribe({
      next: () => {
        this.router.navigateByUrl(this.returnUrl);
      },
      error: (err) => {
        this.loading.set(false);
        const msg = err.error?.message || err.error || '';
        if (typeof msg === 'string' && msg.toLowerCase().includes('not enabled')) {
          this.errorMessage.set('Please confirm your email first');
        } else if (err.status === 400 || err.status === 401) {
          this.errorMessage.set('Email or password incorrect');
        } else {
          this.errorMessage.set('Login failed. Please try again.');
        }
      },
    });
  }
}

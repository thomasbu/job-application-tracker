import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';

import { AuthService } from '../../../services/auth';

@Component({
  selector: 'app-confirm-email',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './confirm-email.html',
  styleUrl: './confirm-email.scss',
})
export class ConfirmEmailComponent implements OnInit {
  loading = signal(true);
  success = signal(false);
  errorMessage = signal('');

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log('🔍 ConfirmEmail component loaded');
    console.log('🔍 Full URL:', window.location.href);
    console.log('🔍 Query params (snapshot):', this.route.snapshot.queryParams);
    console.log('🔍 Query param map:', this.route.snapshot.queryParamMap.keys);

    const token = this.route.snapshot.queryParams['token'];
    console.log('🔍 Token extracted:', token);
    console.log('🔍 Token type:', typeof token);
    console.log('🔍 Token length:', token?.length);

    if (!token) {
      console.log('❌ No token found!');
      this.loading.set(false);
      this.errorMessage.set('No confirmation token provided');
      return;
    }

    console.log('✅ Token found, calling confirmEmail API...');

    this.authService.confirmEmail(token).subscribe({
      next: (response) => {
        console.log('✅ API Success!');
        console.log('✅ Response:', response);
        this.loading.set(false);
        this.success.set(true);
        console.log('⏱️ Redirecting to /login in 3 seconds...');
        setTimeout(() => {
          console.log('🔄 Navigating to /login now');
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: (err) => {
        console.log('❌ API Error!');
        console.log('❌ Error details:', err);
        console.log('❌ Status:', err.status);
        console.log('❌ Message:', err.error?.message);
        this.loading.set(false);
        this.errorMessage.set('Invalid or expired confirmation token');
      },
    });
  }
}

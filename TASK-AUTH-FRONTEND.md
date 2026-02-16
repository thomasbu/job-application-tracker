\# TASK: Angular Authentication Frontend



\## Objective

Implement complete JWT authentication UI with login, register, email confirmation, password reset, and route guards. Integrate with existing Spring Boot backend.



\## Tech Stack

\- Angular 21 with standalone components

\- Signals for state management

\- Modern syntax (@if, @for)

\- Custom design system (no Angular Material)

\- TypeScript 5

\- RxJS for HTTP requests



\## Current Project Structure

```

frontend/src/app/

├── components/

│   ├── application-list/

│   ├── application-dialog/

│   ├── confirm-dialog/

│   └── status-timeline/

├── models/

│   ├── application.ts

│   ├── document.ts

│   └── status-history.ts

├── services/

│   ├── application.ts

│   └── document.ts

├── pipes/

│   ├── status-label.ts

│   └── status-color.ts

├── enums/

│   └── application-status.ts

└── app.component.ts (root)

```



\## Backend API Endpoints (already implemented)



\*\*Auth endpoints:\*\*

\- POST /api/auth/register → RegisterRequest → 201

\- GET /api/auth/confirm?token=xxx → 200

\- POST /api/auth/login → LoginRequest → AuthResponse (accessToken, refreshToken, user)

\- POST /api/auth/refresh → RefreshTokenRequest → new accessToken

\- POST /api/auth/forgot-password → ForgotPasswordRequest → 200

\- POST /api/auth/reset-password → ResetPasswordRequest → 200

\- POST /api/auth/logout → 200



\*\*Protected endpoints:\*\*

\- All /api/applications/\*\* require Authorization: Bearer {token}



\## What to Implement



\### 1. New Models



\*\*auth/user.ts\*\*

```typescript

export interface User {

&nbsp; id: number;

&nbsp; email: string;

&nbsp; firstName: string;

&nbsp; lastName: string;

&nbsp; role: 'USER' | 'ADMIN';

}

```



\*\*auth/auth-response.ts\*\*

```typescript

export interface AuthResponse {

&nbsp; accessToken: string;

&nbsp; refreshToken: string;

&nbsp; user: User;

}

```



\*\*auth/login-request.ts\*\*

```typescript

export interface LoginRequest {

&nbsp; email: string;

&nbsp; password: string;

}

```



\*\*auth/register-request.ts\*\*

```typescript

export interface RegisterRequest {

&nbsp; email: string;

&nbsp; password: string;

&nbsp; firstName: string;

&nbsp; lastName: string;

}

```



\### 2. Auth Service



\*\*services/auth.ts\*\*



Methods:

\- `register(request: RegisterRequest): Observable<any>`

\- `login(request: LoginRequest): Observable<AuthResponse>`

\- `logout(): Observable<any>`

\- `refreshToken(): Observable<{accessToken: string}>`

\- `forgotPassword(email: string): Observable<any>`

\- `resetPassword(token: string, newPassword: string): Observable<any>`

\- `isAuthenticated(): boolean` (check if token exists and valid)

\- `getCurrentUser(): User | null`



Uses TokenService internally.



\### 3. Token Service



\*\*services/token.ts\*\*



Manages JWT storage in localStorage:

\- `setTokens(accessToken: string, refreshToken: string): void`

\- `getAccessToken(): string | null`

\- `getRefreshToken(): string | null`

\- `removeTokens(): void`

\- `setUser(user: User): void`

\- `getUser(): User | null`

\- `isTokenExpired(token: string): boolean` (decode JWT, check exp)



Use `localStorage` for persistence:

\- Key: `'access\_token'`

\- Key: `'refresh\_token'`

\- Key: `'current\_user'`



\### 4. HTTP Interceptor



\*\*interceptors/auth.interceptor.ts\*\*



\- Intercept all HTTP requests

\- Add `Authorization: Bearer {token}` header if token exists

\- Skip for /api/auth/\*\* endpoints (except logout)

\- On 401 response → try refresh token

\- If refresh fails → redirect to /login



Functional interceptor (Angular 21 style):

```typescript

export const authInterceptor: HttpInterceptorFn = (req, next) => {

&nbsp; // Implementation

};

```



Register in `app.config.ts`:

```typescript

provideHttpClient(

&nbsp; withInterceptors(\[authInterceptor])

)

```



\### 5. Auth Guard



\*\*guards/auth.guard.ts\*\*



Functional guard:

```typescript

export const authGuard: CanActivateFn = (route, state) => {

&nbsp; const authService = inject(AuthService);

&nbsp; const router = inject(Router);

&nbsp; 

&nbsp; if (authService.isAuthenticated()) {

&nbsp;   return true;

&nbsp; }

&nbsp; 

&nbsp; router.navigate(\['/login'], { queryParams: { returnUrl: state.url } });

&nbsp; return false;

};

```



\### 6. Auth Components



Create folder: `components/auth/`



\*\*A. LoginComponent\*\*



Path: `/login`



Features:

\- Email + password inputs

\- "Remember me" checkbox (optional)

\- Login button

\- Link to "Forgot password?"

\- Link to "Sign up"

\- Show loading spinner during login

\- Display error messages (invalid credentials, account not enabled)

\- Redirect to /applications on success (or returnUrl from query params)



Design:

\- Centered card layout

\- App logo/title at top

\- Clean, minimal form

\- Consistent with existing design system



\*\*B. RegisterComponent\*\*



Path: `/register`



Features:

\- Email, password, firstName, lastName inputs

\- Password strength indicator (optional but nice)

\- "I agree to terms" checkbox

\- Register button

\- Link to "Already have an account? Login"

\- Show loading spinner

\- Display validation errors

\- After success → show message "Check your email to confirm"



Validation:

\- Email: @Email validator

\- Password: min 8 characters

\- All fields required



\*\*C. ConfirmEmailComponent\*\*



Path: `/auth/confirm`



Features:

\- Extracts token from query params

\- Auto-calls /api/auth/confirm on load

\- Shows loading state

\- On success: "Email confirmed! Redirecting to login..."

\- On error: "Invalid or expired token"

\- Auto-redirect to /login after 3 seconds on success



\*\*D. ForgotPasswordComponent\*\*



Path: `/auth/forgot-password`



Features:

\- Email input only

\- Submit button

\- After submit: "If email exists, reset link sent. Check your inbox."

\- Link back to login



\*\*E. ResetPasswordComponent\*\*



Path: `/auth/reset-password`



Features:

\- Extracts token from query params

\- New password input (+ confirm password)

\- Submit button

\- Show loading

\- On success: "Password reset! Redirecting to login..."

\- On error: "Invalid or expired token"



\*\*F. ProfileComponent\*\* (bonus, optional)



Path: `/profile`



Features:

\- Display user info (firstName, lastName, email)

\- Logout button

\- Edit profile (future)



\### 7. Update App Routes



\*\*app.routes.ts\*\*

```typescript

import { Routes } from '@angular/router';

import { authGuard } from './guards/auth.guard';



export const routes: Routes = \[

&nbsp; // Public routes

&nbsp; { path: 'login', component: LoginComponent },

&nbsp; { path: 'register', component: RegisterComponent },

&nbsp; { path: 'auth/confirm', component: ConfirmEmailComponent },

&nbsp; { path: 'auth/forgot-password', component: ForgotPasswordComponent },

&nbsp; { path: 'auth/reset-password', component: ResetPasswordComponent },

&nbsp; 

&nbsp; // Protected routes

&nbsp; { 

&nbsp;   path: 'applications', 

&nbsp;   component: ApplicationListComponent,

&nbsp;   canActivate: \[authGuard]

&nbsp; },

&nbsp; 

&nbsp; // Default redirects

&nbsp; { path: '', redirectTo: '/login', pathMatch: 'full' },

&nbsp; { path: '\*\*', redirectTo: '/login' }

];

```



\### 8. Update Existing Services



\*\*services/application.ts\*\*



No changes needed! The interceptor will add the Authorization header automatically.



\*\*services/document.ts\*\*



No changes needed! Same - interceptor handles auth.



\### 9. Update App Component



\*\*app.component.ts\*\*



Add navigation bar if user is authenticated:

\- Show user name

\- Logout button

\- Only visible on /applications route



Or keep it simple and add logout button in ApplicationListComponent header.



\### 10. Design System Consistency



Use existing CSS variables from `styles.scss`:

\- `--primary`, `--gray-xxx`, etc.

\- `.btn`, `.btn-primary`, `.btn-secondary`

\- `.input`, `.label`

\- Card styling with shadows

\- Spacing with `--spacing-xxx`



Auth pages specific styles:

\- Centered layout (max-width 400px)

\- White card with shadow on light gray background

\- Logo/title at top

\- Form fields stacked vertically

\- Full-width buttons

\- Links for navigation (blue, underlined on hover)



\### 11. Error Handling



Display user-friendly errors:



Backend error → Frontend message:

\- 400 "Invalid credentials" → "Email or password incorrect"

\- 403 "User not enabled" → "Please confirm your email first"

\- 409 "Email already exists" → "This email is already registered"

\- 401 on refresh fail → Auto-logout + redirect to login



Toast notifications for:

\- Registration success

\- Password reset email sent

\- Email confirmed

\- Logout



Use existing toast system (see application-list component).



\### 12. Loading States



All forms should show loading:

\- Disable inputs + button

\- Show spinner in button

\- Prevent double-submit



\### 13. Password Visibility Toggle



Add eye icon to password fields:

\- Default: type="password"

\- Click eye → type="text"

\- Toggle icon between eye and eye-slash



\### 14. Responsive Design



Auth pages must work on mobile:

\- Card width: 100% on mobile, max 400px on desktop

\- Padding adjustments

\- Stack elements vertically

\- Touch-friendly button sizes



\### 15. Auto-logout on Token Expiry



If access token expires and refresh fails:

\- Clear tokens from localStorage

\- Redirect to /login

\- Show toast: "Session expired. Please login again."



\### 16. Return URL After Login



If user tries to access /applications without auth:

\- Redirect to /login?returnUrl=/applications

\- After successful login → redirect to returnUrl

\- Default to /applications if no returnUrl



\## Implementation Order



\*\*Step 1:\*\* Models + Interfaces

\*\*Step 2:\*\* TokenService (no dependencies)

\*\*Step 3:\*\* AuthService (uses TokenService)

\*\*Step 4:\*\* AuthInterceptor (uses TokenService)

\*\*Step 5:\*\* AuthGuard (uses AuthService)

\*\*Step 6:\*\* LoginComponent (test first)

\*\*Step 7:\*\* RegisterComponent

\*\*Step 8:\*\* ConfirmEmailComponent

\*\*Step 9:\*\* ForgotPasswordComponent

\*\*Step 10:\*\* ResetPasswordComponent

\*\*Step 11:\*\* Update routes with guard

\*\*Step 12:\*\* Update app component (nav/logout)

\*\*Step 13:\*\* Polish UI + error handling

\*\*Step 14:\*\* Test end-to-end flow



\## Testing Checklist



After implementation:

\- \[ ] Register new user → email received (check Gmail)

\- \[ ] Confirm email via link

\- \[ ] Login → redirected to /applications

\- \[ ] Access /applications without login → redirected to /login

\- \[ ] Logout → redirected to /login, tokens cleared

\- \[ ] Login → create application → works

\- \[ ] Refresh page → still authenticated (tokens in localStorage)

\- \[ ] Token expiry → auto-refresh works

\- \[ ] Forgot password → email received

\- \[ ] Reset password → works

\- \[ ] Password visibility toggle

\- \[ ] Validation errors display correctly

\- \[ ] Loading states work

\- \[ ] Mobile responsive



\## Important Notes



\- Do NOT use Angular Material

\- Follow existing component structure (standalone, signals)

\- Use existing design tokens from styles.scss

\- No @Component suffix in filenames (login.ts not login.component.ts)

\- Constructor injection for services

\- Functional guards and interceptors (Angular 21 style)

\- localStorage for token persistence (consider httpOnly cookies for production)



\## API Base URL



Configure in environment or hard-code:

```typescript

private apiUrl = 'http://localhost:8080/api/auth';

```



\## JWT Decoding (optional)



For `isTokenExpired()`, you can:

\- Install `jwt-decode` package: `npm install jwt-decode`

\- Or manually decode: `JSON.parse(atob(token.split('.')\[1]))`



\## Constraints



\- Maintain all existing application/document functionality

\- Auth is additive - don't break existing features

\- User-friendly error messages

\- Consistent design language

\- Mobile-first responsive

\- Accessibility (labels, aria-attributes)



\## Files to Create (~20 files)

```

src/app/

├── models/

│   └── auth/

│       ├── user.ts

│       ├── auth-response.ts

│       ├── login-request.ts

│       └── register-request.ts

├── services/

│   ├── auth.ts

│   └── token.ts

├── guards/

│   └── auth.guard.ts

├── interceptors/

│   └── auth.interceptor.ts

└── components/

&nbsp;   └── auth/

&nbsp;       ├── login/

&nbsp;       │   ├── login.ts

&nbsp;       │   ├── login.html

&nbsp;       │   └── login.scss

&nbsp;       ├── register/

&nbsp;       │   ├── register.ts

&nbsp;       │   ├── register.html

&nbsp;       │   └── register.scss

&nbsp;       ├── confirm-email/

&nbsp;       │   ├── confirm-email.ts

&nbsp;       │   ├── confirm-email.html

&nbsp;       │   └── confirm-email.scss

&nbsp;       ├── forgot-password/

&nbsp;       │   ├── forgot-password.ts

&nbsp;       │   ├── forgot-password.html

&nbsp;       │   └── forgot-password.scss

&nbsp;       └── reset-password/

&nbsp;           ├── reset-password.ts

&nbsp;           ├── reset-password.html

&nbsp;           └── reset-password.scss

```



\## Files to Modify



\- `app.routes.ts` (add auth routes + guard)

\- `app.config.ts` (register interceptor)

\- `app.component.ts` (optional: add nav/logout)

\- `styles.scss` (optional: add auth-specific utilities)


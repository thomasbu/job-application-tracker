import { Routes } from '@angular/router';
import { authGuard } from './guards/auth.guard';

import { LoginComponent } from './components/auth/login/login';
import { RegisterComponent } from './components/auth/register/register';
import { ConfirmEmailComponent } from './components/auth/confirm-email/confirm-email';
import { ForgotPasswordComponent } from './components/auth/forgot-password/forgot-password';
import { ResetPasswordComponent } from './components/auth/reset-password/reset-password';
import { ApplicationListComponent } from './components/application-list/application-list';
import { InterviewPrepDashboardComponent } from './components/interview-prep/dashboard/dashboard';
import { FlashcardsComponent } from './components/interview-prep/flashcards/flashcards';
import { CodingChallengesComponent } from './components/interview-prep/coding-challenges/coding-challenges';
import { TechnicalQuestionsComponent } from './components/interview-prep/technical-questions/technical-questions';
import { SkillsComponent } from './components/interview-prep/skills/skills';
import { StudySessionsComponent } from './components/interview-prep/study-sessions/study-sessions';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'auth/confirm', component: ConfirmEmailComponent },
  { path: 'auth/forgot-password', component: ForgotPasswordComponent },
  { path: 'auth/reset-password', component: ResetPasswordComponent },
  {
    path: 'applications',
    component: ApplicationListComponent,
    canActivate: [authGuard],
  },
  {
    path: 'interview-prep',
    component: InterviewPrepDashboardComponent,
    canActivate: [authGuard],
  },
  {
    path: 'interview-prep/flashcards',
    component: FlashcardsComponent,
    canActivate: [authGuard],
  },
  {
    path: 'interview-prep/coding-challenges',
    component: CodingChallengesComponent,
    canActivate: [authGuard],
  },
  {
    path: 'interview-prep/technical-questions',
    component: TechnicalQuestionsComponent,
    canActivate: [authGuard],
  },
  {
    path: 'interview-prep/skills',
    component: SkillsComponent,
    canActivate: [authGuard],
  },
  {
    path: 'interview-prep/study-sessions',
    component: StudySessionsComponent,
    canActivate: [authGuard],
  },
  { path: '', redirectTo: '/applications', pathMatch: 'full' },
  { path: '**', redirectTo: '/applications' },
];

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, MatIconModule],
  template: `
    <div class="min-h-screen bg-slate-50 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
      <div class="sm:mx-auto sm:w-full sm:max-w-md">
        <div class="flex justify-center">
          <a routerLink="/" class="flex items-center gap-2 text-2xl font-bold tracking-tight text-slate-900 hover:text-blue-600 transition-colors">
            <mat-icon class="text-blue-600 text-3xl h-8 w-8">memory</mat-icon>
            SiliconAtlas
          </a>
        </div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-slate-900 tracking-tight">Zaloguj sie do konta</h2>
      </div>

      <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div class="bg-white py-8 px-4 shadow-xl shadow-slate-200/50 sm:rounded-2xl sm:px-10 border border-slate-100">
          <form class="space-y-6" (ngSubmit)="onSubmit()">
            <div>
              <label for="username" class="block text-sm font-medium text-slate-700">Nazwa uzytkownika</label>
              <div class="mt-1 relative">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <mat-icon class="text-slate-400 text-sm">person</mat-icon>
                </div>
                <input id="username" name="username" type="text" [(ngModel)]="username" required class="appearance-none block w-full pl-10 px-3 py-2 border border-slate-300 rounded-lg shadow-sm placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm transition-colors" placeholder="twoj_login">
              </div>
            </div>

            <div>
              <label for="password" class="block text-sm font-medium text-slate-700">Haslo</label>
              <div class="mt-1 relative">
                <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <mat-icon class="text-slate-400 text-sm">lock</mat-icon>
                </div>
                <input id="password" name="password" type="password" [(ngModel)]="password" required class="appearance-none block w-full pl-10 px-3 py-2 border border-slate-300 rounded-lg shadow-sm placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 sm:text-sm transition-colors" placeholder="••••••••">
              </div>
            </div>

            @if (errorMessage) {
              <div class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{{ errorMessage }}</div>
            }

            <div>
              <button type="submit" [disabled]="loading" class="w-full flex justify-center py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all">
                {{ loading ? 'Logowanie...' : 'Zaloguj sie' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  `
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/app']);
    }
  }

  onSubmit(): void {
    if (!this.username || !this.password) {
      this.errorMessage = 'Wypełnij wszystkie pola';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.login({ username: this.username, password: this.password }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/app']);
      },
      error: (err) => {
        this.loading = false;
        this.errorMessage = err.status === 401
          ? 'Nieprawidłowa nazwa użytkownika lub hasło'
          : 'Wystąpił błąd podczas logowania';
      }
    });
  }
}

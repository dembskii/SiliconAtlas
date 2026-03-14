import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
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
        <h2 class="mt-6 text-center text-3xl font-extrabold text-slate-900 tracking-tight">Utworz nowe konto</h2>
      </div>

      <div class="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div class="bg-white py-8 px-4 shadow-xl shadow-slate-200/50 sm:rounded-2xl sm:px-10 border border-slate-100">
          <form class="space-y-6" (ngSubmit)="onSubmit()">
            <div>
              <label class="block text-sm font-medium text-slate-700">Nazwa uzytkownika</label>
              <input type="text" name="username" [(ngModel)]="username" required class="mt-1 w-full px-3 py-2 border border-slate-300 rounded-lg" placeholder="jan_kowalski">
            </div>

            <div>
              <label class="block text-sm font-medium text-slate-700">Adres e-mail</label>
              <input type="email" name="email" [(ngModel)]="email" required class="mt-1 w-full px-3 py-2 border border-slate-300 rounded-lg" placeholder="twoj@email.com">
            </div>

            <div>
              <label class="block text-sm font-medium text-slate-700">Haslo</label>
              <input type="password" name="password" [(ngModel)]="password" required class="mt-1 w-full px-3 py-2 border border-slate-300 rounded-lg" placeholder="••••••••">
            </div>

            <div>
              <label class="block text-sm font-medium text-slate-700">Potwierdz haslo</label>
              <input type="password" name="confirmPassword" [(ngModel)]="confirmPassword" required class="mt-1 w-full px-3 py-2 border border-slate-300 rounded-lg" placeholder="••••••••">
            </div>

            @if (errorMessage) {
              <div class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{{ errorMessage }}</div>
            }

            <button type="submit" [disabled]="loading" class="w-full py-2.5 px-4 rounded-lg text-white bg-blue-600 hover:bg-blue-700 disabled:opacity-50">
              {{ loading ? 'Rejestracja...' : 'Zarejestruj sie' }}
            </button>

            <p class="text-center text-sm text-slate-600">
              Masz juz konto?
              <a routerLink="/login" class="font-medium text-blue-600 hover:text-blue-700">Zaloguj sie</a>
            </p>
          </form>
        </div>
      </div>
    </div>
  `
})
export class RegisterComponent {
  username = '';
  email = '';
  password = '';
  confirmPassword = '';
  errorMessage = '';
  loading = false;

  constructor(private authService: AuthService, private router: Router) {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/app']);
    }
  }

  onSubmit(): void {
    if (!this.username || !this.email || !this.password || !this.confirmPassword) {
      this.errorMessage = 'Wypełnij wszystkie pola';
      return;
    }

    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Hasła nie są identyczne';
      return;
    }

    if (this.password.length < 6) {
      this.errorMessage = 'Hasło musi mieć co najmniej 6 znaków';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.authService.register({
      username: this.username,
      email: this.email,
      password: this.password,
      confirmPassword: this.confirmPassword
    }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/app']);
      },
      error: (err) => {
        this.loading = false;
        if (err.status === 409) {
          this.errorMessage = 'Użytkownik o podanej nazwie lub emailu już istnieje';
        } else {
          this.errorMessage = 'Wystąpił błąd podczas rejestracji';
        }
      }
    });
  }
}

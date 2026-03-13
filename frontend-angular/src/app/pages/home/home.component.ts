import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [RouterLink, MatIconModule],
  template: `
    <div class="min-h-screen bg-slate-50 flex flex-col">
      <header class="bg-white shadow-sm sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-between h-16 items-center">
            <div class="flex items-center gap-2">
              <mat-icon class="text-blue-600">memory</mat-icon>
              <span class="text-xl font-bold tracking-tight text-slate-900">SiliconAtlas</span>
            </div>
            <div class="flex items-center gap-4">
              <a routerLink="/login" class="text-sm font-medium text-slate-600 hover:text-slate-900 transition-colors">Zaloguj sie</a>
              <a routerLink="/register" class="bg-blue-600 text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-blue-700 transition-colors shadow-sm">Zarejestruj sie</a>
            </div>
          </div>
        </div>
      </header>

      <main class="flex-1 flex flex-col items-center justify-center text-center px-4 sm:px-6 lg:px-8 py-20">
        <div class="max-w-3xl space-y-8">
          <h1 class="text-5xl sm:text-6xl font-extrabold text-slate-900 tracking-tight leading-tight">
            Baza wiedzy o <span class="text-blue-600">procesorach</span>
          </h1>
          <p class="text-xl text-slate-600 max-w-2xl mx-auto leading-relaxed">
            Odkrywaj, porownuj i zarzadzaj informacjami o procesorach, producentach i technologiach w jednym miejscu.
          </p>
          <div class="flex flex-col sm:flex-row gap-4 justify-center pt-4">
            <a routerLink="/register" class="bg-blue-600 text-white px-8 py-4 rounded-xl text-lg font-semibold hover:bg-blue-700 transition-all shadow-md hover:shadow-lg flex items-center justify-center gap-2">
              Rozpocznij teraz
              <mat-icon>arrow_forward</mat-icon>
            </a>
            <a routerLink="/app" class="bg-white text-slate-700 border border-slate-200 px-8 py-4 rounded-xl text-lg font-semibold hover:bg-slate-50 hover:border-slate-300 transition-all shadow-sm flex items-center justify-center gap-2">
              Przejdz do aplikacji
            </a>
          </div>
        </div>
      </main>
    </div>
  `
})
export class HomeComponent {}

import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { AuthService } from '../../services/auth.service';
import { LogConsoleComponent } from '../log-console/log-console.component';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, MatIconModule, LogConsoleComponent],
  template: `
    <div class="min-h-screen flex flex-col bg-slate-50">
      <header class="bg-slate-900 text-white shadow-md sticky top-0 z-50">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div class="flex justify-between h-16 items-center">
            <div class="flex items-center gap-8">
              <a routerLink="/" class="flex items-center gap-2 text-xl font-bold tracking-tight text-white hover:text-blue-400 transition-colors">
                <mat-icon class="text-blue-500">memory</mat-icon>
                SiliconAtlas
              </a>
              <nav class="hidden md:flex space-x-1">
                <a routerLink="/app" routerLinkActive="bg-slate-800 text-white" [routerLinkActiveOptions]="{ exact: true }" class="px-3 py-2 rounded-md text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white transition-colors">Dashboard</a>
                <a routerLink="/app/cpu" routerLinkActive="bg-slate-800 text-white" class="px-3 py-2 rounded-md text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white transition-colors">CPU</a>
                <a routerLink="/app/manufacturers" routerLinkActive="bg-slate-800 text-white" class="px-3 py-2 rounded-md text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white transition-colors">Producenci</a>
                <a routerLink="/app/technologies" routerLinkActive="bg-slate-800 text-white" class="px-3 py-2 rounded-md text-sm font-medium text-slate-300 hover:bg-slate-800 hover:text-white transition-colors">Technologie</a>
              </nav>
            </div>
            <div class="flex items-center gap-4">
              <button type="button" (click)="logout()" class="text-sm font-medium text-slate-300 hover:text-white flex items-center gap-1 transition-colors">
                <mat-icon class="text-sm">logout</mat-icon>
                Wyloguj się
              </button>
            </div>
          </div>
        </div>
      </header>

      <main class="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8 pb-96">
        <router-outlet></router-outlet>
      </main>

      <app-log-console></app-log-console>
    </div>
  `
})
export class LayoutComponent {
  constructor(private authService: AuthService) {}

  logout(): void {
    this.authService.logout();
  }
}

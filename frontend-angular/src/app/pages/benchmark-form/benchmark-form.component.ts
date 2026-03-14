import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-benchmark-form',
  standalone: true,
  imports: [RouterLink, MatIconModule],
  template: `
    <div class="max-w-2xl mx-auto space-y-6">
      <div class="flex items-center justify-between">
        <h1 class="text-2xl font-bold text-slate-900 tracking-tight">Dodaj Benchmark</h1>
        <a routerLink="/app/cpu" class="inline-flex items-center gap-2 text-sm font-medium text-slate-500 hover:text-slate-800 transition-colors">
          <mat-icon class="text-sm">arrow_back</mat-icon>
          Powrot
        </a>
      </div>

      <div class="bg-amber-50 border border-amber-200 rounded-xl p-4 text-amber-800 text-sm">
        Dodawanie benchmarkow nie jest jeszcze podlaczone do backendu w frontend-angular.
      </div>
    </div>
  `
})
export class BenchmarkFormComponent {}

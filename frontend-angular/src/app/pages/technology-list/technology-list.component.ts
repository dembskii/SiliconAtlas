import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { TechnologyService } from '../../services/technology.service';
import { Technology } from '../../models/technology.model';

@Component({
  selector: 'app-technology-list',
  standalone: true,
  imports: [CommonModule, RouterLink, MatIconModule],
  template: `
    <div class="space-y-6">
      <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <h1 class="text-2xl font-bold text-slate-900 tracking-tight">Lista technologii</h1>
        <a routerLink="/app/technologies/add" class="inline-flex items-center gap-2 px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-sm">
          <mat-icon class="text-sm">add</mat-icon>
          Dodaj technologie
        </a>
      </div>

      @if (successMessage) {
        <div class="rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700">{{ successMessage }}</div>
      }
      @if (errorMessage) {
        <div class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{{ errorMessage }}</div>
      }

      <div class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full text-sm text-left">
            <thead class="text-xs text-slate-500 uppercase bg-slate-50 border-b border-slate-200">
              <tr>
                <th class="px-6 py-4 font-semibold">Nazwa</th>
                <th class="px-6 py-4 font-semibold">Opis</th>
                <th class="px-6 py-4 font-semibold">Rok</th>
                <th class="px-6 py-4 font-semibold text-right">Akcje</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100">
              @for (t of technologies; track t.id) {
                <tr class="hover:bg-slate-50 transition-colors group">
                  <td class="px-6 py-4 font-semibold text-slate-900">{{ t.name }}</td>
                  <td class="px-6 py-4 text-slate-700">{{ t.description }}</td>
                  <td class="px-6 py-4 text-slate-700">{{ t.releaseYear }}</td>
                  <td class="px-6 py-4 text-right flex justify-end gap-2">
                    <a [routerLink]="['/app/technologies', t.id, 'edit']" class="inline-flex items-center gap-1 px-3 py-1.5 bg-blue-50 text-blue-700 text-xs font-medium rounded-md hover:bg-blue-100 transition-colors opacity-0 group-hover:opacity-100">
                      <mat-icon class="text-[16px] w-[16px] h-[16px]">edit</mat-icon>
                      Edytuj
                    </a>
                    <button (click)="deleteTechnology(t.id, t.name)" class="inline-flex items-center gap-1 px-3 py-1.5 bg-red-50 text-red-700 text-xs font-medium rounded-md hover:bg-red-100 transition-colors opacity-0 group-hover:opacity-100">
                      <mat-icon class="text-[16px] w-[16px] h-[16px]">delete</mat-icon>
                      Usun
                    </button>
                  </td>
                </tr>
              } @empty {
                <tr>
                  <td colspan="4" class="px-6 py-8 text-center text-slate-500 italic">Brak technologii</td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `
})
export class TechnologyListComponent implements OnInit {
  technologies: Technology[] = [];
  loading = true;
  successMessage = '';
  errorMessage = '';

  constructor(private technologyService: TechnologyService) {}

  ngOnInit(): void {
    this.loadTechnologies();
  }

  loadTechnologies(): void {
    this.loading = true;
    this.technologyService.getAll().subscribe({
      next: t => {
        this.technologies = t;
        this.loading = false;
      },
      error: () => {
        this.errorMessage = 'Nie udało się załadować technologii';
        this.loading = false;
      }
    });
  }

  deleteTechnology(id: string, name: string): void {
    if (confirm(`Czy na pewno chcesz usunąć technologię "${name}"?`)) {
      this.technologyService.delete(id).subscribe({
        next: () => {
          this.successMessage = `Usunięto technologię "${name}"`;
          this.loadTechnologies();
          setTimeout(() => this.successMessage = '', 4000);
        },
        error: () => {
          this.errorMessage = 'Nie udało się usunąć technologii';
          setTimeout(() => this.errorMessage = '', 4000);
        }
      });
    }
  }

}

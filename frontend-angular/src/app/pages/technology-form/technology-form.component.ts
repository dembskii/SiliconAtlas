import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { TechnologyService } from '../../services/technology.service';

@Component({
  selector: 'app-technology-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, MatIconModule],
  template: `
    <div class="max-w-2xl mx-auto space-y-6">
      <div class="flex items-center justify-between">
        <h1 class="text-2xl font-bold text-slate-900 tracking-tight">{{ isEditMode ? 'Edytuj technologie' : 'Dodaj technologie' }}</h1>
        <a routerLink="/app/technologies" class="inline-flex items-center gap-2 text-sm font-medium text-slate-500 hover:text-slate-800 transition-colors">
          <mat-icon class="text-sm">arrow_back</mat-icon>
          Powrot
        </a>
      </div>

      @if (errorMessage) {
        <div class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{{ errorMessage }}</div>
      }

      <form (ngSubmit)="onSubmit()" class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 space-y-6">
        <div>
          <label for="name" class="block text-sm font-medium text-slate-700 mb-1">Nazwa technologii</label>
          <input type="text" id="name" [(ngModel)]="name" name="name" required minlength="2" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-shadow" placeholder="np. AVX-512">
        </div>

        <div>
          <label for="description" class="block text-sm font-medium text-slate-700 mb-1">Opis</label>
          <textarea id="description" [(ngModel)]="description" name="description" rows="4" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-shadow" placeholder="Krotki opis technologii..."></textarea>
        </div>

        <div>
          <label for="releaseYear" class="block text-sm font-medium text-slate-700 mb-1">Rok wydania</label>
          <input type="number" id="releaseYear" [(ngModel)]="releaseYear" name="releaseYear" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-shadow" placeholder="np. 2022">
        </div>

        <div class="pt-4 border-t border-slate-100 flex justify-end gap-3">
          <a routerLink="/app/technologies" class="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50 transition-colors shadow-sm">Anuluj</a>
          <button type="submit" [disabled]="loading || !name" class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors shadow-sm disabled:opacity-50 disabled:cursor-not-allowed inline-flex items-center gap-2">
            <mat-icon class="text-sm">save</mat-icon>
            Zapisz
          </button>
        </div>
      </form>
    </div>
  `
})
export class TechnologyFormComponent implements OnInit {
  isEditMode = false;
  technologyId: string | null = null;
  loading = false;
  errorMessage = '';

  name = '';
  description = '';
  releaseYear: number | null = null;

  constructor(
    private technologyService: TechnologyService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.technologyId = id;
      this.technologyService.getById(this.technologyId).subscribe({
        next: (technology) => {
          this.name = technology.name;
          this.description = technology.description;
          this.releaseYear = technology.releaseYear;
        },
        error: () => {
          this.errorMessage = 'Nie udalo sie zaladowac technologii';
        }
      });
    }
  }

  onSubmit(): void {
    if (!this.name) {
      this.errorMessage = 'Nazwa technologii jest wymagana';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const payload = {
      name: this.name,
      description: this.description,
      releaseYear: this.releaseYear ?? 0
    };

    const request$ = this.isEditMode && this.technologyId
      ? this.technologyService.update(this.technologyId, payload)
      : this.technologyService.create(payload);

    request$.subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/app/technologies']);
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Nie udalo sie zapisac technologii';
      }
    });
  }
}

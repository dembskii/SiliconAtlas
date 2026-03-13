import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { ManufacturerService } from '../../services/manufacturer.service';

@Component({
  selector: 'app-manufacturer-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, MatIconModule],
  template: `
    <div class="max-w-2xl mx-auto space-y-6">
      <div class="flex items-center justify-between">
        <h1 class="text-2xl font-bold text-slate-900 tracking-tight">{{ isEditMode ? 'Edytuj producenta' : 'Dodaj producenta' }}</h1>
        <a routerLink="/app/manufacturers" class="inline-flex items-center gap-2 text-sm font-medium text-slate-500 hover:text-slate-800 transition-colors">
          <mat-icon class="text-sm">arrow_back</mat-icon>
          Powrot
        </a>
      </div>

      @if (errorMessage) {
        <div class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{{ errorMessage }}</div>
      }

      <form (ngSubmit)="onSubmit()" class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 space-y-6">
        <div>
          <label for="name" class="block text-sm font-medium text-slate-700 mb-1">Nazwa producenta</label>
          <input type="text" id="name" [(ngModel)]="name" name="name" required minlength="2" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-shadow" placeholder="np. Intel, AMD, Apple">
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
          <div>
            <label for="country" class="block text-sm font-medium text-slate-700 mb-1">Kraj</label>
            <input type="text" id="country" [(ngModel)]="country" name="country" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-shadow" placeholder="np. USA, Taiwan">
          </div>

          <div>
            <label for="foundedYear" class="block text-sm font-medium text-slate-700 mb-1">Rok zalozenia</label>
            <input type="number" id="foundedYear" [(ngModel)]="foundedYear" name="foundedYear" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-shadow" placeholder="np. 1968">
          </div>
        </div>

        <div class="pt-4 border-t border-slate-100 flex justify-end gap-3">
          <a routerLink="/app/manufacturers" class="px-4 py-2 text-sm font-medium text-slate-700 bg-white border border-slate-300 rounded-lg hover:bg-slate-50 transition-colors shadow-sm">Anuluj</a>
          <button type="submit" [disabled]="loading || !name" class="px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 transition-colors shadow-sm disabled:opacity-50 disabled:cursor-not-allowed inline-flex items-center gap-2">
            <mat-icon class="text-sm">save</mat-icon>
            Zapisz
          </button>
        </div>
      </form>
    </div>
  `
})
export class ManufacturerFormComponent implements OnInit {
  isEditMode = false;
  manufacturerId: string | null = null;
  loading = false;
  errorMessage = '';

  name = '';
  country = '';
  foundedYear: number | null = null;

  constructor(
    private manufacturerService: ManufacturerService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.manufacturerId = id;
      this.manufacturerService.getById(this.manufacturerId).subscribe({
        next: (manufacturer) => {
          this.name = manufacturer.name;
          this.country = manufacturer.country;
          this.foundedYear = manufacturer.foundedYear;
        },
        error: () => {
          this.errorMessage = 'Nie udalo sie zaladowac producenta';
        }
      });
    }
  }

  onSubmit(): void {
    if (!this.name) {
      this.errorMessage = 'Nazwa producenta jest wymagana';
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    const payload = {
      name: this.name,
      country: this.country,
      foundedYear: this.foundedYear ?? 0
    };

    const request$ = this.isEditMode && this.manufacturerId
      ? this.manufacturerService.update(this.manufacturerId, payload)
      : this.manufacturerService.create(payload);

    request$.subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/app/manufacturers']);
      },
      error: () => {
        this.loading = false;
        this.errorMessage = 'Nie udalo sie zapisac producenta';
      }
    });
  }
}

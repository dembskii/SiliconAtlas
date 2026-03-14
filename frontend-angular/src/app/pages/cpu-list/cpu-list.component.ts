import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { CpuService } from '../../services/cpu.service';
import { ManufacturerService } from '../../services/manufacturer.service';
import { TechnologyService } from '../../services/technology.service';
import { Cpu, CpuSearchCriteria, PagedResponse } from '../../models/cpu.model';
import { Manufacturer } from '../../models/manufacturer.model';
import { Technology } from '../../models/technology.model';

@Component({
  selector: 'app-cpu-list',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, MatIconModule],
  template: `
    <div class="space-y-6">
      <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <h1 class="text-2xl font-bold text-slate-900 tracking-tight">Lista procesorow</h1>
        <a routerLink="/app/cpu/add" class="inline-flex items-center gap-2 px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-sm">
          <mat-icon class="text-sm">add</mat-icon>
          Dodaj CPU
        </a>
      </div>

      @if (successMessage) {
        <div class="rounded-lg border border-emerald-200 bg-emerald-50 px-4 py-3 text-sm text-emerald-700">{{ successMessage }}</div>
      }
      @if (errorMessage) {
        <div class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{{ errorMessage }}</div>
      }

      <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
        <div class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
          <div>
            <label class="block text-xs font-medium text-slate-500 mb-1 uppercase tracking-wider">Model</label>
            <input type="text" [(ngModel)]="criteria.model" class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-500 mb-1 uppercase tracking-wider">Producent</label>
            <input type="text" [(ngModel)]="criteria.manufacturer" class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-500 mb-1 uppercase tracking-wider">Technologia</label>
            <input type="text" [(ngModel)]="criteria.technology" class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
          </div>
        </div>

        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-4 mb-6">
          <div><input type="number" [(ngModel)]="criteria.minCores" placeholder="Min rdzeni" class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm"></div>
          <div><input type="number" [(ngModel)]="criteria.maxCores" placeholder="Max rdzeni" class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm"></div>
          <div><input type="number" [(ngModel)]="criteria.minThreads" placeholder="Min watkow" class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm"></div>
          <div><input type="number" [(ngModel)]="criteria.maxThreads" placeholder="Max watkow" class="w-full px-3 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm"></div>
        </div>

        <div class="flex gap-3">
          <button (click)="onSearch()" class="px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition-colors">Szukaj</button>
          <button (click)="clearFilters()" class="px-4 py-2 bg-slate-100 text-slate-700 text-sm font-medium rounded-lg hover:bg-slate-200 transition-colors">Wyczysc filtry</button>
        </div>
      </div>

      <div class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full text-sm text-left">
            <thead class="text-xs text-slate-500 uppercase bg-slate-50 border-b border-slate-200">
              <tr>
                <th class="px-6 py-4 font-semibold">Model</th>
                <th class="px-6 py-4 font-semibold">Producent</th>
                <th class="px-6 py-4 font-semibold text-right">Rdzenie</th>
                <th class="px-6 py-4 font-semibold text-right">Watki</th>
                <th class="px-6 py-4 font-semibold text-right">Czestotliwosc</th>
                <th class="px-6 py-4 font-semibold">Technologie</th>
                <th class="px-6 py-4 font-semibold text-right">Akcje</th>
              </tr>
            </thead>
            <tbody class="divide-y divide-slate-100">
              @for (cpu of cpus; track cpu.id) {
                <tr class="hover:bg-slate-50 transition-colors group">
                  <td class="px-6 py-4 font-semibold text-slate-900">{{ cpu.model }}</td>
                  <td class="px-6 py-4 text-slate-600">{{ cpu.manufacturerName }}</td>
                  <td class="px-6 py-4 text-right text-slate-600 font-mono">{{ cpu.cores }}</td>
                  <td class="px-6 py-4 text-right text-slate-600 font-mono">{{ cpu.threads }}</td>
                  <td class="px-6 py-4 text-right text-slate-600 font-mono">{{ cpu.frequencyGhz }} GHz</td>
                  <td class="px-6 py-4">
                    <div class="flex flex-wrap gap-1.5">
                      @for (techName of cpu.technologyNames; track techName) {
                        <span class="inline-flex items-center px-2 py-0.5 rounded text-xs font-medium bg-blue-50 text-blue-700 border border-blue-100">{{ techName }}</span>
                      }
                    </div>
                  </td>
                  <td class="px-6 py-4 text-right">
                    <div class="flex items-center justify-end gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                      <a [routerLink]="['/app/cpu', cpu.id]" class="p-1.5 text-blue-600 hover:bg-blue-50 rounded-md transition-colors" title="Podglad">
                        <mat-icon class="text-[20px] w-[20px] h-[20px]">visibility</mat-icon>
                      </a>
                      <a [routerLink]="['/app/cpu', cpu.id, 'edit']" class="p-1.5 text-amber-600 hover:bg-amber-50 rounded-md transition-colors" title="Edytuj">
                        <mat-icon class="text-[20px] w-[20px] h-[20px]">edit</mat-icon>
                      </a>
                      <button (click)="deleteCpu(cpu.id, cpu.model)" class="p-1.5 text-red-600 hover:bg-red-50 rounded-md transition-colors" title="Usun">
                        <mat-icon class="text-[20px] w-[20px] h-[20px]">delete</mat-icon>
                      </button>
                    </div>
                  </td>
                </tr>
              } @empty {
                <tr>
                  <td colspan="7" class="px-6 py-8 text-center text-slate-500 italic">Brak procesorow</td>
                </tr>
              }
            </tbody>
          </table>
        </div>
      </div>

      @if (totalPages > 1) {
        <div class="flex items-center justify-center gap-2">
          <button class="px-3 py-1.5 rounded border border-slate-200 text-sm" [disabled]="page === 0" (click)="goToPage(page - 1)">Poprzednia</button>
          @for (p of pages; track p) {
            <button class="px-3 py-1.5 rounded text-sm" [class.bg-blue-600]="p === page" [class.text-white]="p === page" [class.border]="p !== page" [class.border-slate-200]="p !== page" (click)="goToPage(p)">{{ p + 1 }}</button>
          }
          <button class="px-3 py-1.5 rounded border border-slate-200 text-sm" [disabled]="page >= totalPages - 1" (click)="goToPage(page + 1)">Nastepna</button>
        </div>
      }
    </div>
  `
})
export class CpuListComponent implements OnInit {
  cpus: Cpu[] = [];
  manufacturers: Manufacturer[] = [];
  technologies: Technology[] = [];
  loading = true;
  successMessage = '';
  errorMessage = '';

  criteria: CpuSearchCriteria = {};
  page = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  constructor(
    private cpuService: CpuService,
    private manufacturerService: ManufacturerService,
    private technologyService: TechnologyService
  ) {}

  ngOnInit(): void {
    this.loadCpus();
    this.manufacturerService.getAll().subscribe(m => this.manufacturers = m);
    this.technologyService.getAll().subscribe(t => this.technologies = t);
  }

  loadCpus(): void {
    this.loading = true;
    const hasSearch = Object.values(this.criteria).some(v => v !== undefined && v !== null && v !== '');

    if (hasSearch) {
      this.cpuService.search(this.criteria, this.page, this.pageSize).subscribe({
        next: (res: PagedResponse<Cpu>) => {
          this.cpus = res.content;
          this.totalPages = res.totalPages;
          this.totalElements = res.totalElements;
          this.loading = false;
        },
        error: () => {
          this.errorMessage = 'Nie udało się załadować procesorów';
          this.loading = false;
        }
      });
    } else {
      this.cpuService.getAll().subscribe({
        next: cpus => {
          this.cpus = cpus;
          this.totalElements = cpus.length;
          this.loading = false;
        },
        error: () => {
          this.errorMessage = 'Nie udało się załadować procesorów';
          this.loading = false;
        }
      });
    }
  }

  onSearch(): void {
    this.page = 0;
    this.loadCpus();
  }

  clearFilters(): void {
    this.criteria = {};
    this.page = 0;
    this.loadCpus();
  }

  goToPage(p: number): void {
    this.page = p;
    this.loadCpus();
  }

  deleteCpu(id: string, model: string): void {
    if (confirm(`Czy na pewno chcesz usunąć procesor "${model}"?`)) {
      this.cpuService.delete(id).subscribe({
        next: () => {
          this.successMessage = `Procesor "${model}" został usunięty`;
          this.loadCpus();
          setTimeout(() => this.successMessage = '', 4000);
        },
        error: () => {
          this.errorMessage = 'Nie udało się usunąć procesora';
          setTimeout(() => this.errorMessage = '', 4000);
        }
      });
    }
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }
}

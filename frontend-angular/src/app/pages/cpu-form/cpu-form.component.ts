import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { CpuService } from '../../services/cpu.service';
import { ManufacturerService } from '../../services/manufacturer.service';
import { TechnologyService } from '../../services/technology.service';
import { CpuAiAutofillResponse, CpuCreate } from '../../models/cpu.model';
import { Manufacturer } from '../../models/manufacturer.model';
import { Technology } from '../../models/technology.model';

@Component({
  selector: 'app-cpu-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, MatIconModule],
  template: `
    <div class="max-w-4xl mx-auto space-y-6">
      <div class="flex items-center gap-2 text-sm text-slate-500 mb-1">
        <a routerLink="/app/cpu" class="hover:text-blue-600 transition-colors">Procesory</a>
        <mat-icon class="text-[16px] w-[16px] h-[16px]">chevron_right</mat-icon>
        <span>{{ isEdit ? 'Edytuj' : 'Dodaj nowy' }}</span>
      </div>

      <div class="flex items-center gap-3 mb-6">
        <mat-icon class="text-blue-600 text-3xl">{{ isEdit ? 'edit' : 'add_circle' }}</mat-icon>
        <h1 class="text-2xl font-bold text-slate-900 tracking-tight">{{ isEdit ? 'Edytuj CPU' : 'Dodaj nowy CPU' }}</h1>
      </div>

      @if (errorMessage) {
        <div class="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-sm text-red-700">{{ errorMessage }}</div>
      }

      <form (ngSubmit)="onSubmit()" class="space-y-6">
        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
          <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div class="md:col-span-2">
              <div class="mb-1 flex items-center justify-between gap-3">
                <label class="block text-sm font-medium text-slate-700">Nazwa modelu</label>
                <button
                  type="button"
                  (click)="autofillFromAi()"
                  [disabled]="autofillLoading"
                  class="inline-flex items-center gap-2 px-3 py-1.5 rounded-md text-xs font-medium bg-slate-100 text-slate-700 border border-slate-300 hover:bg-slate-200 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
                >
                  <mat-icon class="text-[16px] w-[16px] h-[16px]">auto_fix_high</mat-icon>
                  {{ autofillLoading ? 'Uzupelniam...' : 'Autofill' }}
                </button>
              </div>
              <input type="text" [(ngModel)]="model" name="model" required class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
            </div>
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-1">Liczba rdzeni</label>
              <input type="number" [(ngModel)]="cores" name="cores" required class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
            </div>
            <div>
              <label class="block text-sm font-medium text-slate-700 mb-1">Liczba watkow</label>
              <input type="number" [(ngModel)]="threads" name="threads" required class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
            </div>
            <div class="md:col-span-2">
              <label class="block text-sm font-medium text-slate-700 mb-1">Czestotliwosc (GHz)</label>
              <input type="number" step="0.1" [(ngModel)]="frequencyGhz" name="frequencyGhz" required class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
            </div>
          </div>
        </div>

        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 space-y-6">
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-1">Producent</label>
            <select [(ngModel)]="manufacturerId" name="manufacturerId" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
              <option [ngValue]="null">-- Wybierz producenta --</option>
              @for (m of manufacturers; track m.id) {
                <option [ngValue]="m.id">{{ m.name }}</option>
              }
            </select>
          </div>

          <div>
            <span class="block text-sm font-medium text-slate-700 mb-3">Technologie</span>
            <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3 max-h-60 overflow-y-auto p-4 border border-slate-200 rounded-lg bg-slate-50">
              @for (tech of technologies; track tech.id) {
                <label class="flex items-center gap-3 p-2 hover:bg-white rounded-md transition-colors cursor-pointer border border-transparent hover:border-slate-200">
                  <input type="checkbox" [checked]="isTechSelected(tech.id)" (change)="toggleTechnology(tech.id)" class="w-4 h-4 text-blue-600 bg-white border-slate-300 rounded focus:ring-blue-500">
                  <span class="text-sm font-medium text-slate-700">{{ tech.name }}</span>
                </label>
              }
            </div>
          </div>

          <div class="border-t border-slate-200 pt-6">
            <h2 class="text-lg font-semibold text-slate-900 mb-4">Specyfikacja techniczna</h2>
            <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Cache L1 (KB)</label>
                <input type="number" [(ngModel)]="cacheL1KB" name="cacheL1KB" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
              </div>
              <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Cache L2 (KB)</label>
                <input type="number" [(ngModel)]="cacheL2KB" name="cacheL2KB" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
              </div>
              <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">Cache L3 (MB)</label>
                <input type="number" [(ngModel)]="cacheL3MB" name="cacheL3MB" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
              </div>
              <div>
                <label class="block text-sm font-medium text-slate-700 mb-1">TDP (W)</label>
                <input type="number" [(ngModel)]="tdpWatts" name="tdpWatts" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
              </div>
              <div class="md:col-span-2">
                <label class="block text-sm font-medium text-slate-700 mb-1">Socket</label>
                <input type="text" [(ngModel)]="socketType" name="socketType" class="w-full px-4 py-2 bg-slate-50 border border-slate-200 rounded-lg text-sm">
              </div>
            </div>
          </div>
        </div>

        <div class="flex items-center gap-4 pt-4">
          <button type="submit" class="inline-flex items-center gap-2 px-6 py-2.5 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-sm">
            <mat-icon class="text-sm">{{ isEdit ? 'save' : 'add' }}</mat-icon>
            {{ isEdit ? 'Zapisz zmiany' : 'Dodaj CPU' }}
          </button>
          <a routerLink="/app/cpu" class="inline-flex items-center gap-2 px-6 py-2.5 bg-slate-500 text-white text-sm font-medium rounded-lg hover:bg-slate-600 transition-colors shadow-sm">
            <mat-icon class="text-sm">close</mat-icon>
            Anuluj
          </a>
        </div>
      </form>
    </div>
  `
})
export class CpuFormComponent implements OnInit {
  isEdit = false;
  cpuId: string | null = null;
  manufacturers: Manufacturer[] = [];
  technologies: Technology[] = [];
  errorMessage = '';
  autofillLoading = false;

  model = '';
  cores: number | null = null;
  threads: number | null = null;
  frequencyGhz: number | null = null;
  manufacturerId: string | null = null;
  selectedTechnologyIds: string[] = [];

  cacheL1KB: number | null = null;
  cacheL2KB: number | null = null;
  cacheL3MB: number | null = null;
  tdpWatts: number | null = null;
  socketType = '';

  constructor(
    private cpuService: CpuService,
    private manufacturerService: ManufacturerService,
    private technologyService: TechnologyService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.manufacturerService.getAll().subscribe(m => this.manufacturers = m);
    this.technologyService.getAll().subscribe(t => this.technologies = t);

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.cpuId = id;
      this.cpuService.getById(this.cpuId).subscribe({
        next: cpu => {
          this.model = cpu.model;
          this.cores = cpu.cores;
          this.threads = cpu.threads;
          this.frequencyGhz = cpu.frequencyGhz;
          this.manufacturerId = cpu.manufacturerId;
          this.selectedTechnologyIds = cpu.technologyIds ?? [];

          if (cpu.specification) {
            this.cacheL1KB = cpu.specification.cacheL1KB;
            this.cacheL2KB = cpu.specification.cacheL2KB;
            this.cacheL3MB = cpu.specification.cacheL3MB;
            this.tdpWatts = cpu.specification.tdpWatts;
            this.socketType = cpu.specification.socketType;
          }
        },
        error: () => this.errorMessage = 'Nie udało się załadować danych procesora'
      });
    }
  }

  toggleTechnology(id: string): void {
    const idx = this.selectedTechnologyIds.indexOf(id);
    if (idx > -1) {
      this.selectedTechnologyIds.splice(idx, 1);
    } else {
      this.selectedTechnologyIds.push(id);
    }
  }

  isTechSelected(id: string): boolean {
    return this.selectedTechnologyIds.includes(id);
  }

  autofillFromAi(): void {
    const cpuName = this.model.trim();
    if (!cpuName) {
      this.errorMessage = 'Podaj nazwe modelu, aby uzyc Autofill';
      return;
    }

    this.errorMessage = '';
    this.autofillLoading = true;

    this.cpuService.autofillWithAi(cpuName).subscribe({
      next: (response) => {
        this.applyAutofill(response);
        this.autofillLoading = false;
      },
      error: () => {
        this.autofillLoading = false;
        this.errorMessage = 'Nie udalo sie pobrac danych z AI';
      }
    });
  }

  private applyAutofill(response: CpuAiAutofillResponse): void {
    this.model = response.model?.trim() || this.model;
    this.cores = this.toRequiredNumberOrNull(response.cores);
    this.threads = this.toRequiredNumberOrNull(response.threads);
    this.frequencyGhz = this.toRequiredNumberOrNull(response.frequencyGhz);

    this.cacheL1KB = this.toOptionalNumberOrNull(response.cacheL1KB);
    this.cacheL2KB = this.toOptionalNumberOrNull(response.cacheL2KB);
    this.cacheL3MB = this.toOptionalNumberOrNull(response.cacheL3MB);
    this.tdpWatts = this.toOptionalNumberOrNull(response.tdpWatts);
    this.socketType = (response.socketType || '').trim();

    this.manufacturerId = this.findManufacturerIdByName(response.manufacturerName);
    this.selectedTechnologyIds = this.findTechnologyIdsByNames(response.technologies);
  }

  private findManufacturerIdByName(name: string | null | undefined): string | null {
    const normalized = this.normalizeName(name);
    if (!normalized) {
      return null;
    }

    const match = this.manufacturers.find(m => this.normalizeName(m.name) === normalized);
    return match ? match.id : null;
  }

  private findTechnologyIdsByNames(names: string[] | null | undefined): string[] {
    if (!names || names.length === 0) {
      return [];
    }

    const incoming = new Set(names.map(name => this.normalizeName(name)).filter(Boolean));
    return this.technologies
      .filter(tech => incoming.has(this.normalizeName(tech.name)))
      .map(tech => tech.id);
  }

  private normalizeName(value: string | null | undefined): string {
    return (value || '').trim().toLowerCase();
  }

  private toRequiredNumberOrNull(value: number | null | undefined): number | null {
    if (value === null || value === undefined || value <= 0) {
      return null;
    }
    return value;
  }

  private toOptionalNumberOrNull(value: number | null | undefined): number | null {
    if (value === null || value === undefined || value <= 0) {
      return null;
    }
    return value;
  }

  onSubmit(): void {
    if (!this.model || !this.cores || !this.threads || !this.frequencyGhz || !this.manufacturerId) {
      this.errorMessage = 'Wypełnij wszystkie wymagane pola';
      return;
    }

    const cpuData: CpuCreate = {
      model: this.model,
      cores: this.cores,
      threads: this.threads,
      frequencyGhz: this.frequencyGhz,
      manufacturerId: this.manufacturerId,
      technologyIds: this.selectedTechnologyIds
    };

    const hasSpecificationData =
      this.cacheL1KB !== null ||
      this.cacheL2KB !== null ||
      this.cacheL3MB !== null ||
      this.tdpWatts !== null ||
      this.socketType.trim().length > 0;

    if (hasSpecificationData) {
      cpuData.specification = {
        cacheL1KB: this.cacheL1KB ?? 0,
        cacheL2KB: this.cacheL2KB ?? 0,
        cacheL3MB: this.cacheL3MB ?? 0,
        tdpWatts: this.tdpWatts ?? 0,
        socketType: this.socketType.trim()
      };
    }

    const request$ = this.isEdit && this.cpuId
      ? this.cpuService.update(this.cpuId, cpuData)
      : this.cpuService.create(cpuData);

    request$.subscribe({
      next: () => this.router.navigate(['/app/cpu']),
      error: () => this.errorMessage = 'Nie udało się zapisać procesora'
    });
  }
}

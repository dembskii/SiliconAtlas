import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { CpuService } from '../../services/cpu.service';
import { ManufacturerService } from '../../services/manufacturer.service';
import { TechnologyService } from '../../services/technology.service';
import { ManufacturerStats } from '../../models/stats.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink, MatIconModule],
  template: `
    <div class="space-y-6">
      <div class="flex items-center justify-between">
        <h1 class="text-2xl font-bold text-slate-900 tracking-tight">Dashboard</h1>
      </div>

      <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 flex flex-col items-center justify-center text-center">
          <span class="text-4xl font-bold text-blue-600 mb-2">{{ cpuCount }}</span>
          <span class="text-sm font-medium text-slate-500 uppercase tracking-wider">Procesorow w bazie</span>
        </div>
        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 flex flex-col items-center justify-center text-center">
          <span class="text-4xl font-bold text-emerald-600 mb-2">{{ manufacturerCount }}</span>
          <span class="text-sm font-medium text-slate-500 uppercase tracking-wider">Producentow</span>
        </div>
        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 flex flex-col items-center justify-center text-center">
          <span class="text-4xl font-bold text-violet-600 mb-2">{{ technologyCount }}</span>
          <span class="text-sm font-medium text-slate-500 uppercase tracking-wider">Technologii</span>
        </div>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
          <div class="px-6 py-4 border-b border-slate-100 bg-slate-50/50">
            <h2 class="text-lg font-semibold text-slate-800">Statystyki producentow</h2>
          </div>
          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left">
              <thead class="text-xs text-slate-500 uppercase bg-slate-50 border-b border-slate-200">
                <tr>
                  <th class="px-6 py-3 font-semibold">Producent</th>
                  <th class="px-6 py-3 font-semibold text-right">Liczba CPU</th>
                  <th class="px-6 py-3 font-semibold text-right">Srednia rdzeni</th>
                  <th class="px-6 py-3 font-semibold text-right">Srednia czestotliwosc</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100">
                @for (stat of manufacturerStats; track stat.manufacturerName) {
                  <tr class="hover:bg-slate-50 transition-colors">
                    <td class="px-6 py-4 font-medium text-slate-900">{{ stat.manufacturerName }}</td>
                    <td class="px-6 py-4 text-right text-slate-600">{{ stat.cpuCount }}</td>
                    <td class="px-6 py-4 text-right text-slate-600">{{ stat.avgCores | number:'1.1-1' }}</td>
                    <td class="px-6 py-4 text-right text-slate-600">{{ stat.avgFrequency | number:'1.2-2' }} GHz</td>
                  </tr>
                }
              </tbody>
            </table>
          </div>
        </div>

        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6">
          <h2 class="text-lg font-semibold text-slate-800 mb-4">Szybkie akcje</h2>
          <div class="flex flex-wrap gap-4">
            <a routerLink="/app/cpu/add" class="inline-flex items-center gap-2 px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition-colors shadow-sm">
              <mat-icon class="text-sm">add</mat-icon>
              Dodaj CPU
            </a>
            <a routerLink="/app/manufacturers/add" class="inline-flex items-center gap-2 px-4 py-2 bg-emerald-600 text-white text-sm font-medium rounded-lg hover:bg-emerald-700 transition-colors shadow-sm">
              <mat-icon class="text-sm">business</mat-icon>
              Dodaj Producenta
            </a>
            <a routerLink="/app/technologies/add" class="inline-flex items-center gap-2 px-4 py-2 bg-violet-600 text-white text-sm font-medium rounded-lg hover:bg-violet-700 transition-colors shadow-sm">
              <mat-icon class="text-sm">memory</mat-icon>
              Dodaj Technologie
            </a>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardComponent implements OnInit {
  cpuCount = 0;
  manufacturerCount = 0;
  technologyCount = 0;
  manufacturerStats: ManufacturerStats[] = [];
  loading = true;

  constructor(
    private cpuService: CpuService,
    private manufacturerService: ManufacturerService,
    private technologyService: TechnologyService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.cpuService.getAll().subscribe({
      next: cpus => this.cpuCount = cpus.length,
      error: () => this.cpuCount = 0
    });

    this.manufacturerService.getAll().subscribe({
      next: m => this.manufacturerCount = m.length,
      error: () => this.manufacturerCount = 0
    });

    this.technologyService.getAll().subscribe({
      next: t => {
        this.technologyCount = t.length;
        this.loading = false;
      },
      error: () => {
        this.technologyCount = 0;
        this.loading = false;
      }
    });

    this.cpuService.getManufacturerStats().subscribe({
      next: stats => this.manufacturerStats = stats,
      error: () => this.manufacturerStats = []
    });
  }
}

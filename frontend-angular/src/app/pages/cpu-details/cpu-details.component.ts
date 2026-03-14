import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { CpuService } from '../../services/cpu.service';
import { Cpu, CpuSpecification, CpuBenchmark } from '../../models/cpu.model';

@Component({
  selector: 'app-cpu-details',
  standalone: true,
  imports: [CommonModule, RouterLink, MatIconModule],
  template: `
    @if (cpu) {
      <div class="space-y-6">
        <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <div class="flex items-center gap-2 text-sm text-slate-500 mb-1">
              <a routerLink="/app/cpu" class="hover:text-blue-600 transition-colors">Procesory</a>
              <mat-icon class="text-[16px] w-[16px] h-[16px]">chevron_right</mat-icon>
              <span>Szczegoly</span>
            </div>
            <h1 class="text-3xl font-bold text-slate-900 tracking-tight">{{ cpu.model }}</h1>
          </div>
          <div class="flex items-center gap-3">
            <a [routerLink]="['/app/cpu', cpu.id, 'edit']" class="inline-flex items-center gap-2 px-4 py-2 bg-amber-500 text-white text-sm font-medium rounded-lg hover:bg-amber-600 transition-colors shadow-sm">
              <mat-icon class="text-sm">edit</mat-icon>
              Edytuj
            </a>
            <button (click)="deleteCpu()" class="inline-flex items-center gap-2 px-4 py-2 bg-red-600 text-white text-sm font-medium rounded-lg hover:bg-red-700 transition-colors shadow-sm">
              <mat-icon class="text-sm">delete</mat-icon>
              Usun
            </button>
          </div>
        </div>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div class="bg-white rounded-xl p-5 border border-slate-200 shadow-sm">
            <span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">ID</span>
            <span class="text-sm font-mono text-slate-700 break-all">{{ cpu.id }}</span>
          </div>
          <div class="bg-white rounded-xl p-5 border border-slate-200 shadow-sm">
            <span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">Producent</span>
            <span class="inline-flex items-center px-2.5 py-0.5 rounded-md text-sm font-medium bg-emerald-50 text-emerald-700 border border-emerald-100">{{ cpu.manufacturerName }}</span>
          </div>
          <div class="bg-white rounded-xl p-5 border border-slate-200 shadow-sm">
            <span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">Rdzenie / Watki</span>
            <div class="flex items-baseline gap-2">
              <span class="text-2xl font-bold text-slate-900">{{ cpu.cores }}</span>
              <span class="text-slate-400">/</span>
              <span class="text-2xl font-bold text-slate-900">{{ cpu.threads }}</span>
            </div>
          </div>
          <div class="bg-white rounded-xl p-5 border border-slate-200 shadow-sm">
            <span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">Czestotliwosc bazowa</span>
            <span class="text-2xl font-bold text-slate-900">{{ cpu.frequencyGhz }} <span class="text-sm font-medium text-slate-500">GHz</span></span>
          </div>
          <div class="bg-white rounded-xl p-5 border border-slate-200 shadow-sm lg:col-span-2">
            <span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-2">Technologie</span>
            <div class="flex flex-wrap gap-2">
              @for (techName of cpu.technologyNames; track techName) {
                <span class="inline-flex items-center px-2.5 py-1 rounded-md text-sm font-medium bg-blue-50 text-blue-700 border border-blue-100">{{ techName }}</span>
              } @empty {
                <span class="text-sm text-slate-500 italic">Brak przypisanych technologii</span>
              }
            </div>
          </div>
        </div>

        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
          <div class="px-6 py-4 border-b border-slate-100 bg-slate-50/50 flex items-center gap-2">
            <mat-icon class="text-slate-400">memory</mat-icon>
            <h2 class="text-lg font-semibold text-slate-800">Specyfikacja techniczna</h2>
          </div>
          <div class="p-6 grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            <div><span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">Socket</span><span class="text-base font-medium text-slate-900">{{ specification?.socketType || '-' }}</span></div>
            <div><span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">TDP</span><span class="text-base font-medium text-slate-900">{{ specification?.tdpWatts ? specification?.tdpWatts + ' W' : '-' }}</span></div>
            <div><span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">Cache L1</span><span class="text-base font-medium text-slate-900">{{ specification?.cacheL1KB ? specification?.cacheL1KB + ' KB' : '-' }}</span></div>
            <div><span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">Cache L2</span><span class="text-base font-medium text-slate-900">{{ specification?.cacheL2KB ? specification?.cacheL2KB + ' KB' : '-' }}</span></div>
            <div><span class="block text-xs font-medium text-slate-500 uppercase tracking-wider mb-1">Cache L3</span><span class="text-base font-medium text-slate-900">{{ specification?.cacheL3MB ? specification?.cacheL3MB + ' MB' : '-' }}</span></div>
          </div>
        </div>

        <div class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden">
          <div class="px-6 py-4 border-b border-slate-100 bg-slate-50/50 flex items-center justify-between">
            <div class="flex items-center gap-2">
              <mat-icon class="text-slate-400">speed</mat-icon>
              <h2 class="text-lg font-semibold text-slate-800">Wyniki benchmarkow</h2>
            </div>
            <a [routerLink]="['/app/cpu', cpu.id, 'benchmark', 'add']" class="inline-flex items-center gap-1 px-3 py-1.5 bg-blue-50 text-blue-700 text-xs font-medium rounded-md hover:bg-blue-100 transition-colors">
              <mat-icon class="text-[16px] w-[16px] h-[16px]">add</mat-icon>
              Dodaj wynik
            </a>
          </div>
          <div class="overflow-x-auto">
            <table class="w-full text-sm text-left">
              <thead class="text-xs text-slate-500 uppercase bg-slate-50 border-b border-slate-200">
                <tr>
                  <th class="px-6 py-3 font-semibold">Data testu</th>
                  <th class="px-6 py-3 font-semibold text-right">Single-Core</th>
                  <th class="px-6 py-3 font-semibold text-right">Multi-Core</th>
                  <th class="px-6 py-3 font-semibold text-right">PassMark</th>
                  <th class="px-6 py-3 font-semibold text-right">Cinebench R23</th>
                </tr>
              </thead>
              <tbody class="divide-y divide-slate-100">
                @for (bench of benchmarks; track bench.id) {
                  <tr class="hover:bg-slate-50 transition-colors">
                    <td class="px-6 py-4 text-slate-600">{{ bench.testDate }}</td>
                    <td class="px-6 py-4 text-right font-mono text-slate-900">{{ bench.singleCoreScore }}</td>
                    <td class="px-6 py-4 text-right font-mono text-slate-900">{{ bench.multiCoreScore }}</td>
                    <td class="px-6 py-4 text-right font-mono text-slate-900">{{ bench.passmarkScore }}</td>
                    <td class="px-6 py-4 text-right font-mono text-slate-900">{{ bench.cinebenchR23 }}</td>
                  </tr>
                } @empty {
                  <tr><td colspan="5" class="px-6 py-8 text-center text-slate-500 italic">Brak wynikow benchmarkow dla tego procesora.</td></tr>
                }
              </tbody>
            </table>
          </div>
        </div>
      </div>
    } @else if (!loading) {
      <div class="text-center py-12">
        <mat-icon class="text-4xl text-slate-300 mb-4">error_outline</mat-icon>
        <h2 class="text-xl font-semibold text-slate-700">Nie znaleziono procesora</h2>
        <a routerLink="/app/cpu" class="inline-flex items-center mt-4 px-4 py-2 bg-blue-600 text-white text-sm font-medium rounded-lg hover:bg-blue-700 transition-colors">Wroc do listy</a>
      </div>
    }
  `
})
export class CpuDetailsComponent implements OnInit {
  cpu: Cpu | null = null;
  specification: CpuSpecification | null = null;
  benchmarks: CpuBenchmark[] = [];
  loading = true;
  error = '';

  constructor(
    private cpuService: CpuService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.error = 'Brak identyfikatora procesora';
      this.loading = false;
      return;
    }

    this.cpuService.getById(id).subscribe({
      next: cpu => {
        this.cpu = cpu;
        this.specification = cpu.specification ?? null;
        this.cpuService.getBenchmarks(cpu.id).subscribe({
          next: (items) => this.benchmarks = items,
          error: () => this.benchmarks = []
        });
        this.loading = false;
      },
      error: () => {
        this.error = 'Nie udało się załadować danych procesora';
        this.loading = false;
      }
    });
  }

  deleteCpu(): void {
    if (!this.cpu) {
      return;
    }
    if (confirm('Czy na pewno chcesz usunac ten procesor?')) {
      this.cpuService.delete(this.cpu.id).subscribe({
        next: () => this.router.navigate(['/app/cpu']),
        error: () => this.error = 'Nie udalo sie usunac procesora'
      });
    }
  }
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from '../environment';
import {
  Cpu,
  CpuAiAutofillRequest,
  CpuAiAutofillResponse,
  CpuCreate,
  CpuSearchCriteria,
  CpuBenchmark,
  PagedResponse
} from '../models/cpu.model';
import { ManufacturerStats } from '../models/stats.model';

@Injectable({ providedIn: 'root' })
export class CpuService {
  private url = `${environment.apiUrl}/cpus`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Cpu[]> {
    return this.http.get<Cpu[]>(this.url);
  }

  getById(id: string): Observable<Cpu> {
    return this.http.get<Cpu>(`${this.url}/${id}`);
  }

  create(cpu: CpuCreate): Observable<Cpu> {
    return this.http.post<Cpu>(this.url, cpu);
  }

  update(id: string, cpu: CpuCreate): Observable<Cpu> {
    return this.http.put<Cpu>(`${this.url}/${id}`, cpu);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  search(criteria: CpuSearchCriteria, page = 0, size = 10, sort = 'model,asc'): Observable<PagedResponse<Cpu>> {
    let params = new HttpParams()
      .set('page', page)
      .set('size', size)
      .set('sort', sort);

    Object.entries(criteria).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        params = params.set(key, value);
      }
    });

    return this.http.get<PagedResponse<Cpu>>(`${this.url}/search`, { params });
  }

  getManufacturerStats(): Observable<ManufacturerStats[]> {
    return this.http.get<ManufacturerStats[]>(`${this.url}/manufacturer-stats`);
  }

  getBenchmarks(cpuId: string): Observable<CpuBenchmark[]> {
    return this.http.get<CpuBenchmark[]>(`${environment.apiUrl}/benchmarks`).pipe(
      map((benchmarks) => benchmarks.filter((benchmark) => benchmark.cpuId === cpuId))
    );
  }

  autofillWithAi(cpuName: string): Observable<CpuAiAutofillResponse> {
    const body: CpuAiAutofillRequest = { cpuName };
    return this.http.post<CpuAiAutofillResponse>(`${environment.apiUrl}/cpu-ai/autofill`, body);
  }
}

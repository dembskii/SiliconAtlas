import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environment';
import { Manufacturer } from '../models/manufacturer.model';

@Injectable({ providedIn: 'root' })
export class ManufacturerService {
  private url = `${environment.apiUrl}/manufacturers`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Manufacturer[]> {
    return this.http.get<Manufacturer[]>(this.url);
  }

  getById(id: string): Observable<Manufacturer> {
    return this.http.get<Manufacturer>(`${this.url}/${id}`);
  }

  create(manufacturer: Partial<Manufacturer>): Observable<Manufacturer> {
    return this.http.post<Manufacturer>(this.url, manufacturer);
  }

  update(id: string, manufacturer: Partial<Manufacturer>): Observable<Manufacturer> {
    return this.http.put<Manufacturer>(`${this.url}/${id}`, manufacturer);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}

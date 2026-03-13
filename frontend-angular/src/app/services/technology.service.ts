import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environment';
import { Technology } from '../models/technology.model';

@Injectable({ providedIn: 'root' })
export class TechnologyService {
  private url = `${environment.apiUrl}/technologies`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Technology[]> {
    return this.http.get<Technology[]>(this.url);
  }

  getById(id: string): Observable<Technology> {
    return this.http.get<Technology>(`${this.url}/${id}`);
  }

  create(technology: Partial<Technology>): Observable<Technology> {
    return this.http.post<Technology>(this.url, technology);
  }

  update(id: string, technology: Partial<Technology>): Observable<Technology> {
    return this.http.put<Technology>(`${this.url}/${id}`, technology);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}

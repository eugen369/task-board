import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Board {
  id: number;
  name: string;
}

export interface CreateBoardRequest {
  name: string;
}

@Injectable({ providedIn: 'root' })
export class BoardService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/api/boards`;

  getAll(): Observable<Board[]> {
    return this.http.get<Board[]>(this.apiUrl);
  }

  getById(id: number): Observable<Board> {
    return this.http.get<Board>(`${this.apiUrl}/${id}`);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  create(request: CreateBoardRequest): Observable<Board> {
    return this.http.post<Board>(this.apiUrl, request);
  }
}

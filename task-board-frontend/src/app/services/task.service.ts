import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE';

export interface Task {
  id: number;
  title: string;
  description: string | null;
  status: TaskStatus;
  dueDate: string | null;
}

export interface CreateTaskRequest {
  title: string;
  description?: string;
  dueDate?: string;
}

@Injectable({ providedIn: 'root' })
export class TaskService {
  private readonly http = inject(HttpClient);

  private tasksUrl(boardId: number): string {
    return `${environment.apiUrl}/api/boards/${boardId}/tasks`;
  }

  getByBoard(boardId: number): Observable<Task[]> {
    return this.http.get<Task[]>(this.tasksUrl(boardId));
  }

  create(boardId: number, request: CreateTaskRequest): Observable<Task> {
    return this.http.post<Task>(this.tasksUrl(boardId), request);
  }

  updateStatus(boardId: number, taskId: number, status: TaskStatus): Observable<Task> {
    return this.http.patch<Task>(`${this.tasksUrl(boardId)}/${taskId}/status`, { status });
  }

  delete(boardId: number, taskId: number): Observable<void> {
    return this.http.delete<void>(`${this.tasksUrl(boardId)}/${taskId}`);
  }
}

import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Task, TaskService, TaskStatus } from '../services/task.service';
import { Board, BoardService } from '../services/board.service';

@Component({
    selector: 'app-board-detail',
    imports: [FormsModule, RouterLink],
    templateUrl: './board-detail.html',
    styleUrl: './board-detail.scss'
})
export class BoardDetail implements OnInit {
    private readonly route = inject(ActivatedRoute);
    private readonly taskService = inject(TaskService);
    private readonly boardService = inject(BoardService);

    boardId = 0;
    boardName = signal<string>('');
    tasks = signal<Task[]>([]);
    error = signal<string | null>(null);
    newTaskTitle = '';

    ngOnInit(): void {
        this.boardId = Number(this.route.snapshot.paramMap.get('id'));
        this.loadBoard();
        this.loadTasks();
    }

    loadBoard(): void {
        this.boardService.getById(this.boardId).subscribe({
            next: (board: Board) => this.boardName.set(board.name),
            error: (e: Error) => this.showError(e.message)
        });
    }

    loadTasks(): void {
        this.taskService.getByBoard(this.boardId).subscribe({
            next: (tasks) => this.tasks.set(tasks),
            error: (e: Error) => this.showError(e.message)
        });
    }

    createTask(): void {
        const title = this.newTaskTitle.trim();
        if (!title) return;

        this.taskService.create(this.boardId, { title }).subscribe({
            next: (task) => {
                this.tasks.update(tasks => [...tasks, task]);
                this.newTaskTitle = '';
            },
            error: (e: Error) => this.showError(e.message)
        });
    }

    statusLabel(status: string): string {
        const labels: Record<string, string> = {
            TODO: 'Offen',
            IN_PROGRESS: 'In Bearbeitung',
            DONE: 'Erledigt'
        };
        return labels[status] ?? status;
    }

    nextStatus(status: TaskStatus): TaskStatus {
        const next: Record<TaskStatus, TaskStatus> = {
            TODO: 'IN_PROGRESS',
            IN_PROGRESS: 'DONE',
            DONE: 'TODO'
        };
        return next[status];
    }

    deleteTask(task: Task): void {
        this.taskService.delete(this.boardId, task.id).subscribe({
            next: () => this.tasks.update(tasks => tasks.filter(t => t.id !== task.id)),
            error: (e: Error) => this.showError(e.message)
        });
    }

    cycleStatus(task: Task): void {
        const status = this.nextStatus(task.status);
        this.taskService.updateStatus(this.boardId, task.id, status).subscribe({
            next: (updated) => this.tasks.update(tasks =>
                tasks.map(t => t.id === updated.id ? updated : t)
            ),
            error: (e: Error) => this.showError(e.message)
        });
    }

    showError(message: string): void {
        this.error.set(message);
        setTimeout(() => this.error.set(null), 5000);
    }
}

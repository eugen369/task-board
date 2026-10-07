import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Board, BoardService } from '../services/board.service';

@Component({
  selector: 'app-board-list',
  imports: [FormsModule, RouterLink],
  templateUrl: './board-list.html',
  styleUrl: './board-list.scss'
})
export class BoardList implements OnInit {
  private readonly boardService = inject(BoardService);

  boards = signal<Board[]>([]);
  error = signal<string | null>(null);
  newBoardName = '';

  ngOnInit(): void {
    this.loadBoards();
  }

  loadBoards(): void {
    this.boardService.getAll().subscribe({
      next: (boards) => this.boards.set(boards),
      error: (e: Error) => this.showError(e.message)
    });
  }

  createBoard(): void {
    const name = this.newBoardName.trim();
    if (!name) return;

    this.boardService.create({ name }).subscribe({
      next: (board) => {
        this.boards.update(boards => [...boards, board]);
        this.newBoardName = '';
      },
      error: (e: Error) => this.showError(e.message)
    });
  }

  deleteBoard(board: Board, event: MouseEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.boardService.delete(board.id).subscribe({
      next: () => this.boards.update(boards => boards.filter(b => b.id !== board.id)),
      error: (e: Error) => this.showError(e.message)
    });
  }

  showError(message: string): void {
    this.error.set(message);
    setTimeout(() => this.error.set(null), 5000);
  }
}

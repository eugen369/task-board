import { Routes } from '@angular/router';
import { BoardList } from './boards/board-list';
import { BoardDetail } from './boards/board-detail';

export const routes: Routes = [
  { path: '', component: BoardList },
  { path: 'boards/:id', component: BoardDetail },
  { path: '**', redirectTo: '' }
];

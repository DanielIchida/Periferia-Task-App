import { Routes } from '@angular/router';
import {TaskIndexComponent} from './pages/task-index/task-index.component';

export const routes: Routes = [
  {
    path: '',
    component: TaskIndexComponent
  },
  {
    path: '**',
    redirectTo: ''
  }
];

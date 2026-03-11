import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../environments/environment';
import {Data, ResulTask, Task} from '../interfaces/Task';
import {catchError, delay, map, Observable, throwError} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TaskService {

  private apiUrl = environment.taskApiUrl;
  private http = inject(HttpClient)

  searchAllTask(page: number = 0) : Observable<Data> {
     return this.http.get<ResulTask>(
       `${this.apiUrl}/task`,
       {
         params: {
           page: page,
         }
       }
     ).pipe(
       map(response => response.data as Data),
       delay(1000),
       catchError(error => {
          return throwError(() => new Error('no se pudo obtener las tareas'));
       })
     );
  }

  deleteTask(id: number): Observable<boolean> {
    return this.http.delete<ResulTask>(`${this.apiUrl}/task/${id}`)
      .pipe(
        map(response => response.data as boolean),
        catchError(error => {
          return throwError(() => new Error('no se puedo eliminar tarea'));
        })
      )
  }

  sendCreateOrEditTask(task: Task): Observable<Task> {
    return this.httpCreateOrEdit(task)
      .pipe(
        map(response => response.data as Task),
        catchError(error => {
          return throwError(() => new Error('no se puedo editar o crear la tarea'));
        })
      )
  }

  private httpCreateOrEdit(task: Task) {
      if (task.id === 0) {
         return this.http.post<ResulTask>(`${this.apiUrl}/task`, task)
      } else {
         return this.http.put<ResulTask>(`${this.apiUrl}/task/${task.id}`, task);
      }
  }

}

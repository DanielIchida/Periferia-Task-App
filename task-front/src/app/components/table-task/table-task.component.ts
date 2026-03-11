import {Component, input, output, signal} from '@angular/core';
import {Task} from '../../interfaces/Task';
import {RangePipePipe} from '../../util/range-pipe-pipe';

@Component({
  selector: 'app-table-task',
  imports: [
    RangePipePipe
  ],
  templateUrl: './table-task.component.html',
})
export class TableTaskComponent {

  tasks = input.required<Task[]>()

  errorMessage = input<string | unknown | null>()
  isLoading = input<boolean>(false)
  isEmpty = input<boolean>(false)
  numberPaginate = input<number>(0)
  updateTaskId = output<Task>()
  deleteTaskId = output<Task>()

  paginateChange = output<number>()

  eventChangePaginate(paginate: number) {
    if (paginate >= 0 && paginate <= this.numberPaginate()) {
        this.paginateChange.emit(paginate);
    }
  }

  protected eventUpdateModal(task: Task) {
    this.updateTaskId.emit(task)
  }

  protected eventDeleteModal(task: Task) {
    this.deleteTaskId.emit(task)
  }
}

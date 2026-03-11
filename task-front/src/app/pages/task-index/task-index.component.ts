import {Component, inject, output, signal} from '@angular/core';
import {NavBarComponent} from '../../components/nav-bar/nav-bar.component';
import {TableTaskComponent} from '../../components/table-task/table-task.component';
import {ModalTaskComponent} from '../../components/modal-task/modal-task.component';
import {TaskService} from '../../services/task.service';
import {Task} from '../../interfaces/Task';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-task-index',
  imports: [
    NavBarComponent,
    TableTaskComponent,
    ModalTaskComponent
  ],
  templateUrl: './task-index.component.html',
})
export class TaskIndexComponent {

  taskServices = inject(TaskService)

  tasks = signal<Task[]>([])
  isLoading = signal<boolean>(false)
  error= signal<string|null>(null)
  isEmpty = signal<boolean>(false)
  numPaginate = signal<number>(0)
  openNewModal = signal<boolean>(false);
  isEditModal = signal<boolean>(false)
  taskCurrent = signal<Task | null>(null)
  paginateCurrent = signal<number>(0)

  constructor() {
    this.loadingTask()
  }

  loadingTask(page: number = 0) {
      this.isLoading.set(true)
      this.tasks.set([])
      this.paginateCurrent.set(page)
      this.taskServices.searchAllTask(page)
        .subscribe({
          next: (result) => {
            this.isLoading.set(false)
            this.tasks.set(result.content)
            this.numPaginate.set(result.totalPages)
          },
          error: ( err ) => {
            this.isLoading.set(false)
            this.error.set(err.message)
          }
        })
  }

  eventOpenNewModal(stateModal: boolean): void {
    this.isEditModal.set(false)
    this.openNewModal.set(stateModal)
  }

  closeNewModal() {
    this.openNewModal.set(false)
  }

  updateModalTask(task: Task) {
    this.taskCurrent.set(task)
    this.isEditModal.set(true)
    this.openNewModal.set(true)
  }

  saveOrEditTask(task: Task) {
     this.taskServices.sendCreateOrEditTask(task)
       .subscribe({
         next: (result) => {
           Swal.fire({
             title: `Tarea #${result.id}`,
             html: `
                <p><b>Title: </b>${result.title}</p>
                <p><b>Description: </b>${result.description}</p>
                <p><b>Status: </b>${result.status}</p>
                <p><b>CreateAt: </b>${result.createdAt}</p>
             `,
             icon: 'success',
             confirmButtonText: 'Ok'
           });
           this.loadingTask(this.paginateCurrent())
         },
         error: ( err ) => {
           this.createAlertError(
             `no se pudo crear o actualizar ${err.message}`,
             err.message,
           )
         }
       })
  }

  createAlertDelete(task: Task) {
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: "btn btn-success",
        cancelButton: "btn btn-danger"
      },
      buttonsStyling: false
    });
    swalWithBootstrapButtons.fire({
      title: "Estas seguro de eliminar la tarea?",
      text: "Si lo hace no puede revertir los cambios!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Si, quiero borrarlo!",
      cancelButtonText: "No, Cancelar!",
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
          this.deleteTask(task)
      } else if (
        /* Read more about handling dismissals below */
        result.dismiss === Swal.DismissReason.cancel
      ) {
        swalWithBootstrapButtons.fire({
          title: "Cancelled",
          text: "Se cancelo la eliminacion de la tarea :)",
          icon: "error"
        });
      }
    });
  }

  private createAlertError(title: string, message: string) {
    Swal.fire({
      title: title,
      text: message,
      icon: 'error',
      confirmButtonText: 'Ok'
    });
  }

  private deleteTask(task: Task) {
    this.taskServices.deleteTask(task.id)
      .subscribe({
        next: (result) => {
          if (result) {
            Swal.fire({
              title: `Tarea #${task.id}`,
              text: `Se elimino la tarea: ${task.title}`,
              icon: 'success',
              confirmButtonText: 'Ok'
            });
            this.loadingTask(this.paginateCurrent())
          }
        },
        error: ( err ) => {
          this.createAlertError(
            `Tarea #${task.id} no se pudo eliminar`,
            err.message,
          )
        }
      })
  }
}

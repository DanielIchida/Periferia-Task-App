import {Component, effect, inject, input, model, output} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Task} from '../../interfaces/Task';

@Component({
  selector: 'app-modal-task',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './modal-task.component.html'
})
export class ModalTaskComponent {

  private fb = inject(FormBuilder);

  isEdit = model<boolean>(false)
  taskData = model<Task | null>(null);

  isOpen = model(false)
  closeModel = output()
  save = output<Task>();

  taskForm: FormGroup = this.fb.group({
    title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(100)]],
    description: ['', [Validators.maxLength(150)]],
    status: ['PENDING',[Validators.required]],
  })

  constructor() {
    effect(() => {
      if (this.isOpen()) {
        this.resetForm();
      }
    });
  }

  close() {
    this.isOpen.set(false)
    this.closeModel.emit()
  }

  closeOutsideClick(event: MouseEvent) {
    if ((event.target as HTMLElement).classList.contains('modal-open')) {
      this.close()
    }
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.taskForm.get(fieldName);
    return field ? field.invalid && (field.dirty || field.touched) : false;
  }

  hasError(fieldName: string, errorType: string): boolean {
    const control = this.taskForm.get(fieldName);
    return control ? control.hasError(errorType) && (control.dirty || control.touched) : false;
  }

  getError(fieldName: string, errorType: string): any {
    const control = this.taskForm.get(fieldName);
    return control ? control.getError(errorType) : null;
  }

  editTask() {
    if (this.taskForm.valid) {
      const taskData: Task = {
        id: this.taskData()?.id ?? 0,
        title: this.taskForm.get('title')?.value,
        description: this.taskForm.get('description')?.value,
        status: this.taskForm.get('status')?.value
      };
      this.save.emit(taskData)
      this.taskData.set({
        id: 0,
        title: '',
        description: '',
        status: 'PENDING',
        createdAt: ''
      })
      this.close()
    } else {
      Object.keys(this.taskForm.controls).forEach(key => {
        const control = this.taskForm.get(key);
        control?.markAsTouched();
      });
    }
  }

  private resetForm(): void {
    if (this.isEdit() && this.taskData()) {
      this.taskForm.patchValue({
        id: this.taskData()?.id ?? 0,
        title: this.taskData()?.title || '',
        description: this.taskData()?.description || '',
        status: this.taskData()?.status || 'PENDING'
      });
    } else {
      this.taskForm.reset({
        title: '',
        description: '',
        status: 'PENDING'
      });
    }
  }
}

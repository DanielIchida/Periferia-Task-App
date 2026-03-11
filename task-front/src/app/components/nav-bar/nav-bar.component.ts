import {Component, output} from '@angular/core';

@Component({
  selector: 'app-nav-bar',
  imports: [],
  templateUrl: './nav-bar.component.html'
})
export class NavBarComponent {

  isOpenModal = output<boolean>()

  protected newTaskModal() {
     this.isOpenModal.emit(true);
  }
}

import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { TopBarComponent } from './components/top-bar/top-bar.component';


@NgModule({
  declarations: [
    TopBarComponent
  ],
  imports: [
    CommonModule, MatToolbarModule, MatButtonModule, MatIconModule
  ],
  exports: [
    TopBarComponent
  ]
})
export class SharedModule { }

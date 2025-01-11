import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MAT_DATE_LOCALE, MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatTableModule } from '@angular/material/table';
import { RouterModule, Routes } from '@angular/router';
import { NgxCurrencyDirective } from 'ngx-currency';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { AbastecimentoAddComponent } from './abastecimento-add/abastecimento-add.component';
import { AbastecimentoListComponent } from './abastecimento-list/abastecimento-list.component';
const routes: Routes = [

  { path: 'abastecimento-listar', component: AbastecimentoListComponent },
  { path: 'abastecimento-criar', component: AbastecimentoAddComponent }
];

@NgModule({
  declarations: [
    AbastecimentoListComponent,
    AbastecimentoAddComponent
  ],
  imports: [
    CommonModule, RouterModule.forChild(routes), MatFormFieldModule, MatInputModule, MatTableModule, MatPaginatorModule,
    MatCardModule, NgxMaskDirective, NgxMaskPipe, MatSnackBarModule, ReactiveFormsModule, MatDatepickerModule,
    MatButtonModule, MatIconModule, MatSelectModule, MatProgressSpinnerModule, MatNativeDateModule, NgxCurrencyDirective



  ], providers: [provideNgxMask(), MatDatepickerModule,
  { provide: MAT_DATE_LOCALE, useValue: 'pt-BR' },],
})
export class AbastecimentosModule { }

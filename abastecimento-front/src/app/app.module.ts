import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { AbastecimentosModule } from './features/abastecimentos/abastecimentos.module';
import { SharedModule } from './shared/shared.module';
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    AbastecimentosModule,
    HttpClientModule,

    FormsModule,
    ReactiveFormsModule, NgxMaskDirective, NgxMaskPipe, MatSnackBarModule
  ],
  providers: [provideNgxMask(),
  { provide: MAT_DATE_LOCALE, useValue: 'pt-BR' },


  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { Component, OnInit } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormBuilder, FormGroup, ValidatorFn, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { map, Observable } from 'rxjs';
import { Abastecimento } from 'src/app/core/models/abastecimento';
import { AbastecimentoService } from 'src/app/core/services/abastecimento-service.service';


// Define o formato de data
export const MY_FORMATS = {
  parse: {
    dateInput: 'DD/MM/YYYY',
  },
  display: {
    dateInput: 'DD/MM/YYYY',
    monthYearLabel: 'MMMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};
@Component({
  selector: 'app-abastecimento-add',
  templateUrl: './abastecimento-add.component.html',
  styleUrls: ['./abastecimento-add.component.css']
})
export class AbastecimentoAddComponent implements OnInit {
  form!: FormGroup;
  isLoading = false;

  constructor(private fb: FormBuilder, private abastecimentoService: AbastecimentoService, private snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.createForm();
  }

  createForm() {
    this.form = this.fb.group({
      dataHora: [null, [Validators.required, this.dataValidator()]],
      placa: [null, [Validators.required, Validators.pattern(/^[A-Z]{3}-\d{4}$/)]],
      quilometragem: [null, [Validators.required, Validators.min(1)], this.quilometragemValidator()],
      valorTotal: [null, [Validators.required, Validators.min(0.01)]]
    });
  }




  dataValidator(): ValidatorFn {
    return (control: AbstractControl): { [key: string]: any } | null => {
      const data = control.value;
      return data > new Date() ? { data: true } : null;
    };
  }


  quilometragemValidator(): AsyncValidatorFn {
    return (control: AbstractControl): Observable<{ [key: string]: any } | null> => {
      const placa = this.form.get('placa')?.value;
      if (!placa) {
        return new Observable(observer => observer.next(null));
      }
      return this.abastecimentoService.getUltimaQuilometragem(placa).pipe(
        map(ultimaQuilometragem => {
          const quilometragem = control.value;
          return ultimaQuilometragem && quilometragem <= ultimaQuilometragem
            ? { quilometragemInvalida: true }
            : null;
        })
      );
    };
  }

  onSubmit() {
    if (this.form.invalid) {
      return;
    }
    this.isLoading = true;
    const abastecimento: Abastecimento = {
      dataHora: this.form.get('dataHora')?.value,
      placa: this.form.get('placa')?.value.toUpperCase(),
      quilometragem: this.form.get('quilometragem')?.value,
      valorTotal: this.form.get('valorTotal')?.value
    };

    this.abastecimentoService.adicionarAbastecimento(abastecimento).subscribe({
      next: () => {
        this.isLoading = false;
        this.snackBar.open('Abastecimento adicionado com sucesso!', 'Fechar');
        this.form.reset();
      },
      error: (error) => {
        this.isLoading = false;
        this.snackBar.open(error.message, 'Fechar', { panelClass: ['error-snackbar'] });
      }
    });
  }
}
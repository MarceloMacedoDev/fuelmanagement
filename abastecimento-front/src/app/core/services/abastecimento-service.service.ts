import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from 'src/environments/environments.dev';
import { Abastecimento, } from '../models/abastecimento';
import { AbastecimentoPageResponse } from '../models/abastecimento-page-response';
import { Pageable } from '../models/pageable';


@Injectable({ providedIn: 'root' })
export class AbastecimentoService {
  /**
   * The base URL for the API used by this service.
   */
  private apiUrl = environment.apiUrl;
  constructor(private http: HttpClient, private _snackBar: MatSnackBar) { }

  /**
   * Realiza uma requisição GET para a API para listar abastecimentos, com paginação.
   * @param pageable Dados de paginação.
   * @param placa Placa do veículo, se for informada, filtra os resultados.
   * @returns Um Observable<AbastecimentoPageResponse> com a lista de abastecimentos.
   */
  listarAbastecimentos(pageable: Pageable, placa?: string): Observable<AbastecimentoPageResponse> {
    let params = new HttpParams()
      .set('pagina', pageable.page.toString())
      .set('tamanhoPagina', pageable.size.toString());
    if (placa) {
      params = params.set('placa', placa);
    }
    return this.http.get<AbastecimentoPageResponse>(`${this.apiUrl}/abastecimentos`, { params }).pipe(
      catchError(error => {
        console.error('Erro ao listar abastecimentos:', error);
        return throwError(() => new Error('Falha ao listar abastecimentos.'));
      })
    );
  }


  /**
   * Adiciona um novo abastecimento.
   * @param abastecimento Dados do abastecimento a ser adicionado.
   * @returns Um Observable que emite o abastecimento adicionado.
   * @throws Error se dados do abastecimento forem inválidos.
   * @throws Error se houver erro ao adicionar o abastecimento na API.
   */
  adicionarAbastecimento(abastecimento: Abastecimento): Observable<Abastecimento> {
    if (!abastecimento.dataHora || abastecimento.dataHora > new Date()) {
      return throwError(() => new Error('Data e hora inválidas. Não pode ser data futura.'));
    }
    if (!abastecimento.placa || !this.validarPlaca(abastecimento.placa)) {
      return throwError(() => new Error('Placa inválida. Formato AAA-1234 ou ABC1234.'));
    }
    if (!abastecimento.quilometragem || abastecimento.quilometragem <= 0) {
      return throwError(() => new Error('Quilometragem inválida. Deve ser maior que zero.'));
    }
    if (!abastecimento.valorTotal || abastecimento.valorTotal <= 0) {
      return throwError(() => new Error('Valor total inválido. Deve ser maior que zero.'));
    }

    return this.http.post<Abastecimento>(`${this.apiUrl}/abastecimentos`, abastecimento).pipe(
      catchError(error => {
        console.error('Erro ao adicionar abastecimento:', error);
        return throwError(() => new Error('Falha ao adicionar abastecimento.'));
      })
    );
  }

  /**
   * Remove um abastecimento com o ID especificado.
   * 
   * @param id O ID do abastecimento a ser removido.
   * @returns Um Observable que emite quando a remoção é concluída.
   * @throws Error se houver falha na requisição para remover o abastecimento.
   */

  removerAbastecimento(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/abastecimentos/${id}`).pipe(
      catchError(error => {
        console.error('Erro ao remover abastecimento:', error);
        return throwError(() => new Error('Falha ao remover abastecimento.'));
      })
    );
  }



  /**
   * Verifica se a placa informada tem o formato correto (AAA-1234 ou ABC1234).
   * @param placa A placa a ser verificada.
   * @returns true se a placa tiver o formato correto, false caso contrário.
   */
  public validarPlaca(placa: string): boolean {
    const regex = /^(?:[A-Z]{3}-?[0-9]{4})$/;
    return regex.test(placa);
  }

  /**
   * Obtém a última quilometragem registrada para o veículo com a placa especificada.
   *
   * @param placa A placa do veículo para o qual a última quilometragem será obtida.
   * @returns Um Observable que emite a última quilometragem registrada como um número.
   *          Retorna 0 se houver um erro ao obter a quilometragem.
   * @throws Error se houver falha na requisição à API.
   */
  getUltimaQuilometragem(placa: string): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/abastecimentos/ultimaQuilometragem/${placa}`).pipe(
      map(quilometragem => quilometragem),
      catchError(error => {
        console.error('Erro ao obter a ultima quilometragem:', error);
        return of(0);
      })
    );
  }
  showSnackBar(msg: string = 'Bem-vindo ao meu aplicativo!') {
    this._snackBar.open(msg, 'Fechar', {
      duration: 3000, // Duração em milissegundos (3 segundos)
      horizontalPosition: 'center',
      verticalPosition: 'top',
      panelClass: ['my-custom-snackbar'] // Adiciona uma classe CSS customizada (opcional)
    });
  }
}
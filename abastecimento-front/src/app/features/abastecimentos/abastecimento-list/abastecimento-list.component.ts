
import { Component, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatTableDataSource } from '@angular/material/table';
import { debounceTime, distinctUntilChanged, Observable, Subject, switchMap, tap } from 'rxjs';
import { Abastecimento } from 'src/app/core/models/abastecimento';
import { AbastecimentoPageResponse } from 'src/app/core/models/abastecimento-page-response';
import { AbastecimentoService } from 'src/app/core/services/abastecimento-service.service';

@Component({
  selector: 'app-abastecimento-list',
  templateUrl: './abastecimento-list.component.html',
  styleUrls: ['./abastecimento-list.component.css']
})
export class AbastecimentoListComponent {
  displayedColumns: string[] = ['placa', 'quilometragem', 'dataHora', 'valorTotal', 'acao'];
  dataSource = new MatTableDataSource<Abastecimento>([]);
  isLoading = false;
  pageSizeOptions: number[] = [5, 10, 15];
  currentPage = 0;
  pageSize = 5;
  totalElements = 0;
  totalPages = 0;
  placaFilter = '';
  placa = ''; // Changed to string for type safety

  pageIndex = 0;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  private searchTerms = new Subject<string>();

  constructor(private abastecimentoService: AbastecimentoService, private _snackBar: MatSnackBar) { }

  ngOnInit(): void {
    this.showSnackBar();
    this.searchAbastecimentos().subscribe();
    this.searchTerms.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(() => this.searchAbastecimentos()),
      tap(() => this.isLoading = true),
      tap(() => this.paginator.firstPage())
    ).subscribe({
      next: (response: AbastecimentoPageResponse) => {
        this.dataSource = new MatTableDataSource(response.content);
        this.totalElements = response.totalElements;
        this.totalPages = response.totalPages;
        this.dataSource.paginator = this.paginator;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Erro ao listar abastecimentos:', error);
        this.isLoading = false;
        this.showSnackBar('Erro ao listar abastecimentos:' + error);
      }
    });
    this.searchTerms.next(this.placa);
  }

  searchAbastecimentos(): Observable<AbastecimentoPageResponse> {
    this.isLoading = true;
    return this.abastecimentoService.listarAbastecimentos({ page: this.currentPage, size: this.pageSize }, this.placa).pipe(
      tap(() => this.isLoading = false)
    );
  }


  onChangePage(event: PageEvent) {
    this.currentPage = event.pageIndex;
    this.pageSize = event.pageSize;
    this.searchAbastecimentos().subscribe({
      next: (response: AbastecimentoPageResponse) => {
        this.dataSource = new MatTableDataSource(response.content);
      },
      error: (error) => {
        console.error('Erro ao listar abastecimentos:', error);
        this.showSnackBar("Erro ao carregar os dados");
      }
    });
  }

  onFilterChange(event: Event) {
    this.placa = (event.target as HTMLInputElement).value.toUpperCase();
    this.searchTerms.next(this.placa);
  }

  showSnackBar(msg: string = 'Bem-vindo') {

    this._snackBar.open(msg, 'Fechar', {
      duration: 3000, // Duração em milissegundos (3 segundos) 
      verticalPosition: 'top', // Adiciona uma classe CSS customizada (opcional)
    });
  }

  deleteAbastecimento(id: number) {
    this.abastecimentoService.removerAbastecimento(id).subscribe({
      next: () => {
        this.abastecimentoService.removerAbastecimento(id).subscribe();
        this.showSnackBar('Abastecimento removido com sucesso!');
        this.searchAbastecimentos().subscribe({
          next: (response: AbastecimentoPageResponse) => {
            this.dataSource = new MatTableDataSource(response.content);
            this.totalElements = response.totalElements;
            this.totalPages = response.totalPages;
            this.dataSource.paginator = this.paginator;
            this.isLoading = false;
          },
          error: (error) => {
            console.error('Erro ao listar abastecimentos:', error);
            this.isLoading = false;
            this.showSnackBar('Erro ao listar abastecimentos:' + error);
          }
        });
      },
      error: (error) => {
        console.error('Erro ao remover abastecimento:', error);
        this.showSnackBar('Erro ao remover abastecimento: ' + error);
      }
    });
  }
}
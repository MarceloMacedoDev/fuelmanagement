import { HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing'; // Adjust path as needed
import { TestBed } from '@angular/core/testing';
import { Abastecimento } from '../models/abastecimento';
import { AbastecimentoPageResponse } from '../models/abastecimento-page-response';
import { Pageable } from '../models/pageable';
import { AbastecimentoService } from './abastecimento-service.service';



describe('AbastecimentoService', () => {
  let service: AbastecimentoService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AbastecimentoService]
    });
    service = TestBed.inject(AbastecimentoService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify();
  });


  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should listarAbastecimentos successfully', () => {
    const mockPageable: Pageable = { page: 0, size: 10, sort: 'id,desc' };
    const mockResponse: AbastecimentoPageResponse = {
      content: [],
      totalPages: 0,
      totalElements: 0,
      size: 10,
      number: 0,
      sort: {},
      numberOfElements: 0,
      first: true,
      last: true,
      empty: true,
    };

    service.listarAbastecimentos(mockPageable).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/abastecimentos?pagina=0&tamanhoPagina=10`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });


  it('should listarAbastecimentos with placa filter', () => {
    const mockPageable: Pageable = { page: 0, size: 10, sort: 'id,desc' };
    const mockResponse: AbastecimentoPageResponse = {
      content: [],
      totalPages: 0,
      totalElements: 0,
      size: 10,
      number: 0,
      sort: {},
      numberOfElements: 0,
      first: true,
      last: true,
      empty: true,
    };

    service.listarAbastecimentos(mockPageable, 'ABC-1234').subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/abastecimentos?pagina=0&tamanhoPagina=10&placa=ABC-1234`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('should listarAbastecimentos handle error', () => {
    const mockPageable: Pageable = { page: 0, size: 10, sort: 'id,desc' };
    const mockError = 'Falha ao listar abastecimentos.';

    service.listarAbastecimentos(mockPageable).subscribe({
      error: error => expect(error.message).toBe(mockError)
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/abastecimentos?pagina=0&tamanhoPagina=10`);
    req.flush('error', { status: 500, statusText: 'Server Error' });
  });


  it('should adicionarAbastecimento successfully', () => {
    const mockAbastecimento: Abastecimento = {
      placa: 'ABC-1234',
      quilometragem: 1000,
      dataHora: new Date(),
      valorTotal: 100,
    };

    service.adicionarAbastecimento(mockAbastecimento).subscribe(response => {
      expect(response).toEqual(mockAbastecimento);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/abastecimentos`);
    expect(req.request.method).toBe('POST');
    req.flush(mockAbastecimento);
  });

  it('should adicionarAbastecimento handle invalid data', () => {
    const mockAbastecimento: Abastecimento = {
      placa: 'ABC-1234',
      quilometragem: 1000,
      dataHora: new Date(Date.now() + 24 * 60 * 60 * 1000), // Future date
      valorTotal: 100,
    };

    service.adicionarAbastecimento(mockAbastecimento).subscribe({
      error: error => expect(error.message).toBe('Data e hora inválidas. Não pode ser data futura.')
    });

    httpMock.expectNone(`${service['apiUrl']}/abastecimentos`);
  });

  it('should adicionarAbastecimento handle invalid placa', () => {
    const mockAbastecimento: Abastecimento = {
      placa: 'ABC12345', // Invalid placa
      quilometragem: 1000,
      dataHora: new Date(),
      valorTotal: 100,
    };

    service.adicionarAbastecimento(mockAbastecimento).subscribe({
      error: error => expect(error.message).toBe('Placa inválida. Formato AAA-1234 ou ABC1234.')
    });

    httpMock.expectNone(`${service['apiUrl']}/abastecimentos`);
  });

  it('should adicionarAbastecimento handle invalid quilometragem', () => {
    const mockAbastecimento: Abastecimento = {
      placa: 'ABC-1234',
      quilometragem: -1000, // Invalid quilometragem
      dataHora: new Date(),
      valorTotal: 100,
    };

    service.adicionarAbastecimento(mockAbastecimento).subscribe({
      error: error => expect(error.message).toBe('Quilometragem inválida. Deve ser maior que zero.')
    });

    httpMock.expectNone(`${service['apiUrl']}/abastecimentos`);
  });

  it('should adicionarAbastecimento handle invalid valorTotal', () => {
    const mockAbastecimento: Abastecimento = {
      placa: 'ABC-1234',
      quilometragem: 1000,
      dataHora: new Date(),
      valorTotal: -100, // Invalid valorTotal
    };

    service.adicionarAbastecimento(mockAbastecimento).subscribe({
      error: error => expect(error.message).toBe('Valor total inválido. Deve ser maior que zero.')
    });

    httpMock.expectNone(`${service['apiUrl']}/abastecimentos`);
  });


  it('should removerAbastecimento successfully', () => {
    const mockId = 1;
    service.removerAbastecimento(mockId).subscribe(() => {
      //expect().nothing to assert directly, we are testing the HTTP call
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/abastecimentos/${mockId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should removerAbastecimento handle error', () => {
    const mockId = 1;
    const mockError = 'Falha ao remover abastecimento.';

    service.removerAbastecimento(mockId).subscribe({
      error: error => expect(error.message).toBe(mockError)
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/abastecimentos/${mockId}`);
    req.flush('error', { status: 500, statusText: 'Server Error' });
  });


  it('should validarPlaca correctly', () => {
    expect(service.validarPlaca('AAA-1234')).toBeTrue();
    expect(service.validarPlaca('ABC1234')).toBeTrue();
    expect(service.validarPlaca('AA1234')).toBeFalse();
    expect(service.validarPlaca('AAA-12345')).toBeFalse();
    expect(service.validarPlaca('123-abc')).toBeFalse();
  });

  it('should getUltimaQuilometragem successfully', () => {
    const mockPlaca = 'ABC-1234';
    const mockQuilometragem = 1234;
    service.getUltimaQuilometragem(mockPlaca).subscribe(quilometragem => {
      expect(quilometragem).toBe(mockQuilometragem);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/abastecimentos/ultimaQuilometragem/${mockPlaca}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockQuilometragem);
  });

  it('should getUltimaQuilometragem handle error and return 0', () => {
    const mockPlaca = 'ABC-1234';

    service.getUltimaQuilometragem(mockPlaca).subscribe(quilometragem => {
      expect(quilometragem).toBe(0);
    });

    const req = httpMock.expectOne(`${service['apiUrl']}/abastecimentos/ultimaQuilometragem/${mockPlaca}`);
    req.flush('error', { status: 500, statusText: 'Server Error' });
  });
});

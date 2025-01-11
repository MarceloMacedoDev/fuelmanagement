import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbastecimentoAddComponent } from './abastecimento-add.component';

describe('AbastecimentoAddComponent', () => {
  let component: AbastecimentoAddComponent;
  let fixture: ComponentFixture<AbastecimentoAddComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AbastecimentoAddComponent]
    });
    fixture = TestBed.createComponent(AbastecimentoAddComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

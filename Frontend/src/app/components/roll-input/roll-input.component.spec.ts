import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RollInputComponent } from './roll-input.component';

describe('RollInputComponent', () => {
  let component: RollInputComponent;
  let fixture: ComponentFixture<RollInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RollInputComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RollInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

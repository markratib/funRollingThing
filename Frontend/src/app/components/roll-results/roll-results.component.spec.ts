import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RollResultsComponent } from './roll-results.component';

describe('RollResultsComponent', () => {
  let component: RollResultsComponent;
  let fixture: ComponentFixture<RollResultsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RollResultsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RollResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

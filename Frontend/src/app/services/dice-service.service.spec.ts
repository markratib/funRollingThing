import { TestBed } from '@angular/core/testing';

import { DiceServiceService } from './dice-service.service';

describe('DiceServiceService', () => {
  let service: DiceServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DiceServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

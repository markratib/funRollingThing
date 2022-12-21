import { TestBed } from '@angular/core/testing';

import { LinkingServiceService } from './linking-service.service';

describe('LinkingServiceService', () => {
  let service: LinkingServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LinkingServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

import { TestBed } from '@angular/core/testing';

import { PaygradeService } from './paygrade.service';

describe('PaygradeService', () => {
  let service: PaygradeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PaygradeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

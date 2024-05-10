import { TestBed } from '@angular/core/testing';

import { ReportdataService } from './reportdata.service';

describe('ReportdataService', () => {
  let service: ReportdataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportdataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

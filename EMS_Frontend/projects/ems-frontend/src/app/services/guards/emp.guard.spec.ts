import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { empGuard } from './emp.guard';

describe('empGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => empGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});

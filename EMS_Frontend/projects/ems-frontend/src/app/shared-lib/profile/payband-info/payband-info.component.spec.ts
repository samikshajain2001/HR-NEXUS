import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaybandInfoComponent } from './payband-info.component';

describe('PaybandInfoComponent', () => {
  let component: PaybandInfoComponent;
  let fixture: ComponentFixture<PaybandInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaybandInfoComponent]
    });
    fixture = TestBed.createComponent(PaybandInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

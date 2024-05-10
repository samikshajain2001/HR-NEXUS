import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpSidebarComponent } from './emp-sidebar.component';

describe('EmpSidebarComponent', () => {
  let component: EmpSidebarComponent;
  let fixture: ComponentFixture<EmpSidebarComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EmpSidebarComponent]
    });
    fixture = TestBed.createComponent(EmpSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountsSummaryComponent } from './accounts-summary.component';

describe('AccountsSummaryComponent', () => {
  let component: AccountsSummaryComponent;
  let fixture: ComponentFixture<AccountsSummaryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountsSummaryComponent]
    });
    fixture = TestBed.createComponent(AccountsSummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

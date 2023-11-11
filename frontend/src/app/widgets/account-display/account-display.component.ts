import { Component, Input } from '@angular/core';
import { Account } from 'src/app/model/entities';

@Component({
  selector: 'app-account-display',
  templateUrl: './account-display.component.html',
  styleUrls: ['./account-display.component.scss']
})
export class AccountDisplayComponent {

  @Input()
  account!: Account;

  @Input()
  compact: boolean = false;

  accountTypeLabel(): string {
    if (this.compact) {
      return this.account.accountType.charAt(0);
    } else {
      return this.account.accountType;
    }
  }
}

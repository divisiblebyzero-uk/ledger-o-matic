import { Component } from '@angular/core';

@Component({
  selector: 'app-accounts-summary',
  templateUrl: './accounts-summary.component.html',
  styleUrls: ['./accounts-summary.component.scss']
})
export class AccountsSummaryComponent {

  public accounts = {
    ASSETS: [ { accountId: 1, accountName: 'Current Assets ', accountType: 'Asset', children: [
      { accountId: 2, accountName: 'Current Bank Account', balance: 1000.0, accountType: 'Asset', children: []},
      { accountId: 3, accountName: 'Savings Bank Account', balance: 53000, accountType: 'Asset', children: []},
    ], expanded: false }],
    EQUITY: [ { accountId: 4, accountName: 'Opening Balances', accountType: 'Equity', children: []}],
    EXPENSES: [
      { accountId: 5, accountName: 'Hobbies', accountType: 'Expense', children: [
        { accountId: 7, accountName: 'Flying', accountType: 'Expense', children: []},
        { accountId: 8, accountName: 'Eating out', accountType: 'Expense', children: []},
      ] },
      { accountId: 6, accountName: 'Household', accountType: 'Expense', children: [
        { accountId: 9, accountName: 'Groceries', accountType: 'Expense', children: []},
        { accountId:10, accountName: 'Utilities', accountType: 'Expense', children: []},

      ] },
      ],
    INCOME: [],
    LIABILITIES: []
   };


}

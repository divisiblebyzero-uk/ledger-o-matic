import { Component, OnInit } from '@angular/core';
import { Apollo, gql} from 'apollo-angular';

export interface Account {
  id: string,
  name: string,
  currentBalance: number,
  accountType: string,
  parentAccount: string|null,
  children: string[]
}

export interface TopLevelAccounts {
  [accountType: string]: Account[]
}

@Component({
  selector: 'app-accounts-summary',
  templateUrl: './accounts-summary.component.html',
  styleUrls: ['./accounts-summary.component.scss']
})
export class AccountsSummaryComponent implements OnInit {

  public accounts: {ASSETS: Account[], EQUITY: Account[], EXPENSES: Account[], INCOME: Account[], LIABILITIES: Account[] } = {
    ASSETS: [],
    EQUITY: [],
    EXPENSES: [],
    INCOME: [],
    LIABILITIES: []
  };

  public accountTypes: string[] = ["ASSET", "EQUITY", "EXPENSE", "INCOME", "LIABILITY"];

  public accountTree: TopLevelAccounts = {};

  loading = true;
  error: any;
  graphaccounts: Account[] = [];


  constructor(private apollo: Apollo) {

  }

  watchAccountType(accountType: string): void {
    this.apollo.watchQuery( {
      query: gql`{ topLevelAccounts(accountType:${accountType}) { 
        id
        name
        currentBalance
        accountType,
        parentAccount { id },
        children { id }
      }}`,
    })
    .valueChanges.subscribe((result: any) => {
      console.log(result.data);
      this.accountTree[accountType] = result.data?.topLevelAccounts as Account[];
      this.loading = result.loading;
      this.error = result.error;
    })
  }

  ngOnInit() {
    this.accountTypes.forEach( accountType => {
      this.watchAccountType(accountType);
    })
  }


}

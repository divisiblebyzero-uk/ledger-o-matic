import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Apollo, gql} from 'apollo-angular';
import { Account, TopLevelAccounts } from 'src/app/model/entities';


@Component({
  selector: 'app-accounts-summary',
  templateUrl: './accounts-summary.component.html',
  styleUrls: ['./accounts-summary.component.scss']
})
export class AccountsSummaryComponent implements OnInit {

  public accountTypes: string[] = ["ASSET", "EQUITY", "EXPENSE", "INCOME", "LIABILITY"];

  public accountTree: TopLevelAccounts = {};

  loading = true;
  error: any;


  constructor(private apollo: Apollo, private router: Router) {

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

  showLedger(account: Account) {
    this.router.navigate(['/account-ledger', account.id])
  }


}

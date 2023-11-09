import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Apollo, gql} from 'apollo-angular';
import { Account, AccountLedger, Transaction } from 'src/app/model/entities';
import { AccountLedgerDataService } from 'src/app/service/account-ledger-data-service';

@Component({
  selector: 'app-account-ledger',
  templateUrl: './account-ledger.component.html',
  styleUrls: ['./account-ledger.component.scss']
})
export class AccountLedgerComponent implements OnInit {

  public accountId: number = -1;
  public account: Account|null = null;
  public loading = true;
  public error: any;
  public accountName: string = "";

  public accountLedgers!: AccountLedger[];

  public fromDate!: Date;
  public toDate!: Date;



  constructor(private apollo: Apollo, private route: ActivatedRoute, private dataService: AccountLedgerDataService, private router: Router) {
    this.fromDate = new Date();
    this.fromDate.setDate(1);
    this.toDate = new Date();
  }

  ngOnInit(): void {
    this.loadData();
  }

  watchTransactions(accountId: number): void {
    this.dataService.downloadAccountLedger(accountId, this.fromDate, this.toDate).subscribe(x => this.accountLedgers = x);
  }

  watchAccount(accountId: number): void {
    this.apollo.watchQuery( {
      query: gql`{ account(id:${accountId}) { 
        id
        name
        accountType
      }}`,
    })
    .valueChanges.subscribe((result: any) => {
      console.log(result.data);
      this.account = result.data?.account as Account;
      this.loading = result.loading;
      this.error = result.error;
      this.watchTransactions(this.accountId);
    })
  }

  public loadData():void {
    this.route.params.subscribe(params => {
      this.accountId = params['accountId'];
      this.watchAccount(this.accountId);
    })
  }
  
  showLedger(accountId: string) {
    this.router.navigate(['/account-ledger', accountId])
  }

}

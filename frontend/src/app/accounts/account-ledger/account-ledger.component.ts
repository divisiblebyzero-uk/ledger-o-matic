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
  public accountLoading = true;
  public accountError: any;
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

  getTransactions(accountId: number): void {
    this.dataService.downloadAccountLedger(accountId, this.fromDate, this.toDate).subscribe(x => {
      this.accountLedgers = x.accountLedgers;
      this.error = x.error;
      this.loading = x.loading;
    });
  }

  getAccount(accountId: number):void {
    this.dataService.downloadAccount(accountId).subscribe( result => {
      this.account = result.account;
      this.accountError = result.error;
      this.accountLoading = result.loading;
    })
  }
  public loadData():void {
    this.route.params.subscribe(params => {
      this.accountId = params['accountId'];
      this.getAccount(this.accountId);
      this.getTransactions(this.accountId);
    })
  }
  
  showLedger(accountId: string) {
    this.router.navigate(['/account-ledger', accountId])
  }

}

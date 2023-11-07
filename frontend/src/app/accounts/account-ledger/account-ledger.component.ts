import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Apollo, gql} from 'apollo-angular';
import { Account, Transaction } from 'src/app/model/entities';

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

  public transactions!: Transaction[];


  constructor(private apollo: Apollo, private route: ActivatedRoute) {

  }

  ngOnInit(): void {
    this.loadData();
  }

  wrangleTransactions(transactions: Transaction[]): Transaction[] {
    var debitDirection:number = -1;
    var creditDirection:number = 1;
    if (this.account?.accountType == "ASSET" || this.account?.accountType == "EXPENSE") {
      debitDirection = 1;
      creditDirection = -1;
    }
    const returnValue: Transaction[] = [];
    transactions.forEach(t => {
      const t2 = structuredClone(t);
      if (t2.creditAccount.id == this.accountId) {
        t2.amount = t2.amount * creditDirection;
        t2.transferAccount = t2.debitAccount
      } else {
        t2.amount = t2.amount * debitDirection;
        t2.transferAccount = t2.creditAccount
      }
      returnValue.push(t2);
    })
    return returnValue;
  }


  watchTransactions(accountId: number): void {
    this.apollo.watchQuery( {
      query: gql`{ transactionsForAccount(accountId:${accountId}) { 
        id
        transactionDate
        description
        amount
        debitAccount{id, name}
        creditAccount{id, name}
      }}`,
    })
    .valueChanges.subscribe((result: any) => {
      console.log(result.data);
      this.transactions = this.wrangleTransactions(result.data?.transactionsForAccount as Transaction[]);
      this.loading = result.loading;
      this.error = result.error;
    })
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

}

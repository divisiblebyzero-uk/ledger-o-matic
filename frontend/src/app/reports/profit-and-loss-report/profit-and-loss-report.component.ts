import { Component, OnInit } from '@angular/core';
import { Account, Transaction } from 'src/app/model/entities';
import { TransactionsDataService } from 'src/app/service/transactions-data.service';

export interface ShortAccount {
  id: number,
  name: string,
  accountType: string
}
export interface AccountSummary {
  account: ShortAccount,
  amount: number
}
@Component({
  selector: 'app-profit-and-loss-report',
  templateUrl: './profit-and-loss-report.component.html',
  styleUrls: ['./profit-and-loss-report.component.scss']
})
export class ProfitAndLossReportComponent implements OnInit {
  public loading = true;
  public error: any;

  public transactions!: Transaction[];

  public fromDate!: Date;
  public toDate!: Date;

  public incomeSummaries!: AccountSummary[];
  public expenseSummaries!: AccountSummary[];

  public incomeTotal!: number;
  public expenseTotal!: number;

  public total!: number;

  constructor(private transactionsDataService: TransactionsDataService) {
  }

  ngOnInit(): void {
    this.loadData();
    this.fromDate = new Date();
    this.fromDate.setDate(1);
    this.toDate = new Date();
  }

  sumByKey = (arr: any[], key: string, value: string) => {
    const map = new Map();
    for(const obj of arr) {
      const currSum = map.get(obj[key]) || 0;
      map.set(obj[key], currSum + obj[value]);
    }
    const res = Array.from(map, ([k, v]) => ({[key]: k, [value]: v}));
    return res;
  }

  wrangleTransactions() {
    
    this.incomeSummaries = this.sumByKey(this.transactions.filter(t => { return t.debitAccount.accountType == "INCOME"}).map(t => {return {account: t.debitAccount, amount: -t.amount}}), 'account', 'amount') as AccountSummary[];
    (this.sumByKey(this.transactions.filter(t => { return t.creditAccount.accountType == "INCOME"}).map(t => {return {account: t.creditAccount, amount: t.amount}}), 'account', 'amount') as AccountSummary[]).forEach(element => {
      this.incomeSummaries.push(element);      
    });

    this.expenseSummaries = this.sumByKey(this.transactions.filter(t => { return t.debitAccount.accountType == "EXPENSE"}).map(t => {return {account: t.debitAccount, amount: t.amount}}), 'account', 'amount') as AccountSummary[];
    (this.sumByKey(this.transactions.filter(t => { return t.creditAccount.accountType == "EXPENSE"}).map(t => {return {account: t.creditAccount, amount: -t.amount}}), 'account', 'amount') as AccountSummary[]).forEach(element => {
      this.expenseSummaries.push(element);      
    });

    var incomeCredits = this.transactions.filter(t => { return t.creditAccount.accountType == "INCOME"}).map(t => t.amount).reduce((rt, a) => rt + a, 0);
    var incomeDebits = this.transactions.filter(t => { return t.debitAccount.accountType == "INCOME"}).map(t => t.amount).reduce((rt, a) => rt + a, 0);
    this.incomeTotal = incomeCredits - incomeDebits;

    var expenseCredits = this.transactions.filter(t => { return t.creditAccount.accountType == "EXPENSE"}).map(t => t.amount).reduce((rt, a) => rt + a, 0);
    var expenseDebits = this.transactions.filter(t => { return t.debitAccount.accountType == "EXPENSE"}).map(t => t.amount).reduce((rt, a) => rt + a, 0);
    this.expenseTotal = expenseDebits - expenseCredits;

    this.total = this.incomeTotal - this.expenseTotal;
  }

  watchTransactions(): void {
    this.transactionsDataService.downloadTransactionsBetweenDates(this.fromDate, this.toDate).subscribe((result: any) => {
      this.transactions = result.transactions as Transaction[];
      this.loading = result.loading;
      this.error = result.error;
      this.wrangleTransactions();
    })
  }

  public loadData():void {
    this.watchTransactions();
  }


}

import { Component, OnInit } from '@angular/core';
import { Account, MonthlyAccountTotal, Transaction } from 'src/app/model/entities';
import { TransactionsDataService } from 'src/app/service/transactions-data.service';
import * as moment from 'moment'

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
  public monthlyAccountTotals!: MonthlyAccountTotal[];
  public months!: Date[];
  public incomeAccounts!: ShortAccount[];
  public expenseAccounts!: ShortAccount[];

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

  prettifyMonth(month: Date): string {
    return moment(month).format("MMM YY")
  }

  getDirection(accountType: String) {
    if (accountType == "INCOME") {
      return -1;
    } else {
      return 1;
    }
  }

  wrangleSummaries() {
    this.months = this.monthlyAccountTotals.map(mat => mat.month);
    const allAccounts: ShortAccount[] = [...new Set(this.monthlyAccountTotals.map(mat => mat.accountTotals.map(at => at.account)).flat())];
    this.incomeAccounts = allAccounts.filter(acc => acc.accountType === "INCOME");
    this.expenseAccounts = allAccounts.filter(acc => acc.accountType === "EXPENSE");
  } 
  
  getMonthlyAmount(account: ShortAccount, month: Date): number {
    const value = this.monthlyAccountTotals.filter(mat => mat.month == month)[0]?.accountTotals.filter(at => at.account.id == account.id)[0]?.amount;
    if (value) {
      return value * this.getDirection(account.accountType);
    } else {
      return 0;
    }
  }

  getMonthlyTotalByAccountType(month: Date, accountType: String): number {
    return this.monthlyAccountTotals.filter(mat => mat.month == month)[0]?.accountTotals.filter(at => at.account.accountType == accountType).map(at => at.amount).reduce((rt, a) => rt + a, 0) * this.getDirection(accountType);
  }

  getMonthlyTotal(month: Date): number {
    return - this.monthlyAccountTotals.filter(mat => mat.month == month)[0]?.accountTotals.filter(at => (at.account.accountType == "EXPENSE")).map(at => at.amount).reduce((rt, a) => rt + a, 0) 
     - this.monthlyAccountTotals.filter(mat => mat.month == month)[0]?.accountTotals.filter(at => (at.account.accountType == "INCOME")).map(at => at.amount).reduce((rt, a) => rt + a, 0)  
    ;
  }

  watchTransactions(): void {
    this.transactionsDataService.downloadMonthlyAccountTotals(this.fromDate, this.toDate).subscribe((result: any) => {
      this.monthlyAccountTotals = result.monthlyAccountTotals as MonthlyAccountTotal[];
      this.loading = result.loading;
      this.error = result.error;
      this.wrangleSummaries();
    })
  }

  public loadData():void {
    this.watchTransactions();
  }


}

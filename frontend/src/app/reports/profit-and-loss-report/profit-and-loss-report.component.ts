import { Component, OnInit } from '@angular/core';
import { MonthlyAccountTotal, Transaction } from 'src/app/model/entities';
import { TransactionsDataService } from 'src/app/service/transactions-data.service';
import * as moment from 'moment'
import { ChartData } from 'chart.js';


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

  chartOptions = {
    plugins: {
      legend: {
      }
    },
    scales: {
      y: {
        stacked: true,
        beginAtZero: true,
        grid: {
          drawBorder: false
        }
      },
      x: {
        stacked: true,
        grid: {
          drawBorder: false
        }
      }
    }
  };

  chartData: ChartData = {
    labels: [],
    datasets: [
      {
        label: 'Sales',
        data: [540, 325, 702, 620],
        backgroundColor: ['rgba(255, 159, 64, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(153, 102, 255, 0.2)'],
        borderColor: ['rgb(255, 159, 64)', 'rgb(75, 192, 192)', 'rgb(54, 162, 235)', 'rgb(153, 102, 255)'],
        borderWidth: 1
      }
    ]
  };


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
    this.chartData.labels = this.months.map(m => this.prettifyMonth(m));


    const allAccounts: ShortAccount[] = [...new Set(this.monthlyAccountTotals.map(mat => mat.accountTotals.map(at => at.account)).flat())];
    this.incomeAccounts = allAccounts.filter(acc => acc.accountType === "INCOME");
    this.expenseAccounts = allAccounts.filter(acc => acc.accountType === "EXPENSE");

    this.chartData.datasets = [];
    this.incomeAccounts.forEach(sa => {
      this.chartData.datasets.push( {label: sa.name, data: this.months.map(month => this.getMonthlyAmount(sa, month)), stack: 'Income'});
    });

    this.expenseAccounts.forEach(sa => {
      this.chartData.datasets.push( {label: sa.name, data: this.months.map(month => this.getMonthlyAmount(sa, month)), stack: 'Expense'});
    });

    this.chartData = {...this.chartData};

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

  public loadData(): void {
    this.watchTransactions();
  }


}

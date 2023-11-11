import { Component, OnInit, ViewChild } from '@angular/core';
import { Account, Transaction } from 'src/app/model/entities';
import { Table } from 'primeng/table';
import { TransactionsDataService } from 'src/app/service/transactions-data.service';

@Component({
  selector: 'app-journal',
  templateUrl: './journal.component.html',
  styleUrls: ['./journal.component.scss']
})
export class JournalComponent implements OnInit {

  public accountId: number = -1;
  public account: Account|null = null;
  public loading = true;
  public error: any;
  public accountName: string = "";

  public transactions!: Transaction[];

  @ViewChild('dt1') dt1!:Table;

  constructor(private transactionsDataService: TransactionsDataService) {
  }

  ngOnInit(): void {
    this.loadData();
  }

  clear(table: Table) {
    table.clear();
  }

  globalFilterUpdate(event:any) {
    console.log(JSON.stringify(event.target.value))
    this.dt1.filterGlobal(event.target.value, 'contains');
  }

  watchTransactions(): void {
    this.transactionsDataService.downloadTransactions()
    .subscribe((result: any) => {
      console.log(result.data);
      this.transactions = result.transactions as Transaction[];
      this.loading = result.loading;
      this.error = result.error;
    })
  }

  public loadData():void {
    this.watchTransactions();
  }

}

import { Injectable } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { MonthlyAccountTotal, Transaction } from '../model/entities';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransactionsDataService {

  constructor(private apollo: Apollo) { }

  downloadTransactionsBetweenDates(fromDate: Date, toDate: Date): Observable<{ transactions: Transaction[], error: any, loading: boolean }> {
    return this.apollo.watchQuery({
      query: gql`{ transactions { 
        id
        transactionDate
        description
        amount
        debitAccount{id, name, accountType}
        creditAccount{id, name, accountType}
      }}`, errorPolicy: 'all'
    }).valueChanges.pipe(
      map((result: any) => {
        return { transactions: result.data?.transactions as Transaction[], error: result.error || result.errors, loading: result.loading };
      })
    );
  }

  downloadTransactions(): Observable<{ transactions: Transaction[], error: any, loading: boolean }> {
    return this.apollo.watchQuery({
      query: gql`{ transactions { 
        id
        transactionDate
        description
        amount
        debitAccount{id, name, accountType}
        creditAccount{id, name, accountType}
      }}`, errorPolicy: 'all'
    }).valueChanges.pipe(
      map((result: any) => {
        return { transactions: result.data?.transactions as Transaction[], error: result.error || result.errors, loading: result.loading };
      })
    );
  }

  downloadMonthlyAccountTotals(fromDate: Date, toDate: Date): Observable<{ monthlyAccountTotals: MonthlyAccountTotal[], error: any, loading: boolean }> {
    return this.apollo.watchQuery({
      query: gql`{   monthlySummary(fromDate:"2023-01-01",toDate:"2023-11-30") {
        month
        accountTotals{
          account {
            id, name, accountType
          }
          amount
        }
      }}`, errorPolicy: 'all'
    }).valueChanges.pipe(
      map((result: any) => {
        return { monthlyAccountTotals: result.data?.monthlySummary as MonthlyAccountTotal[], error: result.error || result.errors, loading: result.loading };
      })
    );
  }


}

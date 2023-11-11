import { Injectable } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { Transaction } from '../model/entities';
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
}

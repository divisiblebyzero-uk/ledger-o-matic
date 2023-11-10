import { Injectable } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { Account, AccountLedger } from '../model/entities';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

    
@Injectable()
export class AccountLedgerDataService {

    constructor(private apollo: Apollo) {

    }

    

  downloadAccount(accountId: number): Observable<{account: Account, error: any, loading:boolean}> {
    return this.apollo.watchQuery( {
      query: gql`{ account(id:${accountId}) { 
        id
        name
        accountType
      }}`, errorPolicy: 'all'
    }).valueChanges.pipe(
      map((result: any) => {
        return { account: result.data?.account as Account, error: result.error||result.errors, loading: result.loading };
      })
    );
  }


    downloadAccountLedger(accountId: number, fromDate: Date, toDate: Date): Observable <{accountLedgers: AccountLedger[], error: any, loading: boolean}> {
      return this.apollo.watchQuery( {
        query: gql`{
          accountLedger(accountId:${accountId},fromDate:"${fromDate.toISOString().split('T')[0]}", toDate:"${toDate.toISOString().split('T')[0]}") {
            ledgerAccount{id, name}
            description
            ledgerDate
            debitAmount
            creditAmount
            transferAccount{id,name}
            runningTotal
        }
      }`, errorPolicy: 'all'
      })
      .valueChanges.pipe(
        map((result: any) => {
          return { accountLedgers: result.data?.accountLedger as AccountLedger[], error: result.error||result.errors, loading: result.loading };
        })
      );
    }
  
};
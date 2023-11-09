import { Injectable } from '@angular/core';
import { Apollo, gql } from 'apollo-angular';
import { AccountLedger } from '../model/entities';
import { Observable } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
    
@Injectable()
export class AccountLedgerDataService {

    constructor(private apollo: Apollo) {

    }

    downloadAccountLedger(accountId: number, fromDate: Date, toDate: Date): Observable <AccountLedger[]> {
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
      }`
      })
      .valueChanges.pipe(
        map((result: any) => {
          return result.data?.accountLedger as AccountLedger[];
        })
      );
    }
  
};
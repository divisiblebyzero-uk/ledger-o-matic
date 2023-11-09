import { Injectable, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { Apollo, gql } from 'apollo-angular';
import { Account } from '../model/entities';
    
@Injectable()
export class AccountTreeDataService {

    accountTypes: string[] = ["ASSET", "EQUITY", "EXPENSE", "INCOME", "LIABILITY"];

    constructor(private apollo: Apollo) {

    }

    downloadAccounts(): TreeNode[] {
        const accountsTree: TreeNode[] = [];
        this.accountTypes.forEach(accountType => {
            const topLevel: TreeNode = {
                key: accountType,
                label: accountType,
                data: { id: accountType, name: accountType, currentBalance: null},
                children: []
            };
            accountsTree.push(topLevel);
            this.apollo.watchQuery( {
                query: gql`{ topLevelAccounts(accountType:${accountType}) { 
                  id
                  name
                  currentBalance
                  accountType,
                  parentAccount { id },
                  children { id }
                }}`,
              })
              .valueChanges.subscribe((result: any) => {
                this.addAccounts(result.data?.topLevelAccounts as Account[], topLevel);
                
              })
              
            });
      
            return accountsTree;
      }


      downloadChildren(parentNode: TreeNode, accountId: number) {

        this.apollo.watchQuery( {
            query: gql`{ account(id:${accountId}) { 
              id
              name
              currentBalance
              accountType,
              parentAccount { id },
              children { id }
            }}`,
          })
          .valueChanges.subscribe((result: any) => {
            this.addAccounts([result.data?.account as Account], parentNode);
            
          })
          
        }
  

      addAccounts(accounts: Account[], parent: TreeNode) {
        accounts.forEach(account => {
            const accountNode: TreeNode = {
                key: account.id + "",
                label: account.name,
                data: account,
                children: [],
                type: "account"
            }
            if (account.children.length > 0) {
                account.children.forEach(child => {
                    child.id;
                    this.downloadChildren(accountNode, child.id);
                });
            }
            parent.children?.push(accountNode);
        })
      }

      getAccounts() {
        return Promise.resolve(this.downloadAccounts());
    }

  
};
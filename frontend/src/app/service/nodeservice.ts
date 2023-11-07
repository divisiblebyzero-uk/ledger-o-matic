import { Injectable, OnInit } from '@angular/core';
import { TreeNode } from 'primeng/api';
import { Apollo, gql } from 'apollo-angular';
import { Account } from '../model/entities';
    
@Injectable()
export class NodeService {

    accountTypes: string[] = ["ASSET", "EQUITY", "EXPENSE", "INCOME", "LIABILITY"];

    constructor(private apollo: Apollo) {

    }

    downloadAccounts(): TreeNode[] {
        const accountsTree: TreeNode[] = [];
        this.accountTypes.forEach(accountType => {
            const topLevel: TreeNode = {
                key: accountType,
                label: accountType,
                data: accountType,
                children: [],
                expanded: true
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
                console.log(result.data);
                this.addAccounts(result.data?.topLevelAccounts as Account[], topLevel);
              })
              
            });
      
            return accountsTree;
      }


      addAccounts(accounts: Account[], parent: TreeNode) {
        accounts.forEach(account => {
            console.log("Adding account: " + account.name)
            const accountNode: TreeNode = {
                key: account.id + "",
                label: account.name,
                data: account,
                children: [],
                type: "account"
            }
            parent.children?.push(accountNode);
        })
      }

      getAccounts() {
        return Promise.resolve(this.downloadAccounts());
    }

  
};
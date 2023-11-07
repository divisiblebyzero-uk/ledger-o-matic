import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Apollo, gql} from 'apollo-angular';
import { Account, TopLevelAccounts } from 'src/app/model/entities';
import { TreeNode } from 'primeng/api';
import { NodeService } from 'src/app/service/nodeservice';

@Component({
  selector: 'app-accounts-summary',
  templateUrl: './accounts-summary.component.html',
  styleUrls: ['./accounts-summary.component.scss']
})
export class AccountsSummaryComponent implements OnInit {

  public accountTypes: string[] = ["ASSET", "EQUITY", "EXPENSE", "INCOME", "LIABILITY"];

  public accountTree: TopLevelAccounts = {};

  loading = true;
  error: any;

  treeData !: TreeNode[];

  constructor(private apollo: Apollo, private router: Router, private nodeService: NodeService) {

  }

  watchAccountType(accountType: string): void {
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
      this.accountTree[accountType] = result.data?.topLevelAccounts as Account[];
      this.loading = result.loading;
      this.error = result.error;
    })
  }

  ngOnInit() {
    this.nodeService.getAccounts().then((data) => (this.treeData = data));
    /*this.accountTypes.forEach( accountType => {
      this.watchAccountType(accountType);
    })*/
  }

  showLedger(accountId: string) {
    this.router.navigate(['/account-ledger', accountId])
  }

  expandAll() {
    this.treeData.forEach((node) => {
        this.expandRecursive(node, true);
    });
}

collapseAll() {
    this.treeData.forEach((node) => {
        this.expandRecursive(node, false);
    });
}

private expandRecursive(node: TreeNode, isExpand: boolean) {
    node.expanded = isExpand;
    if (node.children) {
        node.children.forEach((childNode) => {
            this.expandRecursive(childNode, isExpand);
        });
    }
}

}

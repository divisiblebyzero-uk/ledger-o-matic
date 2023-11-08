import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Apollo, gql } from 'apollo-angular';
import { Account, TopLevelAccounts } from 'src/app/model/entities';
import { TreeNode } from 'primeng/api';
import { NodeService } from 'src/app/service/nodeservice';

@Component({
  selector: 'app-accounts-summary',
  templateUrl: './accounts-summary.component.html',
  styleUrls: ['./accounts-summary.component.scss']
})
export class AccountsSummaryComponent implements OnInit {

  public accountTree: TopLevelAccounts = {};

  loading = true;
  error: any;

  treeData !: TreeNode[];

  constructor(private router: Router, private nodeService: NodeService) {
  }

  ngOnInit() {
    this.nodeService.getAccounts().then((data) => (this.treeData = data));
  }

  showLedger(accountId: string) {
    this.router.navigate(['/account-ledger', accountId])
  }

}

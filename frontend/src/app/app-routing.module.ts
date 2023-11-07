import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeScreenComponent } from './misc/welcome-screen/welcome-screen.component';
import { AccountsSummaryComponent } from './accounts/accounts-summary/accounts-summary.component';
import { AccountLedgerComponent } from './accounts/account-ledger/account-ledger.component';

const routes: Routes = [
  { path: '', component: WelcomeScreenComponent },
  { path: 'accounts', component: AccountsSummaryComponent },
  { path: 'account-ledger/:accountId', component: AccountLedgerComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

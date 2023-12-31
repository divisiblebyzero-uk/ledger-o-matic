import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeScreenComponent } from './misc/welcome-screen/welcome-screen.component';
import { AccountsSummaryComponent } from './accounts/accounts-summary/accounts-summary.component';
import { AccountLedgerComponent } from './accounts/account-ledger/account-ledger.component';
import { JournalComponent } from './journal/journal/journal.component';
import { ProfitAndLossReportComponent } from './reports/profit-and-loss-report/profit-and-loss-report.component';

const routes: Routes = [
  { path: '', component: WelcomeScreenComponent },
  { path: 'accounts', component: AccountsSummaryComponent },
  { path: 'account-ledger/:accountId', component: AccountLedgerComponent },
  { path: 'journal', component: JournalComponent },
  { path: 'pnlReport', component: ProfitAndLossReportComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

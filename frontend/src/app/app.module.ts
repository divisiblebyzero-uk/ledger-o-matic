import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { WelcomeScreenComponent } from './misc/welcome-screen/welcome-screen.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AccountsSummaryComponent } from './accounts/accounts-summary/accounts-summary.component';
import { GraphQLModule } from './graphql.module';
import { HttpClientModule } from '@angular/common/http';
import { AccountLedgerComponent } from './accounts/account-ledger/account-ledger.component';
import { StyleDirective } from './style.directive';
import { TableModule } from 'primeng/table';
import { TreeModule } from 'primeng/tree';
import { TreeTableModule } from 'primeng/treetable';
import { AccountTreeDataService } from './service/account-tree-data-service';
import { JournalComponent } from './journal/journal/journal.component';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AccountLedgerDataService } from './service/account-ledger-data-service';
import { ProfitAndLossReportComponent } from './reports/profit-and-loss-report/profit-and-loss-report.component';
import { AccountDisplayComponent } from './widgets/account-display/account-display.component';
import { ChartModule } from 'primeng/chart';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeScreenComponent,
    AccountsSummaryComponent,
    AccountLedgerComponent,
    StyleDirective,
    JournalComponent,
    ProfitAndLossReportComponent,
    AccountDisplayComponent
  ],
  imports: [
    InputTextModule,
    ButtonModule,
    FormsModule,
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    NgbModule,
    GraphQLModule,
    HttpClientModule,
    TableModule,
    TreeModule,
    TreeTableModule,
    ChartModule
  ],
  providers: [AccountTreeDataService, AccountLedgerDataService],
  bootstrap: [AppComponent]
})
export class AppModule { }

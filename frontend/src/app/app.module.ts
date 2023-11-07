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
import { NodeService } from './service/nodeservice';

@NgModule({
  declarations: [
    AppComponent,
    WelcomeScreenComponent,
    AccountsSummaryComponent,
    AccountLedgerComponent,
    StyleDirective
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
    NgbModule,
    GraphQLModule,
    HttpClientModule,
    TableModule,
    TreeModule
  ],
  providers: [NodeService],
  bootstrap: [AppComponent]
})
export class AppModule { }

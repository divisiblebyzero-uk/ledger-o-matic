import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { WelcomeScreenComponent } from './misc/welcome-screen/welcome-screen.component';
import { AccountsSummaryComponent } from './accounts/accounts-summary/accounts-summary.component';

const routes: Routes = [
  {path: '', component: WelcomeScreenComponent},
  {path: 'accounts', component: AccountsSummaryComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

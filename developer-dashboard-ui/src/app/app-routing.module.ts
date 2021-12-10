import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { C1Component } from './components/c1/c1.component';
import { C2Component } from './components/c2/c2.component';
import { HomeComponent } from './components/home/home.component';
import { LinksComponent } from './components/links/links.component';
import { ListenToQueueComponent } from './components/listen-to-queue/listen-to-queue.component';
import { RoCalendarComponent } from './components/ro-calendar/ro-calendar.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'sns', component: C1Component },
  { path: 'settings', component: C2Component },
  { path: 'links', component: LinksComponent },
  { path: 'ro-calendar', component: RoCalendarComponent },
  { path: 'listen-to-queue/:queue', component: ListenToQueueComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

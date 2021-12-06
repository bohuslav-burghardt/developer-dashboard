import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { C1Component } from './components/c1/c1.component';
import { C2Component } from './components/c2/c2.component';
import { HomeComponent } from './components/home/home.component';
import { MaterialExampleModule } from '../material.module';
import { HttpClientModule } from '@angular/common/http';
import { SnsSubscriptionsComponent } from './components/sns-subscriptions/sns-subscriptions.component';
import { SnsSubscriptionComponent } from './components/sns-subscription/sns-subscription.component';

@NgModule({
  declarations: [
    AppComponent,
    C1Component,
    C2Component,
    HomeComponent,
    SnsSubscriptionsComponent,
    SnsSubscriptionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialExampleModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

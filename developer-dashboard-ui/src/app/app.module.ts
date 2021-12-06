import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { C1Component } from './components/c1/c1.component';
import { C2Component } from './components/c2/c2.component';
import { HomeComponent } from './components/home/home.component';
import { MaterialExampleModule } from '../material.module';

@NgModule({
  declarations: [
    AppComponent,
    C1Component,
    C2Component,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MaterialExampleModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

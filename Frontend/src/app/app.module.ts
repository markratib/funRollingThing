import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RollInputComponent } from './components/roll-input/roll-input.component';
import { RollResultsComponent } from './components/roll-results/roll-results.component';
import { AgChartsAngularModule } from 'ag-charts-angular';

@NgModule({
  declarations: [
    AppComponent,
    RollInputComponent,
    RollResultsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    AgChartsAngularModule
    
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

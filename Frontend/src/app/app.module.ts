import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { RollInputComponent } from './components/roll-input/roll-input.component';
import { RollResultsComponent } from './components/roll-results/roll-results.component';
import { AgChartsAngularModule } from 'ag-charts-angular';
import { TopBarComponent } from './components/top-bar/top-bar.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { AboutMeComponent } from './components/about-me/about-me.component';

@NgModule({
  declarations: [
    AppComponent,
    RollInputComponent,
    RollResultsComponent,
    TopBarComponent,
    HomePageComponent,
    AboutMeComponent
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

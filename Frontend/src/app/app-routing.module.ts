import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AboutMeComponent } from './components/about-me/about-me.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { RollInputComponent } from './components/roll-input/roll-input.component';

const routes: Routes = [
  {path: '', component: HomePageComponent},
  {path: 'HomePage', component: HomePageComponent},
  {path: 'AboutMe', component: AboutMeComponent},
  {path: 'LowRoller', component: RollInputComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

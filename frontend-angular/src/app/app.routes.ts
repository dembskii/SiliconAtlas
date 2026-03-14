import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { CpuListComponent } from './pages/cpu-list/cpu-list.component';
import { CpuFormComponent } from './pages/cpu-form/cpu-form.component';
import { CpuDetailsComponent } from './pages/cpu-details/cpu-details.component';
import { ManufacturerListComponent } from './pages/manufacturer-list/manufacturer-list.component';
import { TechnologyListComponent } from './pages/technology-list/technology-list.component';
import { ManufacturerFormComponent } from './pages/manufacturer-form/manufacturer-form.component';
import { TechnologyFormComponent } from './pages/technology-form/technology-form.component';
import { BenchmarkFormComponent } from './pages/benchmark-form/benchmark-form.component';
import { authGuard } from './guards/auth.guard';
import { LayoutComponent } from './components/layout/layout.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: 'app',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      { path: '', component: DashboardComponent },
      { path: 'cpu', component: CpuListComponent },
      { path: 'cpu/add', component: CpuFormComponent },
      { path: 'cpu/:id', component: CpuDetailsComponent },
      { path: 'cpu/:id/edit', component: CpuFormComponent },
      { path: 'cpu/:id/benchmark/add', component: BenchmarkFormComponent },
      { path: 'manufacturers', component: ManufacturerListComponent },
      { path: 'manufacturers/add', component: ManufacturerFormComponent },
      { path: 'manufacturers/:id/edit', component: ManufacturerFormComponent },
      { path: 'technologies', component: TechnologyListComponent },
      { path: 'technologies/add', component: TechnologyFormComponent },
      { path: 'technologies/:id/edit', component: TechnologyFormComponent }
    ]
  },
  { path: '**', redirectTo: '' }
];

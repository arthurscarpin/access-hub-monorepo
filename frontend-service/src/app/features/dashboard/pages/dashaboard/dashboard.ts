import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { DashboardCard } from '../../components/dashboard-card/dashboard-card';
import { DashboardTable } from '../../components/dashboard-table/dashboard-table';
import { DashboardGateTraffic } from '../../components/dashboard-gate-traffic/dashboard-gate-traffic';
import { DashboardCaptureChart } from "../../components/dashboard-capture-chart/dashboard-capture-chart";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    Sidebar,
    TopMenu,
    PageTitle,
    DashboardCard,
    DashboardTable,
    DashboardGateTraffic,
    DashboardCaptureChart
  ],
  templateUrl: './dashboard.html',
})
export class Dashboard { }
import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { InputSearch } from '../../../../shared/components/input-search/input-search';
import { VehiclesTable } from '../../components/vehicles-table/vehicles-table';

@Component({
  selector: 'app-vehicles',
  standalone: true,
  imports: [Sidebar, TopMenu, PageTitle, InputSearch, VehiclesTable],
  templateUrl: './vehicles.html',
})
export class Vehicles{}
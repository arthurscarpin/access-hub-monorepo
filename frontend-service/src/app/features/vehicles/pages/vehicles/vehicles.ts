import { Component } from '@angular/core';
import { SideMenu } from '../../../../shared/components/side-menu/side-menu';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { InputSearch } from '../../../../shared/components/input-search/input-search';
import { VehiclesTable } from '../../components/vehicles-table/vehicles-table';

import { LucideAngularModule, Plus } from 'lucide-angular';

@Component({
  selector: 'app-vehicles',
  standalone: true,
  imports: [LucideAngularModule, SideMenu, TopMenu, PageTitle, InputSearch, VehiclesTable],
  templateUrl: './vehicles.html',
})
export class Vehicles{
  readonly Plus: any = Plus;
}
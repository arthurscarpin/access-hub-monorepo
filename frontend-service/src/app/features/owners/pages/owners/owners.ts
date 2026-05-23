import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { TopMenu } from '../../../../shared/components/top-menu/top-menu';
import { PageTitle } from '../../../../shared/components/page-title/page-title';
import { OwnersCard } from '../../components/owners-card/owners-card';

@Component({
  selector: 'app-owners-page',
  standalone: true,
  imports: [Sidebar, TopMenu, PageTitle, OwnersCard],
  templateUrl: './owners.html',
})
export class Owners {}
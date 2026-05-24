import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { OwnersCard } from '../../components/owners-card/owners-card';

@Component({
  selector: 'app-owners-page',
  standalone: true,
  imports: [Sidebar, OwnersCard],
  templateUrl: './owners.html',
})
export class Owners {}
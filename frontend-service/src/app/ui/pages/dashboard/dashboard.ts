import { Component } from '@angular/core';
import { Sidebar } from 'src/app/ui/components/shared/sidebar/sidebar';
import { Menu } from 'src/app/ui/components/shared/menu/menu';
import { AuthService } from '@core/services/auth.service';
import { PageHeader } from 'src/app/ui/components/shared/page-header/page-header';
import { EventCard } from 'src/app/ui/components/dashboard/event-card/event-card';
import { EventHistoryItem } from 'src/app/ui/components/dashboard/event-history-item/event-history-item';
import { EVENT_CARD_OPTIONS } from 'src/app/ui/components/dashboard/event-card/event-card.types';
import { EVENT_HISTORY_OPTIONS } from '@ui/pages/dashboard/event-history.types';

@Component({
  standalone: true,
  selector: 'app-dashboard',
  imports: [Sidebar, Menu, PageHeader, EventCard, EventHistoryItem],
  templateUrl: './dashboard.html',
})
export class Dashboard {
  // BreadCrumb
  breadCrumbOperation: string = 'Operation';
  breadCrumbName: string = 'Dashboard';
  
  // Page Header
  pageTitle: string = '';
  pageCategory: string = 'Overview';
  pageDescription: string = 'Monitor your operation and latest access events.';
  
  // Button
  buttonLabel: string = 'New capture';
  buttonStyle: string = 'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';

  // Logout
  username: string = '';
  email: string = '';

  // Event Cards
  eventCards = EVENT_CARD_OPTIONS;

  // Event History
  eventHistory = EVENT_HISTORY_OPTIONS;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    ({ email: this.email, username: this.username } = this.authService.getUsernameAndEmail() || {});
    const greeting: string = this.getGreeting();
    const usernameFormatted: string =
      this.username.charAt(0).toUpperCase() + this.username.slice(1).toLowerCase();
    this.pageTitle = `${greeting} ${usernameFormatted}!`;
  }

  getGreeting(): string {
    const hour = new Date().getHours();
    if (hour >= 5 && hour < 12) {
      return 'Good morning';
    }
    if (hour >= 12 && hour < 18) {
      return 'Good afternoon';
    }
    return 'Good evening';
  }
}

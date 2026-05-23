import { Component } from '@angular/core';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';


@Component({
  standalone: true,
  selector: 'app-captures',
  imports: [Sidebar, Menu, PageHeader, Button],
  templateUrl: './captures.html'
})
export class Captures {
  // BreadCrumb
  breadCrumbOperation: string = 'Operation';
  breadCrumbName: string = 'Captures';
  
  // Page Header
  pageTitle: string = 'Captures';
  pageCategory: string = 'Operation';
  pageDescription: string = 'Send and process batches of images in ZIP format for automatic license plate recognition.';
  
  // Button
  buttonLabel: string = 'Send Capture';
  buttonStyle: string = 'rounded-xl bg-slate-900 px-5 py-3 text-sm font-medium text-white transition hover:bg-slate-800 cursor-pointer';
}

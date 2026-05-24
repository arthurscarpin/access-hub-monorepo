import { Component } from '@angular/core';
import { NgClass, NgComponentOutlet } from '@angular/common';
import { Sidebar } from '@components/shared/sidebar/sidebar';
import { Menu } from '@components/shared/menu/menu';
import { PageHeader } from '@components/shared/page-header/page-header';
import { Button } from '@components/shared/button/button';
import { PROCESSING_HISTORY_OPTIONS } from '@pages/captures/captures.types';
import { LucideCloudUpload } from '@lucide/angular';


@Component({
  standalone: true,
  selector: 'app-captures',
  imports: [NgClass, NgComponentOutlet, Sidebar, Menu, PageHeader, Button, LucideCloudUpload],
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

  buttonDragAndDropLabel: string = 'Select File';
  buttonDragAndDropStyle: string = 'mt-8 rounded-2xl border border-slate-200 bg-white px-5 py-3 text-sm font-medium text-slate-700 transition hover:bg-slate-50 cursor-pointer'; 

  processingHistory = PROCESSING_HISTORY_OPTIONS;
}

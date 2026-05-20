import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  LucideAngularModule,
  Shield,
  ShieldAlert,
  Eye,
  HardDrive,
  Plus,
  CheckCircle,
  Settings,
  Activity,
  Calendar,
  Download,
  Upload,
  Lock,
  User,
  FileText,
  ClipboardList,
  Bell,
  Edit2,
  Trash2,
  MoreHorizontal,
  Users
} from 'lucide-angular';

@Component({
  selector: 'app-scopes-management',
  standalone: true,
  imports: [CommonModule, LucideAngularModule],
  templateUrl: './scopes.html',
})
export class Scopes {
  // Ícones dos Cards de Escopo
  readonly Shield = Shield;
  readonly ShieldAlert = ShieldAlert;
  readonly Eye = Eye;
  readonly HardDrive = HardDrive;
  
  // Ícones do Header
  readonly Plus = Plus;
  readonly Users = Users;
  
  // Ícones das Permissões
  readonly CheckCircle = CheckCircle;
  readonly Settings = Settings;
  readonly Activity = Activity;
  readonly Calendar = Calendar;
  readonly Download = Download;
  readonly Upload = Upload;
  readonly Lock = Lock;
  readonly User = User;
  readonly FileText = FileText;
  readonly ClipboardList = ClipboardList;
  readonly Bell = Bell;
  
  // Ícones da Tabela
  readonly Edit2 = Edit2;
  readonly Trash2 = Trash2;
  readonly MoreHorizontal = MoreHorizontal;
}
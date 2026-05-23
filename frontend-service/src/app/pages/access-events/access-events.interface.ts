import { Type } from "@angular/core"

export interface AccessEvents {
    id: string;
    plate: string;
    direction: string;
    status: 'Granted' | 'Denied';
    date: string;
    icon: Type<any>;
}
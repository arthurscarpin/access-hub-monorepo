import { Scope } from "../../../../core/models/scope.model";
import { User } from "../../../../core/models/users.model";

export interface UserConfig extends Omit<User, 'scopes'> {
  scopes: Scope[];
}
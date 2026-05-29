import { Scope } from "../../../../core/models/scope.model";
import { User } from "../../../../core/models/user.model";

export interface UserConfig extends Omit<User, 'scopes'> {
  scopes: Scope[];
}
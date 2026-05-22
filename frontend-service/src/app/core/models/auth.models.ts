import { User } from "@core/models/user.models";

export interface LoginResponse {
  accessToken: string;
  user: User;
}
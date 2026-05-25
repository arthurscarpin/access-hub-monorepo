export interface User {
  name: string;
  email: string;
}

export interface AuthResponse {
  accessToken: string;
  user: User;
}
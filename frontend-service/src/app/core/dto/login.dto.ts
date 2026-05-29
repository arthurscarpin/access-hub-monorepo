export type LoginRequest = {
  email: string;
  password: string;
  rememberMe: boolean;
};

export type LoginResponse = {
  accessToken: string;
  expiresIn: number;
}

export type LoginInfoResponse = {
  email: string;
  username: string
}
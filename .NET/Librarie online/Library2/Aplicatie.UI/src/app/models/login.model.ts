export interface LoginModel {
  userId?: number;
  userName: string;
  password: string;
  role?: string;
  result?: boolean;
  message?: string;
}

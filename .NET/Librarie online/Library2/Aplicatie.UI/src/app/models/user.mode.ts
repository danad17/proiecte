export interface User {
  userId?: number;
  userName: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  createdDate?: Date;
  role?: string;
}

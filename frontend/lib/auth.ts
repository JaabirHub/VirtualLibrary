import { AuthResponse, User } from "@/types";
import api from "./api";

export const register = async (
  email: string,
  username: string,
  password: string
): Promise<AuthResponse> => {
  const response = await api.post<AuthResponse>("api/auth/register", {
    email,
    username,
    password,
  });
  return response.data;
}

export const login = async (
  email: string,
  password: string
): Promise<AuthResponse> => {
  const response = await api.post<AuthResponse>("api/auth/login", {
    email,
    password,
  });
  return response.data;
}

export const getMe = async (): Promise<User> => {
  const response = await api.get<User>("api/auth/me");
  return response.data;
}
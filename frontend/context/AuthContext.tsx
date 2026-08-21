import { login as loginApi, register as registerApi, getMe } from "@/lib/auth";
import { AuthResponse, User } from "@/types";
import { createContext, useContext, useEffect, useState } from "react";

interface AuthContextType {
  user: User | null;
  token: string | null;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (email: string, username: string, password: string) => Promise<void>;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children}: {children: React.ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [token, setToken] = useState<string | null>(() => {
    if (typeof window !== "undefined") {
      return localStorage.getItem("token");
    }
    return null;
  });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    if (token) {
      getMe()
        .then((user) => setUser(user))
        .catch(() => {
          localStorage.removeItem("token");
          setToken(null);
        })
        .finally(() => setIsLoading(false));
    } else {
      setIsLoading(false);
    }
  }, []);

  const login = async (email: string, password: string) => {
    const response: AuthResponse = await loginApi(email, password);
    localStorage.setItem("token", response.token);
    setToken(response.token);
    setUser(response.user);
  };

  const register = async (email: string, username: string, password: string) => {
    const response: AuthResponse = await registerApi(email, username, password);
    localStorage.setItem("token", response.token);
    setToken(response.token);
    setUser(response.user);
  };

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        token,
        isLoading,
        login,
        register,
        logout,
        isAuthenticated: !!token && !!user,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
}
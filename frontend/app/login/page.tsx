"use client";

import { useAuth } from "@/context/AuthContext";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function LoginPage() {
  const {login} = useAuth();
  const router = useRouter();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e:React.SubmitEvent) => {
    e.preventDefault();
    setError(null);
    setIsLoading(true);
    try {
      await login(email, password);
      router.push("/library");
    } catch {
      setError("Invalid email or password");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="bg-white p-8 rounded-xl border border-gray-200 w-full max-w-md">
        <h1 className="text-2xl font-bold text-gray-900 mb-6">Login</h1>

        {error && (
          <p className="text-red-500 text-sm mb-4 p-3 bg-red-50 rounded-lg">
            {error}
          </p>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Email
            </label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900"
            />
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Password
            </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full border border-gray-300 rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-gray-900"
            />
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-gray-900 text-white py-2 rounded-lg text-sm hover:bg-gray-700 transition-colors disabled:opacity-50"
          >
            {isLoading ? "Logging in..." : "Login"}
          </button>
        </form>

        <p className="text-sm text-gray-500 mt-4 text-center">
          Don&apos;t have an account?{" "}
          <Link href="/register" className="text-gray-900 font-medium hover:underline">
            Register
          </Link>
        </p>
      </div>
    </div>
  ); 
}
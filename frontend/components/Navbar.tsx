"use client";

import { useAuth } from "@/context/AuthContext";
import Link from "next/link";
import { useRouter } from "next/navigation";

export default function Navbar() {
  const { user, isAuthenticated, logout } = useAuth();
  const router = useRouter();

  const handleLogout = (): void => {
    logout();
    router.push("/login");
  };
  return (
    <nav className="bg-white border-b border-gray-200 px-6 py-4">
      <div className="max-w-6xl mx-auto flex items-center justify-between">
        <Link href="/" className="text-xl font-bold text-gray-900">
          Virtual Library
        </Link>

        <div className="flex items-center gap-6">
          {isAuthenticated ? (
            <>
              <Link
                href="/search"
                className="text-gray-600 hover:text-gray-900 transition-colors"
              >
                Search
              </Link>
              <Link
                href="/library"
                className="text-gray-600 hover:text-gray-900 transition-colors"
              >
                My Library
              </Link>
              <span className="text-gray-500 text-sm">
                {user?.username}
              </span>
              <button
                onClick={handleLogout}
                className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm hover:bg-gray-700 transition-colors"
              >
                Logout
              </button>
            </>
          ) : (
            <>
              <Link
                href="/login"
                className="text-gray-600 hover:text-gray-900 transition-colors"
              >
                Login
              </Link>
              <Link
                href="/register"
                className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm hover:bg-gray-700 transition-colors"
              >
                Register
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
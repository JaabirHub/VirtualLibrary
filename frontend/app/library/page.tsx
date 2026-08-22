"use client";

export const dynamic = "force-dynamic";

import { useEffect, useState } from "react";
import { UserBook } from "@/types";
import { getLibrary } from "@/lib/library";
import Navbar from "@/components/Navbar";
import LibraryBookCard from "@/components/LibraryBookCard";
import ProtectedRoute from "@/components/ProtectedRoute";
import Link from "next/link";

export default function LibraryPage() {
  const [userBooks, setUserBooks] = useState<UserBook[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getLibrary()
      .then((books) => setUserBooks(books))
      .catch(() => setError("Failed to load library"))
      .finally(() => setIsLoading(false));
  }, []);

  const handleRemove = (googleVolumeId: string) => {
    setUserBooks((prev) =>
      prev.filter((ub) => ub.book.googleVolumeId !== googleVolumeId)
    );
  };

  const handleUpdate = (updatedBook: UserBook) => {
    setUserBooks((prev) =>
      prev.map((ub) => (ub.id === updatedBook.id ? updatedBook : ub))
    );
  };

  return (
    <ProtectedRoute>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <main className="max-w-6xl mx-auto px-6 py-8">
          <div className="flex items-center justify-between mb-6">
            <h1 className="text-2xl font-bold text-gray-900">My Library</h1>
            <Link
              href="/search"
              className="bg-gray-900 text-white px-4 py-2 rounded-lg text-sm hover:bg-gray-700 transition-colors"
            >
              Add Books
            </Link>
          </div>

          {isLoading && (
            <p className="text-gray-500 text-sm text-center mt-8">
              Loading library...
            </p>
          )}

          {error && (
            <p className="text-red-500 text-sm text-center mt-8">{error}</p>
          )}

          {!isLoading && !error && userBooks.length === 0 && (
            <div className="text-center mt-16">
              <p className="text-gray-500 mb-4">Your library is empty.</p>
              <Link
                href="/search"
                className="bg-gray-900 text-white px-6 py-2 rounded-lg text-sm hover:bg-gray-700 transition-colors"
              >
                Search for books
              </Link>
            </div>
          )}

          {!isLoading && userBooks.length > 0 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {userBooks.map((userBook) => (
                <LibraryBookCard
                  key={userBook.id}
                  userBook={userBook}
                  onRemove={handleRemove}
                  onUpdate={handleUpdate}
                />
              ))}
            </div>
          )}
        </main>
      </div>
    </ProtectedRoute>
  );
}
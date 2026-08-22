"use client";

import { useState } from "react";
import { Book } from "@/types";
import { searchBooks } from "@/lib/books";
import Navbar from "@/components/Navbar";
import SearchBar from "@/components/SearchBar";
import BookCard from "@/components/BookCard";
import ProtectedRoute from "@/components/ProtectedRoute";

export default function SearchPage() {
  const [books, setBooks] = useState<Book[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searched, setSearched] = useState(false);

  const handleSearch = async (query: string) => {
    setIsLoading(true);
    setError(null);
    setSearched(true);
    try {
      const results = await searchBooks(query);
      setBooks(results);
    } catch {
      setError("Failed to search books. Please try again.");
      setBooks([]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <ProtectedRoute>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <main className="max-w-6xl mx-auto px-6 py-8">
          <h1 className="text-2xl font-bold text-gray-900 mb-6">
            Search Books
          </h1>

          <SearchBar onSearch={handleSearch} isLoading={isLoading} />

          {error && (
            <p className="text-red-500 text-sm mt-4">{error}</p>
          )}

          {isLoading && (
            <p className="text-gray-500 text-sm mt-8 text-center">
              Searching...
            </p>
          )}

          {!isLoading && searched && books.length === 0 && (
            <p className="text-gray-500 text-sm mt-8 text-center">
              No books found. Try a different search.
            </p>
          )}

          {!isLoading && books.length > 0 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mt-6">
              {books.map((book) => (
                <BookCard key={book.googleVolumeId} book={book} />
              ))}
            </div>
          )}
        </main>
      </div>
    </ProtectedRoute>
  );
}
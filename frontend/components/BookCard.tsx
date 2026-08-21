"use client";

import { addBook } from "@/lib/library";
import { Book, ReadingStatus } from "@/types";
import { useState } from "react";

interface BookCardProps {
  book: Book;
}

export default function BookCard({ book }: BookCardProps) {
  const [adding, setAdding] = useState(false);
  const [added, setAdded] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleAdd = async () => {
    setAdding(true);
    setError(null);
    try {
      await addBook({
        googleVolumeId: book.googleVolumeId,
        status: "WANT_TO_READ" as ReadingStatus,
      });
      setAdded(true);
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Failed to add book");
      }
    } finally {
      setAdding(false);
    }
  };

  return (
    <div className="flex gap-4 p-4 border border-gray-200 rounded-lg bg-white">
      {book.thumbnailUrl ? (
        <img
          src={book.thumbnailUrl}
          alt={book.title ?? "Book cover"}
          className="w-20 h-28 object-cover rounded flex-shrink-0"
        />
      ) : (
        <div className="w-20 h-28 bg-gray-100 rounded flex-shrink-0 flex items-center justify-center">
          <span className="text-gray-400 text-xs text-center px-1">No cover</span>
        </div>
      )}

      <div className="flex flex-col justify-between flex-1 min-w-0">
        <div>
          <h3 className="font-semibold text-gray-900 truncate">
            {book.title ?? "Unknown Title"}
          </h3>
          <p className="text-sm text-gray-500 mt-1">
            {book.authors?.join(", ") ?? "Unknown Author"}
          </p>
          {book.publishedDate && (
            <p className="text-xs text-gray-400 mt-1">{book.publishedDate}</p>
          )}
          {book.description && (
            <p className="text-sm text-gray-600 mt-2 line-clamp-2">
              {book.description}
            </p>
          )}
        </div>

        <div className="mt-3">
          {error && <p className="text-red-500 text-xs mb-2">{error}</p>}
          <button
            onClick={handleAdd}
            disabled={adding || added}
            className="bg-gray-900 text-white px-3 py-1.5 rounded text-sm hover:bg-gray-700 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {added ? "Added" : adding ? "Adding..." : "Add to Library"}
          </button>
        </div>
      </div>
    </div>
  );
}
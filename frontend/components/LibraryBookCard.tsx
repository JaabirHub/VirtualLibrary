"use client";

import { UserBook, ReadingStatus } from "@/types";
import { updateBook, removeBook } from "../lib/library";
import { useState } from "react";

interface LibraryBookCardProps {
  userBook: UserBook;
  onRemove: (googleVolumeId: string) => void;
  onUpdate: (updatedBook: UserBook) => void;
}

export default function LibraryBookCard({
  userBook,
  onRemove,
  onUpdate,
}: LibraryBookCardProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [status, setStatus] = useState<ReadingStatus>(userBook.status);
  const [rating, setRating] = useState<number | null>(userBook.rating);
  const [notes, setNotes] = useState<string>(userBook.notes ?? "");
  const [saving, setSaving] = useState(false);
  const [removing, setRemoving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSave = async () => {
    setSaving(true);
    setError(null);
    try {
      const updated = await updateBook(userBook.book.googleVolumeId, {
        status,
        rating: rating ?? undefined,
        notes,
      });
      onUpdate(updated);
      setIsEditing(false);
    } catch {
      setError("Failed to update book");
    } finally {
      setSaving(false);
    }
  };

  const handleRemove = async () => {
    setRemoving(true);
    try {
      await removeBook(userBook.book.googleVolumeId);
      onRemove(userBook.book.googleVolumeId);
    } catch {
      setError("Failed to remove book");
      setRemoving(false);
    }
  };

  const statusLabels: Record<ReadingStatus, string> = {
    WANT_TO_READ: "Want to Read",
    CURRENTLY_READING: "Currently Reading",
    READ: "Read",
  };

  return (
    <div className="flex gap-4 p-4 border border-gray-200 rounded-lg bg-white">
      {userBook.book.thumbnailUrl ? (
        <img
          src={userBook.book.thumbnailUrl}
          alt={userBook.book.title ?? "Book cover"}
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
            {userBook.book.title ?? "Unknown Title"}
          </h3>
          <p className="text-sm text-gray-500 mt-1">
            {userBook.book.authors?.join(", ") ?? "Unknown Author"}
          </p>

          {!isEditing ? (
            <div className="mt-2 space-y-1">
              <p className="text-xs text-gray-500">
                Status:{" "}
                <span className="font-medium text-gray-700">
                  {statusLabels[userBook.status]}
                </span>
              </p>
              {userBook.rating && (
                <p className="text-xs text-gray-500">
                  Rating:{" "}
                  <span className="font-medium text-gray-700">
                    {userBook.rating}/5
                  </span>
                </p>
              )}
              {userBook.notes && (
                <p className="text-xs text-gray-600 mt-1">{userBook.notes}</p>
              )}
            </div>
          ) : (
            <div className="mt-2 space-y-2">
              <select
                value={status}
                onChange={(e) => setStatus(e.target.value as ReadingStatus)}
                className="w-full text-sm border border-gray-300 rounded px-2 py-1"
              >
                <option value="WANT_TO_READ">Want to Read</option>
                <option value="CURRENTLY_READING">Currently Reading</option>
                <option value="READ">Read</option>
              </select>

              <select
                value={rating ?? ""}
                onChange={(e) =>
                  setRating(e.target.value ? Number(e.target.value) : null)
                }
                className="w-full text-sm border border-gray-300 rounded px-2 py-1"
              >
                <option value="">No rating</option>
                {[1, 2, 3, 4, 5].map((r) => (
                  <option key={r} value={r}>
                    {r}/5
                  </option>
                ))}
              </select>

              <textarea
                value={notes}
                onChange={(e) => setNotes(e.target.value)}
                placeholder="Add notes..."
                className="w-full text-sm border border-gray-300 rounded px-2 py-1 resize-none"
                rows={2}
              />
            </div>
          )}
        </div>

        {error && <p className="text-red-500 text-xs mt-1">{error}</p>}

        <div className="flex gap-2 mt-3">
          {!isEditing ? (
            <>
              <button
                onClick={() => setIsEditing(true)}
                className="bg-gray-900 text-white px-3 py-1.5 rounded text-sm hover:bg-gray-700 transition-colors"
              >
                Edit
              </button>
              <button
                onClick={handleRemove}
                disabled={removing}
                className="border border-gray-300 text-gray-600 px-3 py-1.5 rounded text-sm hover:bg-gray-50 transition-colors disabled:opacity-50"
              >
                {removing ? "Removing..." : "Remove"}
              </button>
            </>
          ) : (
            <>
              <button
                onClick={handleSave}
                disabled={saving}
                className="bg-gray-900 text-white px-3 py-1.5 rounded text-sm hover:bg-gray-700 transition-colors disabled:opacity-50"
              >
                {saving ? "Saving..." : "Save"}
              </button>
              <button
                onClick={() => setIsEditing(false)}
                className="border border-gray-300 text-gray-600 px-3 py-1.5 rounded text-sm hover:bg-gray-50 transition-colors"
              >
                Cancel
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}
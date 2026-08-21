export interface User {
  id: string;
  email: string;
  username: string;
  role: "USER"|"ADMIN";
  createdAt: string;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface Book {
  id?: string;
  googleVolumeId: string;
  title: string;
  authors: string[] | null;
  publisher: string | null;
  publishedDate: string | null;
  description: string | null;
  thumbnailUrl: string | null;
  pageCount: number | null;
  categories: string[] | null;
  language: string | null;
  infoLink: string | null;
}

export type ReadingStatus = "WANT_TO_READ" | "CURRENTLY_READING" | "READ";

export interface UserBook {
  id: string;
  book: Book;
  status: ReadingStatus;
  rating: number | null;
  notes: string | null;
  createdAt: string;
  updatedAt: string;
}

export interface UserBookRequest {
  googleVolumeId: string;
  status?: ReadingStatus;
  rating?: number;
  notes?: string;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  details?: Record<string, string>;
}
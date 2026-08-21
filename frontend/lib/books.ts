import { Book } from "@/types";
import api from "./api";

export const searchBooks = async (
  query: string,
  page: number = 0,
  size: number = 10 
): Promise<Book[]> => {
  const response = await api.get<Book[]>("api/books/search", {
    params: {query, page, size},
  });
  return response.data;
}
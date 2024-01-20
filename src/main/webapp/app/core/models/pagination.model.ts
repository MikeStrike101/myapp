export interface Pagination<T> {
  content: T[];
  totalItems: number;
  totalPages: number;
  currentPage: number;
}

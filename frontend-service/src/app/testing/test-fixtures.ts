import { Paginated } from '../core/models/paginated.model';

export function paginatedFixture<T>(
  content: T[],
  overrides: Partial<Paginated<T>> = {},
): Paginated<T> {
  return {
    content,
    empty: content.length === 0,
    first: true,
    last: true,
    number: 0,
    numberOfElements: content.length,
    size: content.length,
    totalElements: content.length,
    totalPages: content.length ? 1 : 0,
    ...overrides,
  };
}

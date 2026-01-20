package dev.danvega.runnerz.run;

import java.util.List;

/**
 * A generic response wrapper for paginated data.
 *
 * @param <T> the type of elements in the paginated content
 * @param content the list of elements for the current page
 * @param page the current page number (zero-based)
 * @param size the number of elements per page
 * @param totalElements the total number of elements across all pages
 * @param totalPages the total number of pages
 */
public record PagedResponse<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages
) {
    /**
     * Constructs a PagedResponse with automatic calculation of total pages.
     *
     * @param content the list of elements for the current page
     * @param page the current page number (zero-based)
     * @param size the number of elements per page
     * @param totalElements the total number of elements across all pages
     */
    public PagedResponse(List<T> content, int page, int size, long totalElements) {
        this(content, page, size, totalElements, (int) Math.ceil((double) totalElements / size));
    }
}

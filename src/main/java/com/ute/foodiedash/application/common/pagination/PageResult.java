package com.ute.foodiedash.application.common.pagination;

import lombok.Getter;

import java.util.List;

@Getter
public class PageResult<T> {
    private final List<T> content;

    private final int pageNumber;

    private final int pageSize;

    private final long totalElements;

    private final int totalPages;

    public PageResult(List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }


    public boolean isEmpty() {
        return content.isEmpty();
    }
}

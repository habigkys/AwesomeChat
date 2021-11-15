package com.awesome.infrastructures.page;

public interface PageResponse {
    int getTotalPageSize();

    boolean hasNext();

    boolean hasPrev();

    int getPage();

    int getPageSize();

    long getTotalCount();
}

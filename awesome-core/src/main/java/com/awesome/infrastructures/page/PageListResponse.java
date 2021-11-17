package com.awesome.infrastructures.page;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
@Accessors(chain = true)
public class PageListResponse<T> implements PageResponse {
    private int totalPageSize;
    private long totalCount;
    private boolean next;
    private boolean prev;
    private int page;
    private int pageSize;
    private List<T> contents;

    public static PageListResponse create() {
        return new PageListResponse();
    }

    /**
     * pageable를 이용해서 기본 정보 세팅
     *
     * @param pageable
     * @return
     */
    public static PageListResponse create(Pageable pageable) {
        PageListResponse pageListResponse = create();

        // 요청시 설정으로 자동 -1 이 되므로, 응답에서 보정해줌.
        pageListResponse.setPage(pageable.getPageNumber() + 1);
        pageListResponse.setPageSize(pageable.getPageSize());
        pageListResponse.setContents(Lists.newArrayList());

        boolean hasPrev = (pageable.getPageNumber() >= 1) ? true : false;
        pageListResponse.setPrev(hasPrev);
        pageListResponse.setNext(false);
        return pageListResponse;
    }

    public static <T> PageListResponse<T> create(Pageable pageable, Page<T> result) {
        PageListResponse pageListResponse = create(pageable);

        pageListResponse.setPrev(result.hasPrevious());
        pageListResponse.setNext(result.hasNext());
        pageListResponse.setTotalCount(result.getTotalElements());
        pageListResponse.setTotalPageSize(result.getTotalPages());

        pageListResponse.setContents(result.getContent());

        return pageListResponse;
    }


    public static <T, R> PageListResponse<R> create(Pageable pageable, Page<T> result, Function<? super T, ? extends R> mapper) {
        PageListResponse pageListResponse = create(pageable, result);
        pageListResponse.setContents(result.getContent().stream().map(mapper).collect(Collectors.toList()));
        return pageListResponse;
    }


    public static <T> PageListResponse<T> create(Pageable pageable, Page pageResult, List<T> contents) {
        PageListResponse pageListResponse = create(pageable, pageResult);
        pageListResponse.setContents(contents);
        return pageListResponse;
    }

    public void forEach(Consumer<? super T> consumer) {
        contents.stream().forEach(consumer);
    }

    public <R> PageListResponse<R> map(Function<? super T, ? extends R> mapper) {

        PageListResponse pageListResponse = create();
        pageListResponse.setPage(getPage());
        pageListResponse.setPageSize(getPageSize());
        pageListResponse.setPrev(isPrev());
        pageListResponse.setNext(isNext());
        pageListResponse.setTotalCount(getTotalCount());
        pageListResponse.setTotalPageSize(getTotalPageSize());

        pageListResponse.setContents(contents.stream().map(mapper).collect(Collectors.toList()));
        return pageListResponse;
    }

    public <T> PageListResponse<T> convert(List<T> convertedContents) {
        PageListResponse pageListResponse = create();
        pageListResponse.setPage(getPage());
        pageListResponse.setPageSize(getPageSize());
        pageListResponse.setPrev(isPrev());
        pageListResponse.setNext(isNext());
        pageListResponse.setTotalCount(getTotalCount());
        pageListResponse.setTotalPageSize(getTotalPageSize());

        pageListResponse.setContents(convertedContents);
        return pageListResponse;
    }


    @Override
    public int getTotalPageSize() {
        return totalPageSize;
    }

    @Override
    public long getTotalCount() {
        return totalCount;
    }

    @Override
    public boolean hasNext() {
        return next;
    }

    @Override
    public boolean hasPrev() {
        return prev;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

}

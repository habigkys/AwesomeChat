package com.awesome.domains.chatroom.entities.vos;

import com.awesome.infrastructures.page.PageResponse;
import lombok.Data;

@Data
public class ChatRoomPageInfoVO implements PageResponse {
    private int totalPageSize;
    private long totalCount;
    private boolean next;
    private boolean prev;
    private int page;
    private int pageSize;

    @Override
    public boolean hasNext() {
        return next;
    }

    @Override
    public boolean hasPrev() {
        return prev;
    }
}
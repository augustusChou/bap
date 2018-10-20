package com.github.bap.event.handler.controller.dto.response;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 周广
 **/
@Data
public class PageResponse<T> {


    /**
     * 总记录数
     */
    private long total;

    private List<T> data;

    public PageResponse(long total, List<T> data) {
        this.total = total;
        this.data = data;
    }

    public static <T> PageResponse<T> of(long total, List<T> data) {
        return new PageResponse<>(total, data);
    }


    public static Pageable createPageable(int pageNo, int pageSize) {
        return PageRequest.of(pageNo == 0 ? pageNo : pageNo - 1, pageSize);
    }
}

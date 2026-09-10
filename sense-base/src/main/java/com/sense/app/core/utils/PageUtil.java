package com.sense.app.core.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.HttpHeaders;

/**
 * @ClassName PageUtil
 * @description 分页工具
 * @author sense-x
 * @date 2026-01-01 23:00:00
 */
public class PageUtil {

    public static <T> Page<T> getPage(int page, int pageSize) {
        return new Page((long)page, (long)pageSize);
    }

    public static HttpHeaders getTotalHeader(Page<?> page) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Total-Count", "" + page.getTotal());
        return headers;
    }

}

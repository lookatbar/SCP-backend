package com.lookatbar.scp.basicdata.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 通用分页响应DTO
 * 用于封装分页查询结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {

    /**
     * 数据列表
     */
    private List<T> data;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 当前页码
     */
    private Integer page;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Integer totalPages;

    /**
     * 创建分页响应
     *
     * @param data     数据列表
     * @param total    总记录数
     * @param page     当前页码
     * @param pageSize 每页条数
     * @return 分页响应DTO
     */
    public static <T> PageResponseDTO<T> of(List<T> data, Long total, Integer page, Integer pageSize) {
        int totalPages = (int) Math.ceil((double) total / pageSize);
        return PageResponseDTO.<T>builder()
                .data(data)
                .total(total)
                .page(page)
                .pageSize(pageSize)
                .totalPages(totalPages)
                .build();
    }
}
package com.lookatbar.scp.basicdata.application.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 分页查询基类
 * 所有分页查询DTO应继承此类
 * 提供通用分页参数：page、pageSize
 */
@Data
@SuperBuilder
public abstract class BasePageQuery {

    /**
     * 当前页码（从1开始）
     */
    @Parameter(description = "当前页码（从1开始）")
    private Integer page = 1;

    /**
     * 每页条数
     */
    @Parameter(description = "每页条数")
    private Integer pageSize = 10;

    /**
     * 排序字段（如：createTime、updateTime）
     */
    @Parameter(description = "排序字段")
    private String sortField;

    /**
     * 排序方向（asc/desc）
     */
    @Parameter(description = "排序方向：asc或desc")
    private String sortOrder = "desc";

    /**
     * 开始时间（用于时间范围查询）
     */
    @Parameter(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 结束时间（用于时间范围查询）
     */
    @Parameter(description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 获取分页偏移量
     * @return 偏移量
     */
    public int getOffset() {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        return (page - 1) * pageSize;
    }

    /**
     * 校验分页参数
     */
    public void validate() {
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        if (pageSize > 100) {
            pageSize = 100;
        }
    }
}
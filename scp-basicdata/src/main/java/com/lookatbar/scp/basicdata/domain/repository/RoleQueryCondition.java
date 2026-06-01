package com.lookatbar.scp.basicdata.domain.repository;

/**
 * 角色查询条件
 * 用于条件过滤和分页查询
 */
public class RoleQueryCondition {
    private String name;
    private String code;
    private Integer status;
    private Integer offset;
    private Integer limit;

    public RoleQueryCondition() {}

    public RoleQueryCondition(String name, String code, Integer status, Integer offset, Integer limit) {
        this.name = name;
        this.code = code;
        this.status = status;
        this.offset = offset;
        this.limit = limit;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getOffset() { return offset; }
    public void setOffset(Integer offset) { this.offset = offset; }
    public Integer getLimit() { return limit; }
    public void setLimit(Integer limit) { this.limit = limit; }
}
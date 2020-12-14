package com.ai.generator.page;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页请求
 * @author Louis
 * @date Aug 19, 2018
 */
public class PageRequest {
	/**
	 * 当前页码
	 */
	private int pageNum = 1;
	/**
	 * 每页数量
	 */
	private int pageSize = 10;

	/**
	 * 过滤字段
	 */
	private Map<String, Object> filters = new HashMap<String, Object>();
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, Object> getFilters() {
		return filters;
	}

	public void setFilters(Map<String, Object> filters) {
		this.filters = filters;
	}
}

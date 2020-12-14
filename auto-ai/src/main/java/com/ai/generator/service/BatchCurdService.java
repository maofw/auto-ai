package com.ai.generator.service;

import java.util.List;


/**
 * 通用CURD接口
 */
public interface BatchCurdService<T> extends CurdService<T>{

	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	int insertBatch(List<T> list);


}
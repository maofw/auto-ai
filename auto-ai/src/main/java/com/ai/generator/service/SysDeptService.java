package com.ai.generator.service;


import com.ai.generator.model.SysDept;

import java.util.List;

/**
 * 机构管理
 * @author Louis
 * @date Oct 29, 2018
 */
public interface SysDeptService extends CurdService<SysDept> {

	/**
	 * 查询机构树
	 * @return
	 */
	List<SysDept> findTree();

	SysDept findByName(String name);
}

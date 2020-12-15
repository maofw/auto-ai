package com.ai.generator.controller;

import com.ai.generator.http.HttpResult;
import com.ai.generator.model.SysDept;
import com.ai.generator.service.SysDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 机构控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("dept")
public class SysDeptController {

	@Autowired
	private SysDeptService sysDeptService;
	
	@PreAuthorize("hasAuthority('sys:dept:add') AND hasAuthority('sys:dept:edit')")
	@PostMapping(value="/save")
	public HttpResult<Long> save(@RequestBody SysDept record) {
		int res = sysDeptService.save(record);
		if(res > 0){
			return HttpResult.success(record.getId());
		}
		return HttpResult.error("保存失败");
	}

	@PreAuthorize("hasAuthority('sys:dept:delete')")
	@PostMapping(value="/delete")
	public HttpResult<Integer> delete(@RequestBody List<SysDept> records) {
		return HttpResult.success(sysDeptService.deleteBatch(records));
	}

	@PreAuthorize("hasAuthority('sys:dept:view')")
	@GetMapping(value="/findTree")
	public HttpResult<List<SysDept>>  findTree() {
		return HttpResult.success(sysDeptService.findTree());
	}

}

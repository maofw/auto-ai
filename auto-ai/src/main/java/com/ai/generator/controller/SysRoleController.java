package com.ai.generator.controller;

import com.ai.generator.http.HttpResult;
import com.ai.generator.model.SysRole;
import com.ai.generator.model.SysRoleMenu;
import com.ai.generator.page.PageRequest;
import com.ai.generator.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("role")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;
	
	@PreAuthorize("hasAuthority('sys:role:add') AND hasAuthority('sys:role:edit')")
	@PostMapping(value="/save")
	public HttpResult<Long> save(@RequestBody SysRole record) {
		int res =  sysRoleService.save(record);
		if(res > 0){
			return HttpResult.success(record.getId());
		}
		return HttpResult.error("保存失败");
	}

	@PreAuthorize("hasAuthority('sys:role:delete')")
	@PostMapping(value="/delete")
	public HttpResult<Integer> delete(@RequestBody List<SysRole> records) {
		return HttpResult.success(sysRoleService.deleteBatch(records));
	}

	@PreAuthorize("hasAuthority('sys:role:view')")
	@PostMapping(value="/findPage")
	public HttpResult findPage(@RequestBody PageRequest pageRequest) {
		return HttpResult.success(sysRoleService.findPage(pageRequest));
	}
	
	@PreAuthorize("hasAuthority('sys:role:view')")
	@GetMapping(value="/findAll")
	public HttpResult findAll() {
		return HttpResult.success(sysRoleService.findAll());
	}
	
//	@PreAuthorize("hasAuthority('sys:role:view')")
	@GetMapping(value="/findRoleMenus")
	public HttpResult findRoleMenus(@RequestParam Long roleId) {
		return HttpResult.success(sysRoleService.findRoleMenus(roleId));
	}
	
	@PreAuthorize("hasAuthority('sys:role:view')")
	@PostMapping(value="/saveRoleMenus")
	public HttpResult saveRoleMenus(@RequestBody List<SysRoleMenu> records) {
		return HttpResult.success(sysRoleService.saveRoleMenus(records));
	}
}

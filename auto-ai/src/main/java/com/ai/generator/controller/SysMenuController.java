package com.ai.generator.controller;

import com.ai.generator.http.HttpResult;
import com.ai.generator.model.SysMenu;
import com.ai.generator.service.SysMenuService;
import com.ai.generator.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("menu")
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;

	@PreAuthorize("hasAuthority('sys:menu:add') AND hasAuthority('sys:menu:edit')")
	@PostMapping(value="/save")
	public HttpResult<Integer> save(@RequestBody SysMenu record) {
		return HttpResult.success(sysMenuService.save(record));
	}

	@PreAuthorize("hasAuthority('sys:menu:delete')")
	@PostMapping(value="/delete")
	public HttpResult<Integer> delete(@RequestBody List<SysMenu> records) {
		return HttpResult.success(sysMenuService.deleteBatch(records));
	}

//	@PreAuthorize("hasAuthority('sys:menu:view')")
	@GetMapping(value="/findNavTree")
	public HttpResult<List<SysMenu>> findNavTree( ) {
		String userName = SecurityUtils.getUsername() ;
		return HttpResult.success(sysMenuService.findTree(userName,1));
	}
	
//	@PreAuthorize("hasAuthority('sys:menu:view')")
	@GetMapping(value="/findMenuTree")
	public HttpResult<List<SysMenu>> findMenuTree() {
		return HttpResult.success(sysMenuService.findTree(null,0));
	}
}

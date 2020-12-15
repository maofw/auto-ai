package com.ai.generator.controller;

import com.ai.generator.consts.SysConstants;
import com.ai.generator.http.HttpResult;
import com.ai.generator.model.SysUser;
import com.ai.generator.page.PageRequest;
import com.ai.generator.service.SysUserService;
import com.ai.generator.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户控制器
 *
 * @author Louis
 * @date Oct 29, 2018
 */
@RestController
@RequestMapping("user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @PreAuthorize("hasAuthority('sys:user:add') AND hasAuthority('sys:user:edit')")
    @PostMapping(value = "/save")
    public HttpResult<Long> save(@RequestBody SysUser record) {
        SysUser user = null;
        if(record.getId() != null){
            user = sysUserService.findById(record.getId());
        }
        if (user != null) {
            if (SysConstants.ADMIN.equalsIgnoreCase(user.getName())) {
                return HttpResult.error("超级管理员不允许修改!");
            }
        }
        if (record.getPassword() != null) {
            String salt = PasswordUtils.getSalt();
            if (user == null) {
                // 新增用户
                if (sysUserService.findByName(record.getName()) != null) {
                    return HttpResult.error("用户名已存在!");
                }
                String password = PasswordUtils.encode(record.getPassword(), salt);
                record.setSalt(salt);
                record.setPassword(password);
            } else {
                // 修改用户, 且修改了密码
                if (!record.getPassword().equals(user.getPassword())) {
                    String password = PasswordUtils.encode(record.getPassword(), salt);
                    record.setSalt(salt);
                    record.setPassword(password);
                }
            }
        }
        int res = sysUserService.save(record);
        if(res > 0){
            return HttpResult.success(record.getId());
        }
        return HttpResult.error("保存失败");
    }

    @PreAuthorize("hasAuthority('sys:user:delete')")
    @PostMapping(value = "/delete")
    public HttpResult delete(@RequestBody List<SysUser> records) {
        for (SysUser record : records) {
            SysUser sysUser = sysUserService.findById(record.getId());
            if (sysUser != null && SysConstants.ADMIN.equalsIgnoreCase(sysUser.getName())) {
                return HttpResult.error("超级管理员不允许删除!");
            }
        }
        return HttpResult.success(sysUserService.deleteBatch(records));
    }

//    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findByName")
    public HttpResult findByName(@RequestParam String name) {
        return HttpResult.success(sysUserService.findByName(name));
    }

//    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findPermissions")
    public HttpResult findPermissions(@RequestParam String name) {
        return HttpResult.success(sysUserService.findPermissions(name));
    }

//    @PreAuthorize("hasAuthority('sys:user:view')")
    @GetMapping(value = "/findUserRoles")
    public HttpResult findUserRoles(@RequestParam Long userId) {
        return HttpResult.success(sysUserService.findUserRoles(userId));
    }

    @PreAuthorize("hasAuthority('sys:user:view')")
    @PostMapping(value = "/findPage")
    public HttpResult findPage(@RequestBody PageRequest pageRequest) {
        return HttpResult.success(sysUserService.findPage(pageRequest));
    }

    @PostMapping(value = "/changePassword")
    public HttpResult<Integer> changePassword(@RequestBody SysUser record) {
        SysUser user = sysUserService.findById(record.getId());
        if (null == user) {
            return HttpResult.error("用户不存在!");
        }
        if (user != null) {
            if (SysConstants.ADMIN.equalsIgnoreCase(user.getName())) {
                return HttpResult.error("超级管理员不允许修改密码!");
            }
        }
        String oldSalt = user.getSalt();
        if (!PasswordUtils.encode(record.getOldPassword(), oldSalt).equals(user.getPassword())) {
            return HttpResult.error("原密码不正确!");
        }
        String salt = PasswordUtils.getSalt();
        String password = PasswordUtils.encode(record.getPassword(), salt);
        record.setSalt(salt);
        record.setPassword(password);
        return HttpResult.success(sysUserService.updateUser(record));
    }

    @GetMapping(value = "/findById")
    public HttpResult<SysUser> findById(@RequestParam(required = false) Long id) {
        return HttpResult.success(sysUserService.findById(id));
    }
}

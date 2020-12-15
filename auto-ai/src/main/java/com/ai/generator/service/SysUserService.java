package com.ai.generator.service;


import com.ai.generator.model.SysUser;
import com.ai.generator.model.SysUserRole;

import java.util.List;
import java.util.Set;

/**
 * 用户管理
 *
 * @author Louis
 * @date Oct 29, 2018
 */
public interface SysUserService extends CurdService<SysUser> {

    SysUser findByName(String username);

    /**
     * 查找用户的菜单权限标识集合
     *
     * @param userName
     * @return
     */
    Set<String> findPermissions(String userName);

    /**
     * 查找用户的角色集合
     *
     * @param userId
     * @return
     */
    List<SysUserRole> findUserRoles(Long userId);

    /**
     * 更新用户
     *
     * @param record
     * @return
     */
    int updateUser(SysUser record);

}

package com.ai.generator.service.impl;

import com.ai.generator.mapper.SysRoleMapper;
import com.ai.generator.mapper.SysUserMapper;
import com.ai.generator.mapper.SysUserRoleMapper;
import com.ai.generator.model.SysMenu;
import com.ai.generator.model.SysRole;
import com.ai.generator.model.SysUser;
import com.ai.generator.model.SysUserRole;
import com.ai.generator.page.MybatisPageHelper;
import com.ai.generator.page.PageRequest;
import com.ai.generator.page.PageResult;
import com.ai.generator.service.SysMenuService;
import com.ai.generator.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;
    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Transactional
    @Override
    public int save(SysUser record) {
        if (record.getId() == null || record.getId() == 0) {
            // 新增用户
            sysUserMapper.insertSelective(record);
        } else {
            // 更新用户信息
            sysUserMapper.updateByPrimaryKeySelective(record);
        }
        sysUserRoleMapper.deleteByUserId(record.getId());
        if(record.getUserRoles()!=null && record.getUserRoles().size()>0){
            for (SysUserRole sysUserRole : record.getUserRoles()) {
                sysUserRole.setUserId(record.getId());
                sysUserRoleMapper.insertSelective(sysUserRole);
            }
        }
        return 1;
    }

    @Override
    public int delete(SysUser record) {
        return sysUserMapper.deleteByPrimaryKey(record.getId());
    }

    @Override
    public int deleteBatch(List<SysUser> records) {
        for (SysUser record : records) {
            delete(record);
        }
        return 1;
    }

    @Override
    public SysUser findById(Long id) {
        return sysUserMapper.selectByPrimaryKey(id);
    }

    @Override
    public SysUser findByName(String name) {
        return sysUserMapper.findByName(name);
    }

    @Override
    public PageResult findPage(PageRequest pageRequest) {
        PageResult pageResult = MybatisPageHelper.findPage(pageRequest, sysUserMapper);
        // 加载用户角色信息
        findUserRoles(pageResult);
        return pageResult;
    }


    /**
     * 加载用户角色
     *
     * @param pageResult
     */
    private void findUserRoles(PageResult pageResult) {
        List<?> content = pageResult.getContent();
        for (Object object : content) {
            SysUser sysUser = (SysUser) object;
            List<SysUserRole> userRoles = findUserRoles(sysUser.getId());
            sysUser.setUserRoles(userRoles);
            sysUser.setRoleNames(getRoleNames(userRoles));
        }
    }

    private String getRoleNames(List<SysUserRole> userRoles) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<SysUserRole> iter = userRoles.iterator(); iter.hasNext(); ) {
            SysUserRole userRole = iter.next();
            SysRole sysRole = sysRoleMapper.selectByPrimaryKey(userRole.getRoleId());
            if (sysRole == null) {
                continue;
            }
            sb.append(sysRole.getRemark());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override
    public Set<String> findPermissions(String userName) {
        Set<String> perms = new HashSet<>();
        List<SysMenu> sysMenus = sysMenuService.findByUser(userName);
        for (SysMenu sysMenu : sysMenus) {
            if (sysMenu.getPerms() != null && !"".equals(sysMenu.getPerms())) {
                perms.add(sysMenu.getPerms());
            }
        }
        return perms;
    }

    @Override
    public List<SysUserRole> findUserRoles(Long userId) {
        return sysUserRoleMapper.findUserRoles(userId);
    }

    @Override
    public int updateUser(SysUser record) {
        return sysUserMapper.updateByPrimaryKeySelective(record);
    }

}

package com.jackeyj.service.impl;

import com.jackeyj.dao.RoleDao;
import com.jackeyj.entity.Role;
import com.jackeyj.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jiyaofei
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> getRoleList() {
        return roleDao.getRoleList();
    }
}

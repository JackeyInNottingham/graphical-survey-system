package com.jackeyj.service;

import com.jackeyj.entity.Role;

import java.util.List;

/**
 * @author jiyaofei
 */
public interface RoleService {

    /**
     * get system roles
     * @return roles list
     */
    List<Role> getRoleList();

}

package com.jackeyj.dao;

import com.jackeyj.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleDao {
    List<Role> getRoleList();
}
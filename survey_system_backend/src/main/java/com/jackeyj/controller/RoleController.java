package com.jackeyj.controller;

import com.jackeyj.common.Result;
import com.jackeyj.entity.Role;
import com.jackeyj.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jiyaofei
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roleList")
    public Result getRoleList(){
        List<Role> roles = roleService.getRoleList();
        if (roles == null){
            return Result.fail("role system error",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return Result.success().put("roles", roles);
    }

}

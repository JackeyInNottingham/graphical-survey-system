package com.jackeyj.entity.vo;

import lombok.Data;

/**
 * an object containing user information
 * @author jiyaofei
 */
@Data
public class UserInfoVo{

    private Integer id;

    private String username;

    private String role;

    private Boolean status;

}

package com.jackeyj.entity.vo;

import lombok.Data;

/**
 * @author jiyaofei
 */
@Data
public class OperateUserVo {

    private Integer id;

    private String username;

    private String password;

    private String email;

    private Integer roleId;

    private Boolean status;

}

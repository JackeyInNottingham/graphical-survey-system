package com.jackeyj.entity.vo;

import lombok.Data;

/**
 * @author jiyaofei
 */
@Data
public class UserVo {

    private Integer id;

    private String username;

    private String email;

    private Boolean status;

    private String role;

}

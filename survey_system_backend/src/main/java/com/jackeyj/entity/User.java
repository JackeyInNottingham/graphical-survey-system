package com.jackeyj.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * t_user
 * @author 
 */
@Data
@Accessors(chain = true)
public class User implements Serializable {
    private Integer id;

    private String username;

    private String password;

    private String email;

    private Integer roleId;

    private Boolean status;

    private static final long serialVersionUID = 1L;
}
package com.jackeyj.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * t_role
 * @author 
 */
@Data
public class Role implements Serializable {
    private Integer id;

    private String name;

    private static final long serialVersionUID = 1L;
}
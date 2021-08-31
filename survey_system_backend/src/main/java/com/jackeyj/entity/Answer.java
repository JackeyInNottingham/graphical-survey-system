package com.jackeyj.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * t_answer
 * @author 
 */
@Data
public class Answer implements Serializable {
    private Integer id;

    private Integer imageId;

    private Double x;

    private Double y;

    private static final long serialVersionUID = 1L;
}
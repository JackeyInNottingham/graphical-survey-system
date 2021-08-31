package com.jackeyj.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * t_survey
 * @author 
 */
@Data
@Accessors(chain = true)
public class Survey implements Serializable {
    private Integer id;

    private String title;

    private Integer authorId;

    private Date createTime;

    private Integer imageNumber;

    private static final long serialVersionUID = 1L;
}
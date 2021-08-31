package com.jackeyj.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * t_image
 * @author 
 */
@Data
public class Image implements Serializable {
    private Integer id;

    private Integer surveyId;

    private Integer order;

    private Double width;

    private Double height;

    private String path;

    private String description;

    private static final long serialVersionUID = 1L;
}
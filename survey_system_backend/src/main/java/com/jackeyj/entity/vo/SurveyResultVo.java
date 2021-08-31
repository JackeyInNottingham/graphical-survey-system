package com.jackeyj.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * @author jiyaofei
 */
@Data
public class SurveyResultVo {

    @JsonIgnore
    private Integer id;

    private Double width;

    private Double height;

    private String path;

    private String description;

    private List<PointVo> points;

}

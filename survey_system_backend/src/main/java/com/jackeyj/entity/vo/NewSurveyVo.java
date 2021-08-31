package com.jackeyj.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author jiyaofei
 */
@Data
public class NewSurveyVo {

    private String title;

    private Integer imageNumber;

    private Date createTime;

    private List<NewImageVo> images;

}

package com.jackeyj.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author jiyaofei
 */
@Data
public class SurveyBoardVo {

    private Integer id;

    private String title;

    private String author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    private List<ImageVo> images;
}

package com.jackeyj.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jiyaofei
 */
@Data
public class SurveyVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private String author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    private Integer imageNumber;
}

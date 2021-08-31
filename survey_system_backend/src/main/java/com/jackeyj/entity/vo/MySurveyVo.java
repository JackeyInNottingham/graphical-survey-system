package com.jackeyj.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author jiyaofei
 */
@Data
public class MySurveyVo {

    private Integer id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createTime;

    private Integer imageNumber;

}

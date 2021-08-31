package com.jackeyj.entity.vo;

import lombok.Data;

/**
 * value object of login form
 * @author jiyaofei
 */
@Data
public class LoginVo {

    private String username;

    private String password;

    private String captcha;

}

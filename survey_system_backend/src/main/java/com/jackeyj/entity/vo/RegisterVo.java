package com.jackeyj.entity.vo;

import lombok.Data;

/**
 * the value object of register form
 * @author jiyaofei
 */
@Data
public class RegisterVo {

    private String username;

    private String password;

    private String email;

    private String captcha;

}

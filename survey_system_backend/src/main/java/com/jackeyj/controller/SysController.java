package com.jackeyj.controller;


import com.jackeyj.common.Result;
import com.jackeyj.entity.vo.UserInfoVo;
import com.jackeyj.entity.vo.LoginVo;
import com.jackeyj.entity.vo.RegisterVo;
import com.jackeyj.service.UserService;
import com.jackeyj.utils.CaptchaUtil;
import com.jackeyj.utils.JwtUtil;
import com.jackeyj.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

/**
 * @author jiyaofei
 */
@RestController
@RequestMapping("/sys")
public class SysController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/getCaptchaImage")
    public Result getCaptchaImage() {
        CaptchaUtil captchaUtil = new CaptchaUtil();
        String captchaId = UUID.randomUUID().toString();
        String captcha = captchaUtil.getCaptcha();

        boolean res = redisUtil.set(captchaId, captcha, 60);
        if (!res) {
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        BufferedImage bufferedImage = captchaUtil.getBufferedImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", stream);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        String captchaImage = Base64.getEncoder().encodeToString(stream.toByteArray());

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Result.success().put("captchaId", captchaId).put("captcha", captchaImage);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo, @RequestParam("captchaId") String captchaId){
        if (loginVo == null){
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }

        String username = loginVo.getUsername();
        String password = loginVo.getPassword();
        String captcha = loginVo.getCaptcha();

        if (username == null || "".equals(username) ||
            password == null || "".equals(password) ||
            captcha == null || "".equals(captcha)){
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }

        String code = (String) redisUtil.get(captchaId);
        if (!captcha.equals(code)) {
            return Result.fail("Incorrect Captcha",
                    HttpStatus.UNAUTHORIZED.value());
        }
        redisUtil.delete(captchaId);


        UserInfoVo userInfo = userService.login(username, password);

        if (userInfo == null){
            return Result.fail("Wrong username or password.");
        }

        if (!userInfo.getStatus()){
            return Result.fail("The user has been banned.",
                    HttpStatus.FORBIDDEN.value());
        }

        String token = JwtUtil.createJwt(userInfo);

        return Result.success()
                .put("token", token)
                .put("role", userInfo.getRole())
                .put("username", userInfo.getUsername());
    }

    @PostMapping("register")
    public Result register(@RequestParam("captchaId") String captchaId, @RequestBody RegisterVo registerVo) {
        String username = registerVo.getUsername();
        String password = registerVo.getPassword();
        String email = registerVo.getEmail();
        String captcha = registerVo.getCaptcha();

        if (username == null || "".equals(username) || username.length() > 50) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }
        if (password == null || "".equals(password) || password.length() > 50) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }
        if (email == null || "".equals(email) || email.length() > 100) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }
        if (captcha == null || "".equals(captcha)) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }

        if (!userService.ensureUniqueUsername(username)) {
            return Result.fail("This username has been in use");
        }

        String code = (String) redisUtil.get(captchaId);
        if (!captcha.equals(code)) {
            return Result.fail("Incorrect Captcha",
                    HttpStatus.BAD_REQUEST.value());
        }
        redisUtil.delete(captchaId);

        if (!userService.registerUser(username, password, email)) {
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return Result.success("Register successfully. Please login now.");
    }

}

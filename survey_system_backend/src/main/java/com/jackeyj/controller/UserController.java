package com.jackeyj.controller;

import com.github.pagehelper.PageInfo;
import com.jackeyj.annotation.RequireAdmin;
import com.jackeyj.common.Result;
import com.jackeyj.entity.User;
import com.jackeyj.entity.vo.PasswordVo;
import com.jackeyj.entity.vo.UserVo;
import com.jackeyj.service.UserService;
import com.jackeyj.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jiyaofei
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequireAdmin
    @GetMapping("/getUserListByPage")
    public Result getUserListByPage(@RequestParam("page") Integer page, @RequestParam("pageSize") Integer pageSize) {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }
        PageInfo<UserVo> userList = userService.getUserVoList(page, pageSize);
        if (userList.getTotal() == 0) {
            return Result.fail("no available info");
        }

        return Result.success().put("userList", userList);
    }

    @RequireAdmin
    @PostMapping("/addUser")
    public Result addUser(@RequestBody User user) {
        if (user == null) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }

        String username = user.getUsername();
        if (!userService.ensureUniqueUsername(username)) {
            return Result.fail("this username has been used");
        }

        if (!userService.insertUser(user)) {
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return Result.success();
    }

    @RequireAdmin
    @GetMapping("/filtrateUser")
    public Result filtrateUser(@RequestParam("username") String username, @RequestParam("active") Boolean status,
                               @RequestParam("role") Integer roleId, @RequestParam("page") Integer page,
                               @RequestParam("pageSize") Integer pageSize) {
        if (page == null || page <= 0) {
            page = 1;
        }
        if (pageSize == null || pageSize <= 0) {
            pageSize = 10;
        }

        PageInfo<UserVo> userList = userService.filtrateUser(page, pageSize, username, status, roleId);

        if (userList.getTotal() == 0) {
            return Result.fail("no available info");
        }

        return Result.success().put("userList", userList);
    }

    @RequireAdmin
    @GetMapping("/getUserInfo")
    public Result getUserInfo(@RequestParam("id") Integer id) {
        if (id == null || id <= 0) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }

        User userInfo = userService.getUserInfo(id);

        if (userInfo == null) {
            return Result.fail("no such user",
                    HttpStatus.NO_CONTENT.value());
        }

        return Result.success().put("userInfo", userInfo);
    }

    @RequireAdmin
    @PutMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody User user) {
        if (user == null) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }
        Integer id = user.getId();
        if (id == null || id <= 0) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }
        if (!userService.updateUserInfo(user)) {
            return Result.fail(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.getReasonPhrase(),
                    HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
        }
        return Result.success("edit successfully");
    }

    @RequireAdmin
    @DeleteMapping("/deleteUserById")
    public Result deleteUserById(@RequestParam("id") Integer id) {
        if (id == null || id <= 0) {
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }
        if (!userService.deleteUserById(id)) {
            return Result.fail("no such user",
                    HttpStatus.BAD_REQUEST.value());
        }

        return Result.success("delete successfully!");
    }

    @PostMapping("/changePassword")
    public Result changePassword(HttpServletRequest request,
                                 @RequestBody PasswordVo passwordVo){
        String oldPassword = passwordVo.getOldPassword();
        String newPassword = passwordVo.getNewPassword();
        if (oldPassword == null || "".equals(oldPassword)){
            return Result.fail("old password is empty",
                    HttpStatus.BAD_REQUEST.value());
        }
        if (newPassword == null || "".equals(newPassword)){
            return Result.fail("new password is empty",
                    HttpStatus.BAD_REQUEST.value());
        }

        String token = request.getHeader("token");

        Integer id = JwtUtil.getClaim(token, "userId").asInt();

        if (!userService.compareOriginalPassword(id, oldPassword)){
            return Result.fail("the old password is wrong",
                    HttpStatus.FORBIDDEN.value());
        }

        if (!userService.updatePassword(id, newPassword)){
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return Result.success("password updated");
    }

}

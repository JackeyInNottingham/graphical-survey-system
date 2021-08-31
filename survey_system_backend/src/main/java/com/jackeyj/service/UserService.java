package com.jackeyj.service;

import com.github.pagehelper.PageInfo;
import com.jackeyj.entity.User;
import com.jackeyj.entity.vo.OperateUserVo;
import com.jackeyj.entity.vo.UserInfoVo;
import com.jackeyj.entity.vo.UserVo;

/**
 * the interface of user service
 * @author jiyaofei
 */
public interface UserService {

    /**
     * realize login function
     * @param username submitted username
     * @param password submitted password
     * @return an object containing user's basic information, if it's null, the user can not login
     */
    UserInfoVo login(String username, String password);

    /**
     * ensure the uniqueness of username
     * @param username submitted username
     * @return whether username is unique
     */
    boolean ensureUniqueUsername(String username);

    /**
     * register user service
     * @param username submitted username
     * @param password submitted password
     * @param email submitted email
     * @return whether the registration is successful
     */
    boolean registerUser(String username, String password, String email);

    PageInfo<UserVo> getUserVoList(Integer page, Integer pageSize);

    PageInfo<UserVo> filtrateUser(Integer page, Integer pageSize, String username, Boolean status, Integer roleId);

    boolean insertUser(User user);

    User getUserInfo(Integer id);

    boolean updateUserInfo(User user);

    boolean deleteUserById(Integer id);

    boolean compareOriginalPassword(Integer id, String oldPassword);

    boolean updatePassword(Integer id, String newPassword);
}
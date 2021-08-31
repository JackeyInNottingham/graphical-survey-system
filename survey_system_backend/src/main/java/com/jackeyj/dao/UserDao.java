package com.jackeyj.dao;

import com.jackeyj.entity.User;
import com.jackeyj.entity.vo.OperateUserVo;
import com.jackeyj.entity.vo.UserInfoVo;
import com.jackeyj.entity.vo.UserVo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiyaofei
 */
@Repository
public interface UserDao {

    /**
     * select specific user from t_user table
     * @param username input username
     * @param password input password
     * @return an object containing user's basic information
     */
    UserInfoVo login(String username, String password);

    /**
     * select user according to username
     * @param username input username
     * @return username
     */
    String selectByUsername(String username);

    /**
     *
     * @param user user object
     * @return number of influenced rows
     */
    int insertUser(User user);

    List<UserVo> getUserVoList();

    List<UserVo> filtrateUser(String username, Boolean status, Integer roleId);

    int insert(User user);

    User getUserInfo(Integer id);

    int updateUserInfo(User user);

    int deleteUserById(Integer id);

    String getPassword(Integer id);

    int updatePassword(Integer id, String newPassword);
}
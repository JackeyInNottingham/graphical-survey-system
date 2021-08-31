package com.jackeyj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jackeyj.dao.AnswerDao;
import com.jackeyj.dao.ImageDao;
import com.jackeyj.dao.SurveyDao;
import com.jackeyj.dao.UserDao;
import com.jackeyj.entity.User;
import com.jackeyj.entity.vo.OperateUserVo;
import com.jackeyj.entity.vo.UserInfoVo;
import com.jackeyj.entity.vo.UserVo;
import com.jackeyj.service.SurveyService;
import com.jackeyj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jiyaofei
 */
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SurveyDao surveyDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private AnswerDao answerDao;

    @Override
    public UserInfoVo login(String username, String password) {
        return userDao.login(username, password);
    }

    @Override
    public boolean ensureUniqueUsername(String username) {
        return userDao.selectByUsername(username) == null;
    }

    @Override
    public boolean registerUser(String username, String password, String email) {
        User user = new User().
                setUsername(username).
                setPassword(password).
                setEmail(email).
                setRoleId(2).
                setStatus(true);
        return userDao.insertUser(user) > 0;
    }

    @Override
    public PageInfo<UserVo> getUserVoList(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<UserVo> userVoList = userDao.getUserVoList();
        PageInfo<UserVo> userList = new PageInfo<>(userVoList);
        return userList;
    }

    @Override
    public PageInfo<UserVo> filtrateUser(Integer page,
                                         Integer pageSize,
                                         String username,
                                         Boolean status,
                                         Integer roleId) {
        PageHelper.startPage(page, pageSize);
        List<UserVo> userVoList = userDao.filtrateUser(username, status, roleId);
        PageInfo<UserVo> userList = new PageInfo<>(userVoList);
        return userList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertUser(User user) {
        return userDao.insert(user) > 0;
    }

    @Override
    public User getUserInfo(Integer id) {
        return userDao.getUserInfo(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateUserInfo(User user) {
        return userDao.updateUserInfo(user) > 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteUserById(Integer id) {
        answerDao.deleteAnswerByAuthorId(id);
        imageDao.deleteImageByAuthorId(id);
        surveyDao.deleteSurveyByAuthorId(id);
        return userDao.deleteUserById(id) > 0;
    }

    @Override
    public boolean compareOriginalPassword(Integer id, String oldPassword) {
        String originalPassword = userDao.getPassword(id);
        return oldPassword.equals(originalPassword);
    }

    @Override
    public boolean updatePassword(Integer id, String newPassword) {
        return userDao.updatePassword(id, newPassword) > 0;
    }
}

package com.jackeyj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jackeyj.dao.AnswerDao;
import com.jackeyj.dao.ImageDao;
import com.jackeyj.dao.SurveyDao;
import com.jackeyj.entity.Survey;
import com.jackeyj.entity.vo.*;
import com.jackeyj.service.ImageService;
import com.jackeyj.service.SurveyService;
import com.jackeyj.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jiyaofei
 */
@Service
public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private SurveyDao surveyDao;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public PageInfo<SurveyVo> getSurveyList(Integer page, Integer pageSize) {
        PageInfo<SurveyVo> surveyList = null;
        if (page == 1 && pageSize == 13){
            String key = "surveyListFirstPage";
            surveyList = (PageInfo<SurveyVo>) redisUtil.get(key);
            if (surveyList == null) {
                PageHelper.startPage(page, pageSize);
                List<SurveyVo> surveyVoList = surveyDao.getSurveyList();
                surveyList = new PageInfo<>(surveyVoList);
                redisUtil.set(key, surveyList);
            }
        }else {
            PageHelper.startPage(page, pageSize);
            List<SurveyVo> surveyVoList = surveyDao.getSurveyList();
            surveyList = new PageInfo<>(surveyVoList);
        }

        return surveyList;
    }

    @Override
    public PageInfo<SurveyVo> filtrateSurveyList(Integer page, Integer pageSize, String title, String author) {
        PageHelper.startPage(page, pageSize);
        List<SurveyVo> surveyVoList = surveyDao.filtrateSurveyList(title, author);
        PageInfo<SurveyVo> surveyList = new PageInfo<>(surveyVoList);
        return surveyList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteSurveyById(Integer id) {
        int i = surveyDao.deleteSurveyById(id);
        redisUtil.delete("surveyListFirstPage");
        return i > 0 && imageService.deleteImageBySurveyId(id);
    }

    @Override
    public PageInfo<MySurveyVo> getMySurveyList(Integer page, Integer pageSize, Integer userId) {
        PageHelper.startPage(page, pageSize);
        List<MySurveyVo> surveyVoList = surveyDao.getMySurveyList(userId);
        PageInfo<MySurveyVo> surveyList = new PageInfo<>(surveyVoList);
        return surveyList;
    }

    @Override
    public PageInfo<MySurveyVo> filtrateMySurveyList(Integer page, Integer pageSize, Integer userId, String title) {
        PageHelper.startPage(page, pageSize);
        List<MySurveyVo> surveyVoList = surveyDao.filtrateMySurveyList(userId, title);
        PageInfo<MySurveyVo> surveyList = new PageInfo<>(surveyVoList);
        return surveyList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteMySurveyById(Integer id, Integer userId) {
        Integer authorId = surveyDao.getAuthorId(id);
        if (authorId == null || !authorId.equals(userId)) {
            return false;
        }
        int i = surveyDao.deleteSurveyById(id);
        redisUtil.delete("surveyListFirstPage");
        return i > 0 && imageService.deleteImageBySurveyId(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean createSurvey(Integer userId, NewSurveyVo surveyVo) {
        Survey survey = new Survey()
                .setAuthorId(userId)
                .setTitle(surveyVo.getTitle())
                .setImageNumber(surveyVo.getImageNumber())
                .setCreateTime(surveyVo.getCreateTime());
        redisUtil.delete("surveyListFirstPage");
        return surveyDao.insertSurvey(survey) > 0 && imageDao.insertImages(survey.getId(), surveyVo.getImages()) > 0;
    }

    @Override
    public SurveyBoardVo getSurveyById(Integer id) {
        SurveyBoardVo surveyBoardVo = surveyDao.selectSurveyById(id);
        return surveyBoardVo;
    }

    @Override
    public List<SurveyResultVo> getResultById(Integer id) {
        List<SurveyResultVo> surveyResult = imageDao.getResultBySurveyId(id);
        return surveyResult;
    }

}

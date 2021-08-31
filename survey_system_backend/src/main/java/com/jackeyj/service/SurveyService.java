package com.jackeyj.service;

import com.github.pagehelper.PageInfo;
import com.jackeyj.entity.vo.*;

import java.util.List;

/**
 * @author jiyaofei
 */
public interface SurveyService {
    PageInfo<SurveyVo> getSurveyList(Integer page, Integer pageSize);

    PageInfo<SurveyVo> filtrateSurveyList(Integer page, Integer pageSize, String title, String author);

    boolean deleteSurveyById(Integer id);

    PageInfo<MySurveyVo> getMySurveyList(Integer page, Integer pageSize, Integer userId);

    PageInfo<MySurveyVo> filtrateMySurveyList(Integer page, Integer pageSize, Integer userId, String title);

    boolean deleteMySurveyById(Integer id, Integer userId);

    boolean createSurvey(Integer userId, NewSurveyVo surveyVo);

    SurveyBoardVo getSurveyById(Integer id);

    List<SurveyResultVo> getResultById(Integer id);
}

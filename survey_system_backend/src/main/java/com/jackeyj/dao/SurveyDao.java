package com.jackeyj.dao;

import com.jackeyj.entity.Survey;
import com.jackeyj.entity.vo.MySurveyVo;
import com.jackeyj.entity.vo.SurveyBoardVo;
import com.jackeyj.entity.vo.SurveyVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyDao {

    List<SurveyVo> getSurveyList();

    List<SurveyVo> filtrateSurveyList(String title, String author);

    int deleteSurveyById(Integer id);

    List<MySurveyVo> getMySurveyList(Integer userId);

    List<MySurveyVo> filtrateMySurveyList(Integer userId, String title);

    Integer getAuthorId(Integer id);

    SurveyBoardVo selectSurveyById(Integer id);

    int insertSurvey(Survey survey);

    void deleteSurveyByAuthorId(Integer authorId);
}
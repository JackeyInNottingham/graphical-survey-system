package com.jackeyj.dao;

import com.jackeyj.entity.vo.AnswerVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerDao {

    int deleteAnswerBySurveyId(Integer surveyId);

    int insertAnswers(List<AnswerVo> answerVoList);

    void deleteAnswerByAuthorId(Integer authorId);
}
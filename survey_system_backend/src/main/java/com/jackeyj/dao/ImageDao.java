package com.jackeyj.dao;

import com.jackeyj.entity.vo.NewImageVo;
import com.jackeyj.entity.vo.SurveyResultVo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageDao {
    int deleteImageBySurveyId(Integer surveyId);

    List<String> getImagePathBySurveyId(Integer surveyId);

    int insertImages(Integer surveyId, List<NewImageVo> images);

    List<SurveyResultVo> getResultBySurveyId(Integer surveyId);

    void deleteImageByAuthorId(Integer authorId);
}
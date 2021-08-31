package com.jackeyj.service.impl;

import com.jackeyj.dao.AnswerDao;
import com.jackeyj.dao.ImageDao;
import com.jackeyj.service.ImageService;
import com.jackeyj.utils.PictureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jiyaofei
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private AnswerDao answerDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteImageBySurveyId(Integer surveyId) {
        List<String> paths = imageDao.getImagePathBySurveyId(surveyId);
        answerDao.deleteAnswerBySurveyId(surveyId);
        int i = imageDao.deleteImageBySurveyId(surveyId);
        for (String path : paths) {
            PictureUtil.deletePicture(path);
        }
        return i > 0;
    }
}

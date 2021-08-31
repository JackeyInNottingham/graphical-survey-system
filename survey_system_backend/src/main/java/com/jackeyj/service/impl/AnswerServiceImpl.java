package com.jackeyj.service.impl;

import com.jackeyj.dao.AnswerDao;
import com.jackeyj.entity.vo.AnswerVo;
import com.jackeyj.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author jiyaofei
 */
@Service
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertAnswers(List<AnswerVo> answerVoList) {
        return answerDao.insertAnswers(answerVoList) > 0;
    }
}

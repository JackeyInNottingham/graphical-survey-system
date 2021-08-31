package com.jackeyj.service;

import com.jackeyj.entity.vo.AnswerVo;

import java.util.List;

/**
 * @author jiyaofei
 */
public interface AnswerService {
    boolean insertAnswers(List<AnswerVo> answerVoList);
}

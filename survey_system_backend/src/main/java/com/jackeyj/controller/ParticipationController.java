package com.jackeyj.controller;

import com.github.pagehelper.PageInfo;
import com.jackeyj.common.Result;
import com.jackeyj.entity.vo.AnswerVo;
import com.jackeyj.entity.vo.SurveyBoardVo;
import com.jackeyj.entity.vo.SurveyVo;
import com.jackeyj.service.AnswerService;
import com.jackeyj.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * survey participation module APIs
 * @author jiyaofei
 */
@RestController
@RequestMapping("/part")
public class ParticipationController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private AnswerService answerService;

    @GetMapping("/getSurveySampleListByPage")
    public Result getSurveySampleListByPage(@RequestParam("page") Integer page,
                                            @RequestParam("pageSize") Integer pageSize){
        if (page == null || page < 1){
            page = 1;
        }
        if (pageSize == null || pageSize < 1){
            pageSize = 10;
        }

        PageInfo<SurveyVo> surveyList = surveyService.getSurveyList(page, pageSize);

        if (surveyList.getTotal() == 0){
            return Result.fail("no surveys");
        }

        return Result.success().put("surveyList", surveyList);
    }

    @GetMapping("/filtrateSurveySampleListByPage")
    public Result filtrateSurveySampleListByPage(@RequestParam("page") Integer page,
                                                 @RequestParam("pageSize") Integer pageSize,
                                                 @RequestParam("title") String title,
                                                 @RequestParam("author") String author){
        if (page == null || page < 1){
            page = 1;
        }
        if (pageSize == null || pageSize < 1){
            pageSize = 10;
        }

        PageInfo<SurveyVo> surveyList = surveyService.filtrateSurveyList(page, pageSize, title, author);

        if (surveyList.getTotal() == 0){
            return Result.fail("no surveys");
        }

        return Result.success().put("surveyList", surveyList);
    }

    @GetMapping("/getSurveyBoard")
    public Result getSurveyBoard(@RequestParam("id") Integer id){
        if (id == null || id < 1){
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }

        SurveyBoardVo surveyBoardVo = surveyService.getSurveyById(id);

        if (surveyBoardVo == null){
            return Result.fail("no such survey",
                    HttpStatus.NO_CONTENT.value());
        }

        return Result.success().put("surveyBoardVo", surveyBoardVo);
    }

    @PostMapping("/submitAnswer")
    public Result submitAnswer(@RequestBody List<AnswerVo> answerVoList){
        if (answerVoList == null){
            return Result.fail("no answers",
                    HttpStatus.NO_CONTENT.value());
        }

        if (!answerService.insertAnswers(answerVoList)){
            return Result.fail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return Result.success("answer submitted, thank you for your cooperation");
    }

}

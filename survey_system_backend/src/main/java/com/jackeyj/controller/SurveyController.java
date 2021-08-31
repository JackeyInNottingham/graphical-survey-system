package com.jackeyj.controller;

import com.github.pagehelper.PageInfo;
import com.jackeyj.annotation.RequireAdmin;
import com.jackeyj.common.Result;
import com.jackeyj.entity.vo.MySurveyVo;
import com.jackeyj.entity.vo.NewSurveyVo;
import com.jackeyj.entity.vo.SurveyResultVo;
import com.jackeyj.entity.vo.SurveyVo;
import com.jackeyj.service.SurveyService;
import com.jackeyj.utils.JwtUtil;
import com.jackeyj.utils.PictureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author jiyaofei
 */
@RestController
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @PostMapping("/uploadPicture")
    public Result uploadPicture(HttpServletRequest request, MultipartFile picture){
        //check picture
        if (picture == null){
            return Result.fail("picture is empty",
                    HttpStatus.BAD_REQUEST.value());
        }

        //check user information
        String token = request.getHeader("token");
        Integer id = null;
        try {
            id = JwtUtil.getClaim(token, "userId").asInt();
        } catch (Exception e) {
            return Result.fail(HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    HttpStatus.UNAUTHORIZED.value());
        }

        InputStream pictureInputStream = null;
        try {
            //get picture size
            pictureInputStream = picture.getInputStream();
            BufferedImage pic = ImageIO.read(pictureInputStream);
            int width = pic.getWidth();
            int height = pic.getHeight();

            //save picture
            File file = PictureUtil.savePicture(picture, id);
            if (file == null){
                return Result.fail("upload fail",
                        HttpStatus.INTERNAL_SERVER_ERROR.value());
            }

            //get the path
            String originPath = System.getProperty("user.dir");
            String absolutePath = file.getAbsolutePath();
            String relativePath = absolutePath.substring(originPath.length());

            return Result.success("upload success")
                    .put("path", relativePath)
                    .put("width", width)
                    .put("height", height);
        } catch (IOException e) {
            return Result.fail(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        } finally {
            if (pictureInputStream != null){
                try {
                    pictureInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * delete the picture according the path
     * used in cancel upload
     *
     * @param path picture path
     * @return interface specification
     */
    @DeleteMapping("/deletePictureFile")
    public Result deletePictureFile(@RequestParam("path") String path){
        if (path == null || "".equals(path)){
            return Result.fail("path is empty",
                    HttpStatus.BAD_REQUEST.value());
        }
        if (!PictureUtil.deletePicture(path)){
            return Result.fail("delete fail",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return Result.success();
    }


    /**
     * following interfaces are for the full survey list page
     */

    @RequireAdmin
    @GetMapping("/getSurveyListByPage")
    public Result getSurveyListByPage(@RequestParam("page") Integer page,
                                      @RequestParam("pageSize") Integer pageSize){
        if (page == null || page <= 0){
            page = 1;
        }
        if (pageSize == null || pageSize <= 0){
            pageSize = 10;
        }

        PageInfo<SurveyVo> surveyList = surveyService.getSurveyList(page, pageSize);
        if (surveyList.getTotal() == 0){
            return Result.fail("no available info");
        }

        return Result.success().put("surveyList", surveyList);
    }

    @RequireAdmin
    @GetMapping("/filtrateSurveyList")
    public Result filterSurveyList(@RequestParam("title") String title,
                                   @RequestParam("author") String author,
                                   @RequestParam("page") Integer page,
                                   @RequestParam("pageSize") Integer pageSize){
        if (page == null || page <= 0){
            page = 1;
        }
        if (pageSize == null || pageSize <= 0){
            pageSize = 10;
        }

        PageInfo<SurveyVo> surveyList = surveyService.filtrateSurveyList(page, pageSize, title, author);

        if (surveyList.getTotal() == 0){
            return Result.fail("no available info");
        }

        return Result.success().put("surveyList", surveyList);
    }

    @RequireAdmin
    @DeleteMapping("/deleteSurveyById")
    public Result deleteSurveyById(@RequestParam("id") Integer id){
        if (id == null || id <= 0){
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }

        if (!surveyService.deleteSurveyById(id)){
            return Result.fail("no such survey",
                    HttpStatus.BAD_REQUEST.value());
        }

        return Result.success("delete success");
    }


    /**
     * following interfaces are for the my survey list page
     */

    @GetMapping("getMySurveyListByPage")
    public Result getMySurveyListByPage(HttpServletRequest request,
                                        @RequestParam("page") Integer page,
                                        @RequestParam("pageSize") Integer pageSize){

        String token = request.getHeader("token");
        Integer userId = null;
        try {
            userId = JwtUtil.getClaim(token, "userId").asInt();
        } catch (Exception e) {
            return Result.fail(e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value());
        }

        if (page == null || page <= 0){
            page = 1;
        }
        if (pageSize == null || pageSize <= 0){
            pageSize = 10;
        }

        PageInfo<MySurveyVo> mySurveyList = surveyService.getMySurveyList(page, pageSize, userId);
        if (mySurveyList.getTotal() == 0){
            return Result.fail("no available info");
        }

        return Result.success().put("mySurveyList", mySurveyList);
    }

    @GetMapping("/filtrateMySurveyList")
    public Result filterMySurveyList(HttpServletRequest request,
                                     @RequestParam("title") String title,
                                     @RequestParam("page") Integer page,
                                     @RequestParam("pageSize") Integer pageSize){
        String token = request.getHeader("token");
        Integer userId = null;
        try {
            userId = JwtUtil.getClaim(token, "userId").asInt();
        } catch (Exception e) {
            return Result.fail(e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value());
        }

        if (page == null || page <= 0){
            page = 1;
        }
        if (pageSize == null || pageSize <= 0){
            pageSize = 10;
        }
        if (title == null || "".equals(title)){
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }

        PageInfo<MySurveyVo> mySurveyList = surveyService.filtrateMySurveyList(page, pageSize, userId, title);
        if (mySurveyList.getTotal() == 0){
            return Result.fail("no available info");
        }

        return Result.success().put("mySurveyList", mySurveyList);
    }

    @DeleteMapping("/deleteMySurveyById")
    public Result deleteMySurveyById(HttpServletRequest request, @RequestParam("id") Integer id){
        if (id == null || id <= 0){
            return Result.fail(HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    HttpStatus.BAD_REQUEST.value());
        }
        String token = request.getHeader("token");
        Integer userId = null;
        try {
            userId = JwtUtil.getClaim(token, "userId").asInt();
        } catch (Exception e) {
            return Result.fail(e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value());
        }

        if (!surveyService.deleteMySurveyById(id, userId)){
            return Result.fail("delete failed",
                    HttpStatus.BAD_REQUEST.value());
        }

        return Result.success("delete success");
    }

    @PostMapping("/createSurvey")
    public Result createSurvey(HttpServletRequest request, @RequestBody NewSurveyVo surveyVo){

        if (surveyVo == null){
            return Result.fail("survey info is empty",
                    HttpStatus.BAD_REQUEST.value());
        }

        String token = request.getHeader("token");
        Integer userId = null;
        try {
            userId = JwtUtil.getClaim(token, "userId").asInt();
        } catch (Exception e) {
            return Result.fail(e.getMessage(),
                    HttpStatus.UNAUTHORIZED.value());
        }


        if (!surveyService.createSurvey(userId, surveyVo)){
            return Result.fail("fail to create");
        }

        return Result.success("new survey created");

    }

    @GetMapping("/getResult")
    public Result getResult(@RequestParam("id") Integer id){
        if (id == null || id <= 0){
            return Result.fail("no such survey",
                    HttpStatus.NO_CONTENT.value());
        }

        List<SurveyResultVo> surveyResult = surveyService.getResultById(id);

        if (surveyResult == null){
            return Result.fail(HttpStatus.NO_CONTENT.getReasonPhrase(),
                    HttpStatus.NO_CONTENT.value());
        }

        return Result.success().put("surveyResult", surveyResult);
    }

}

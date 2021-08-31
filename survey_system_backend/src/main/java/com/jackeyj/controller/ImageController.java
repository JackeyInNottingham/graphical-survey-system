package com.jackeyj.controller;

import com.jackeyj.common.Result;
import com.jackeyj.utils.PictureUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author jiyaofei
 */
@RestController
@RequestMapping("/image")
public class ImageController {

    @GetMapping("/getImageByPath")
    public void getImageByPath(HttpServletResponse response, @RequestParam("path") String path) {
        File file = PictureUtil.getPicture(path);
        String extension = PictureUtil.getFileExtension(file);
        try {
            BufferedImage picture = ImageIO.read(file);
            ImageIO.write(picture, extension, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @DeleteMapping("/deleteImageByPath")
    public Result deleteImageByPath(@RequestParam("path") String path) {
        if (path == null || "".equals(path)) {
            return Result.fail("path is empty");
        }

        if (!PictureUtil.deletePicture(path)) {
            return Result.fail("no such image",
                    HttpStatus.NO_CONTENT.value());
        }

        return Result.success("delete success");
    }

}

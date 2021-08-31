package com.jackeyj.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * image tool class
 *
 * @author jiyaofei
 */
public class PictureUtil {

    private final static String PICTURE_PATH_PREFIX = "/upload/picture";

    /**
     * create file directory if not exists
     *
     * @return file path
     */
    private static File getDir(Integer userId) {
        String filePath = System.getProperty("user.dir") + PICTURE_PATH_PREFIX + File.separator + userId;

        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        return fileDir;
    }


    /**
     * save picture to local disk
     *
     * @param picture picture file
     * @param userId  researcher's id
     * @return save result
     */
    public static File savePicture(MultipartFile picture, Integer userId) {
        String originalFilename = picture.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String newFilename = uuid + "-" + originalFilename;

        try {
            //save picture to local file
            File dir = getDir(userId);
            File newFile = new File(dir.getAbsolutePath() + File.separator + newFilename);
            picture.transferTo(newFile);
            return newFile;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * delete the uploaded file
     *
     * @param path the stored relative path
     * @return
     */
    public static boolean deletePicture(String path) {

        File file = new File(System.getProperty("user.dir") + path);
        if (!file.exists()) {
            return false;
        }

        return file.delete();
    }

    public static String getFileExtension(File file){
        String fileName = file.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0){
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }else {
            return "";
        }
    }

    public static File getPicture(String picturePath) {
        File picture = new File(System.getProperty("user.dir") + picturePath);
        return picture;
    }
}

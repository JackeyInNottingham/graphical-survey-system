package com.jackeyj.common;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

/**
 * common result class
 * interface specification between backend and frontend
 *
 * @author jiyaofei
 */
public class Result extends HashMap<String, Object> {

    public Result(){
        put("success", true);
        put("msg", "");
    }

    public static Result success(){
        Result result = new Result();
        result.put("code", HttpStatus.OK.value());
        return result;
    }

    public static Result success(String msg){
        Result result = new Result();
        result.put("msg", msg);
        return result;
    }

    public static Result fail(){
        Result result = new Result();
        result.put("success", false);
        return result;
    }

    public static Result fail(String errorMsg){
        Result result = new Result();
        result.put("success", false);
        result.put("msg", errorMsg);
        return result;
    }

    public static Result fail(String errorMsg, Integer code){
        Result result = new Result();
        result.put("success", false);
        result.put("msg", errorMsg);
        result.put("code", code);
        return result;
    }

    /**
     * enable chain operation
     * @param key key name
     * @param value value
     * @return general result object
     */
    @Override
    public Result put(String key, Object value){
        super.put(key, value);
        return this;
    }
}

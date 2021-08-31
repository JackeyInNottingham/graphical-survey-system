package com.jackeyj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author jiyaofei
 */
@SpringBootApplication
@MapperScan(basePackages = "com.jackeyj.dao")
public class SurveySystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurveySystemApplication.class, args);
    }

}

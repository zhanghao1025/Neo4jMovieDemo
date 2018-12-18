package com.springboot.exception;

/**
 * @author hai
 * @create 2018年3月10日
 **/
public class DataNotFoundException extends BusinessException {
    public static final String DEFAULT_MESSAGE = "数据不存在或已被删除";

    public DataNotFoundException() {
        this(DEFAULT_MESSAGE);
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
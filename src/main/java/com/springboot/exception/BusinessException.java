package com.springboot.exception;

/**
 * @author hai
 * @create 2018年3月10日
 * 異常處理封裝
 **/
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

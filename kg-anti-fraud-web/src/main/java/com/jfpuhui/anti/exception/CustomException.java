package com.jfpuhui.anti.exception;

/**
 * 自定义异常
 *
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-21-17:59
 */
public class CustomException extends Exception {

    public CustomException() {}

    public CustomException(String message) {
        super(message);
    }
}

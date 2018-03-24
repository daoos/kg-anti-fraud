package com.jfpuhui.anti.exception;

/**
 * 自定义异常
 *
 * @author Nisus-Liu
 * @version 1.0.0
 * @email liuhejun108@163.com
 * @date 2018-03-21-17:59
 */
public class CustomRuntimeException extends RuntimeException {

    public CustomRuntimeException() {}

    public CustomRuntimeException(String message) {
        super(message);
    }
}

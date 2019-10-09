package io.seata.sample.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author penglu
 * @version 1.0.0
 * @date 2019-08-30
 * @copyright 本内容仅限于浙江云贸科技有限公司内部传阅，禁止外泄以及用于其他的商业目的
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Object returnErrorCode(Throwable exception) {
        System.err.println("catch global exception");
        return exception.getMessage();
    }

}

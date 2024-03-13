package com.yj.schedule.global.exception;

import com.yj.schedule.global.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<CommonResponse<?>> handleNotFoundException(IllegalArgumentException ex) {
        CommonResponse<?> Exception = new CommonResponse<>(ex.getMessage());
        return new ResponseEntity<>(
                Exception,
                HttpStatus.BAD_REQUEST
        );
    }
}
package com.yj.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CommonResponse<T> {
    private Integer statusCode;
    private String msg;
    private T data;

    public CommonResponse(Integer statusCode, String msg){
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
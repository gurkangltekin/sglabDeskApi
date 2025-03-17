package com.sglab.SGLabDeskApi.utils;

import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ServiceResult <T>{
    private Boolean success;
    private int code;
    private String errorKey;
    private T data;

}
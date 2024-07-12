/*
 * created by max$
 */


package com.phobebook.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ErrorDto {
    private String timestamp;
    private int status;
    private String error;
    private Object message;
    private String path;

}

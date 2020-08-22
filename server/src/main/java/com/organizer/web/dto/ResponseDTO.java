package com.organizer.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(callSuper = true)
@Builder
public class ResponseDTO<T> {
    private T data;
    private String message;
    private Integer code;
}

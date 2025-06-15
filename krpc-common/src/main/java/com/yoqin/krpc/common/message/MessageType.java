package com.yoqin.krpc.common.message;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum MessageType {
    //枚举常量，0代表Request，1代表Response
    REQUEST(0),
    RESPONSE(1);

    private int code;

    public int getCode() {
        return code;
    }
}

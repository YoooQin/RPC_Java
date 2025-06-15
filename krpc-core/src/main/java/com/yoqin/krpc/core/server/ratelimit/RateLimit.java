package com.yoqin.krpc.core.server.ratelimit;

public interface RateLimit {
    //获得访问许可
    boolean getToken();
}

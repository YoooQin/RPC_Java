package Server.ratelimit;

public interface RateLimit {
    //获得访问许可
    boolean getToken();
}

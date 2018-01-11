package io.github.biezhi.elves.request;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author biezhi
 * @date 2018/1/11
 */
@Data
public class Request {

    private Map<String, String> headers = new ConcurrentHashMap<>();
    private Map<String, String> cookies = new ConcurrentHashMap<>();

    public Request header(String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    public Request cookie(String key, String value) {
        this.cookies.put(key, value);
        return this;
    }

    public String header(String key) {
        return this.headers.get(key);
    }

    public String cookie(String key) {
        return this.cookies.get(key);
    }

}

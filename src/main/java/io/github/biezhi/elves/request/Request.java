package io.github.biezhi.elves.request;

import io.github.biezhi.elves.spider.Spider;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author biezhi
 * @date 2018/1/11
 */
@Getter
public class Request {

    private Spider spider;
    private String url;
    private Map<String, String> headers = new ConcurrentHashMap<>();
    private Map<String, String> cookies = new ConcurrentHashMap<>();

    public Request(Spider spider, String url) {
        this.spider = spider;
        this.url = url;
    }

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

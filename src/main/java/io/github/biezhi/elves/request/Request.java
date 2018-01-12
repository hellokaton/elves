package io.github.biezhi.elves.request;

import io.github.biezhi.elves.spider.Spider;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author biezhi
 * @date 2018/1/11
 */
@Getter
public class Request {

    private Spider spider;
    private String url;
    private String              method  = "GET";
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();

    public Request(Spider spider, String url) {
        this.spider = spider;
        this.url = url;
        this.header("User-Agent", spider.getConfig().userAgent());
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

    public Request method(String method) {
        this.method = method;
        return this;
    }

    public String method() {
        return this.method;
    }
}

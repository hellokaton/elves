package io.github.biezhi.elves.response;

import io.github.biezhi.elves.request.Request;

/**
 * @author biezhi
 * @date 2018/1/11
 */
public class Response {

    private Request request;
    private String body;

    public Response(Request request) {
        this.request = request;
    }

    public Response body(String body) {
        this.body = body;
        return this;
    }

    public String body() {
        return body;
    }

}

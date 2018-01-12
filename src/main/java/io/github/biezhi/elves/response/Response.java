package io.github.biezhi.elves.response;

import io.github.biezhi.elves.request.Request;
import lombok.Getter;

import java.io.InputStream;

/**
 * @author biezhi
 * @date 2018/1/11
 */
public class Response {

    @Getter
    private Request     request;
    private Body body;

    public Response(Request request, InputStream inputStream) {
        this.request = request;
        this.body = new Body(inputStream, request.charset());
    }

    public Body body() {
        return body;
    }

}

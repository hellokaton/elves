package io.github.biezhi.elves.download;

import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * @author biezhi
 * @date 2018/1/11
 */
@Slf4j
public class Downloader implements Runnable {

    private final Request request;

    public Downloader(Request request) {
        this.request = request;
    }

    @Override
    public void run() {
        log.info("开始下载: {}", request.getUrl());

        io.github.biezhi.request.Request httpReq = null;
        if ("get".equalsIgnoreCase(request.method())) {
            httpReq = io.github.biezhi.request.Request.get(request.getUrl());
        }
        if ("post".equalsIgnoreCase(request.method())) {
            httpReq = io.github.biezhi.request.Request.post(request.getUrl());
        }

        String body = httpReq.headers(request.getHeaders())
                .connectTimeout(request.getSpider().getConfig().timeout())
                .readTimeout(request.getSpider().getConfig().timeout())
                .body();

        log.info("下载完成: {}", request.getUrl());

        Response response = new Response(request);
        response.body(body);

        Object item = request.getSpider().parse(response);
        request.getSpider().getPipelines()
                .forEach(pipeline -> pipeline.process(item, request));

    }

}

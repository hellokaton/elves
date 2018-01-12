package io.github.biezhi.elves.download;

import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import io.github.biezhi.elves.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * 豆瓣电影爬虫
 *
 * @author biezhi
 * @date 2018/1/11
 */
@Slf4j
public class Downloader implements Runnable {

    private final Scheduler scheduler;
    private final Request   request;

    public Downloader(Scheduler scheduler, Request request) {
        this.scheduler = scheduler;
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

        InputStream result = httpReq.headers(request.getHeaders())
                .connectTimeout(request.getSpider().getConfig().timeout())
                .readTimeout(request.getSpider().getConfig().timeout())
                .stream();

        log.info("下载完成: {}", request.getUrl());
        Response response = new Response(request, result);
        scheduler.addResponse(response);
    }

}

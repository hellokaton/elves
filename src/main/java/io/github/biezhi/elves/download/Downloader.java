package io.github.biezhi.elves.download;

import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

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
        try {
            log.info("开始下载: {}", request.getUrl());
            TimeUnit.MILLISECONDS.sleep(1300);
            log.info("下载完成: {}", request.getUrl());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Response response = new Response(request);
        response.body("Hello Elves. " + request.getUrl());
        Object item = request.getSpider().parse(response);
        request.getSpider().getPipelines()
                .forEach(pipeline -> {
                    pipeline.process(item, request);
                });

    }

}

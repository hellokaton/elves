package io.github.biezhi.elves.spider;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.event.ElvesEvent;
import io.github.biezhi.elves.event.EventManager;
import io.github.biezhi.elves.pipeline.Pipeline;
import io.github.biezhi.elves.request.Parser;
import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import io.github.biezhi.elves.response.Result;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 爬虫基类
 *
 * @author biezhi
 * @date 2018/1/11
 */
@Data
public abstract class Spider {

    protected String name;
    protected Config config;
    protected List<String>   startUrls = new ArrayList<>();
    protected List<Pipeline> pipelines = new ArrayList<>();
    protected List<Request>  requests  = new ArrayList<>();

    public Spider() {
        this("未命名爬虫");
    }

    public Spider(String name) {
        this.name = name;
        EventManager.registerEvent(ElvesEvent.SPIDER_STARTED, this::onStart);
    }

    public Spider startUrls(String... urls) {
        this.startUrls.addAll(Arrays.asList(urls));
        return this;
    }

    /**
     * 爬虫启动前执行
     */
    public void onStart(Config config) {
    }

    /**
     * 添加 Pipeline 处理
     */
    protected <T> Spider addPipeline(Pipeline<T> pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }

    /**
     * 构建一个Request
     */
    public <T> Request<T> makeRequest(String url) {
        return makeRequest(url, this::parse);
    }

    public <T> Request<T> makeRequest(String url, Parser<T> parser) {
        return new Request(this, url, parser);
    }

    /**
     * 解析 DOM
     */
    protected abstract <T> Result<T> parse(Response response);

}

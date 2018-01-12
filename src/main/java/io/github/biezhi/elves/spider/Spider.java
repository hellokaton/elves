package io.github.biezhi.elves.spider;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.pipeline.Pipeline;
import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/1/11
 */
@Data
@NoArgsConstructor
public abstract class Spider {

    protected String name;
    protected Config config;
    protected List<String>   startUrls = new ArrayList<>();
    protected List<Pipeline> pipelines = new ArrayList<>();
    protected List<Request>  requests  = new ArrayList<>();

    public Spider(String name) {
        this.name = name;
    }

    public Spider startUrls(String... urls) {
        this.startUrls.addAll(Arrays.asList(urls));
        return this;
    }

    public Spider started(Config config) {
        return this;
    }

    public abstract <T> T parse(Response response);

    public Spider addPipeline(Pipeline pipeline) {
        this.pipelines.add(pipeline);
        return this;
    }

}

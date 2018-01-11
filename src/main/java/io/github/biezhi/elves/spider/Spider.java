package io.github.biezhi.elves.spider;

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
    protected List<String> startUrls = new ArrayList<>();
    protected List<Request> requests;

    public Spider(String name) {
        this.name = name;
    }

    public Spider startUrls(String... urls) {
        this.startUrls.addAll(Arrays.asList(urls));
        return this;
    }

    public Spider onStart(){
        return this;
    }

    public abstract void parse(Response response);

}

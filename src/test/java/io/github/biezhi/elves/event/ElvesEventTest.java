package io.github.biezhi.elves.event;

import io.github.biezhi.elves.Elves;
import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.response.Response;
import io.github.biezhi.elves.response.Result;
import io.github.biezhi.elves.spider.Spider;

/**
 * @author biezhi
 * @date 2018/1/12
 */
public class ElvesEventTest {

    public static void main(String[] args) {
        Elves.me(new Spider("测试爬虫") {
            @Override
            public Result<String> parse(Response response) {
                return new Result<>(response.body().toString());
            }
        }, Config.me()).onStart(config -> System.out.println("asasas")).start();
    }

}

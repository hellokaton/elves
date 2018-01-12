package io.github.biezhi.elves.examples;

import io.github.biezhi.elves.Elves;
import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.response.Response;
import io.github.biezhi.elves.spider.DoubanSpider;
import io.github.biezhi.elves.spider.Spider;
import lombok.extern.slf4j.Slf4j;

/**
 * @author biezhi
 * @date 2018/1/12
 */
@Slf4j
public class GithubExample {

    public static void main(String[] args) {
        GithubSpider githubSpider = new GithubSpider("Github");
        Elves.me(githubSpider, Config.me()).start();
    }

    static class GithubSpider extends Spider {

        public GithubSpider(String name) {
            super(name);
        }

        @Override
        public Spider started(Config config) {
            startUrls("");
            return super.started(config);
        }

        @Override
        public String parse(Response response) {
            log.info("Parse: {}", response.body());
            return response.body();
        }
    }
}

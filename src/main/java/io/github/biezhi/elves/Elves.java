package io.github.biezhi.elves;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.config.SampleConfig;
import io.github.biezhi.elves.spider.Spider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author biezhi
 * @date 2018/1/11
 */
@Slf4j
@NoArgsConstructor
public class Elves {

    private Spider spider;
    private Config config;

    public static Elves me(Spider spider) {
        return me(spider, new SampleConfig());
    }

    public static Elves me(Spider spider, Config config) {
        Elves elves = new Elves();
        elves.spider = spider;
        elves.config = config;
        return elves;
    }

    public void start() {
        log.info("Spider [{}] starting", spider.getName());
        log.info("Spider [{}] config [{}]", config);
    }

}

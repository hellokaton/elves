package io.github.biezhi.elves;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.spider.Spider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Elves
 *
 * @author biezhi
 * @date 2018/1/11
 */
@Slf4j
@NoArgsConstructor
public class Elves {

    private Spider spider;
    private Config config;

    public static Elves me(Spider spider) {
        return me(spider, Config.me());
    }

    public static Elves me(Spider spider, Config config) {
        Elves elves = new Elves();
        elves.spider = spider;
        elves.config = config;
        return elves;
    }

    public void start() {
        log.info("Spider [{}] 启动...", spider.getName());
        log.info("Spider [{}] 配置 [{}]", spider.getName(), config);
    }

}

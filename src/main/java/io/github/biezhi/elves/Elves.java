package io.github.biezhi.elves;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.event.ElvesEvent;
import io.github.biezhi.elves.event.EventManager;
import io.github.biezhi.elves.spider.Spider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Elves
 *
 * @author biezhi
 * @date 2018/1/11
 */
@Slf4j
@NoArgsConstructor
public class Elves {

    List<Spider> spiders = new ArrayList<>();
    Config config;

    public static Elves me(Spider spider) {
        return me(spider, Config.me());
    }

    public static Elves me(Spider spider, Config config) {
        Elves elves = new Elves();
        elves.spiders.add(spider);
        elves.config = config;
        return elves;
    }

    public void start() {
        new ElvesEngine(this).start();
    }

    public Elves onStart(Consumer<Config> consumer) {
        EventManager.registerEvent(ElvesEvent.GLOBAL_STARTED, consumer);
        return this;
    }

}

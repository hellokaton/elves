package io.github.biezhi.elves;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.download.Downloader;
import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.scheduler.Scheduler;
import io.github.biezhi.elves.spider.Spider;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Elves
 *
 * @author biezhi
 * @date 2018/1/11
 */
@Slf4j
@NoArgsConstructor
public class Elves {

    private List<Spider> spiders = new ArrayList<>();
    private Config     config;
    private boolean    isRunning;
    private Downloader downloader;
    private Scheduler       scheduler       = new Scheduler();
    private ExecutorService executorService = Executors.newFixedThreadPool(8);

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
        if (isRunning) {
            throw new RuntimeException("Elves 已经启动");
        }

        isRunning = true;

        // 全局启动事件

        spiders.forEach(spider -> {
            log.info("Spider [{}] 启动...", spider.getName());
            log.info("Spider [{}] 配置 [{}]", spider.getName(), config);
            spider.started();
            spider.getStartUrls().stream().map(url -> this.makeRequest(spider, url)).forEach(scheduler::addRequest);
        });

        // 后台生产
        while (isRunning && scheduler.hasRequest()) {
            Request request = scheduler.nextRequest();
            executorService.submit(new Downloader(request));
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 后台消费

    }

    private Request makeRequest(Spider spider, String url) {
        return new Request(spider, url);
    }

}

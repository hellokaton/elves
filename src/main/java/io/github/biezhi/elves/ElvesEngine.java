package io.github.biezhi.elves;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.download.Downloader;
import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.scheduler.Scheduler;
import io.github.biezhi.elves.spider.Spider;
import io.github.biezhi.elves.utils.ElvesUtils;
import io.github.biezhi.elves.utils.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

/**
 * Elves Engine
 *
 * @author biezhi
 * @date 2018/1/12
 */
@Slf4j
public class ElvesEngine {

    private List<Spider>    spiders;
    private Config          config;
    private boolean         isRunning;
    private Scheduler       scheduler;
    private ExecutorService executorService;

    public ElvesEngine(Elves elves) {
        this.scheduler = new Scheduler();
        this.spiders = elves.spiders;
        this.config = elves.config;
        this.executorService = new ThreadPoolExecutor(config.parallelThreads(), config.parallelThreads(), 0, TimeUnit.MILLISECONDS,
                config.queueSize() == 0 ? new SynchronousQueue<>()
                        : (config.queueSize() < 0 ? new LinkedBlockingQueue<>()
                        : new LinkedBlockingQueue<>(config.queueSize())), new NamedThreadFactory("task"));
    }

    public void start() {
        if (isRunning) {
            throw new RuntimeException("Elves 已经启动");
        }

        isRunning = true;
        // 全局启动事件
        spiders.forEach(spider -> {

            Config conf = config.clone();

            log.info("Spider [{}] 启动...", spider.getName());
            log.info("Spider [{}] 配置 [{}]", spider.getName(), conf);
            spider.setConfig(conf);
            spider.getStartUrls().stream()
                    .map(url -> this.makeRequest(spider, url))
                    .forEach(request -> {
                        spider.getRequests().add(request);
                        scheduler.addRequest(request);
                    });
            spider.started(conf);
        });

        // 后台生产
        Thread downloadTread = new Thread(runDownload());
        downloadTread.setDaemon(true);
        downloadTread.setName("download-thread");
        downloadTread.start();
        // 后台消费
        this.complete();
    }

    private void complete() {
        while (isRunning) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable runDownload() {
        return () -> {
            while (isRunning && scheduler.hasRequest()) {
                Request request = scheduler.nextRequest();
                executorService.submit(new Downloader(request));
                ElvesUtils.sleep(request.getSpider().getConfig().delay());
            }
        };
    }

    private Request makeRequest(Spider spider, String url) {
        return new Request(spider, url);
    }

}

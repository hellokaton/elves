package io.github.biezhi.elves;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.download.Downloader;
import io.github.biezhi.elves.event.ElvesEvent;
import io.github.biezhi.elves.event.EventManager;
import io.github.biezhi.elves.pipeline.Pipeline;
import io.github.biezhi.elves.request.Parser;
import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import io.github.biezhi.elves.response.Result;
import io.github.biezhi.elves.scheduler.Scheduler;
import io.github.biezhi.elves.spider.Spider;
import io.github.biezhi.elves.utils.ElvesUtils;
import io.github.biezhi.elves.utils.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

    ElvesEngine(Elves elves) {
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
        EventManager.fireEvent(ElvesEvent.GLOBAL_STARTED, config);

        spiders.forEach(spider -> {

            Config conf = config.clone();

            log.info("Spider [{}] 启动...", spider.getName());
            log.info("Spider [{}] 配置 [{}]", spider.getName(), conf);
            spider.setConfig(conf);

            List<Request> requests = spider.getStartUrls().stream()
                    .map(spider::makeRequest).collect(Collectors.toList());

            spider.getRequests().addAll(requests);
            scheduler.addRequests(requests);

            EventManager.fireEvent(ElvesEvent.SPIDER_STARTED, conf);

        });

        // 后台生产
        Thread downloadTread = new Thread(() -> {
            while (isRunning) {
                if (!scheduler.hasRequest()) {
                    ElvesUtils.sleep(100);
                    continue;
                }
                Request request = scheduler.nextRequest();
                executorService.submit(new Downloader(scheduler, request));
                ElvesUtils.sleep(request.getSpider().getConfig().delay());
            }
        });
        downloadTread.setDaemon(true);
        downloadTread.setName("download-thread");
        downloadTread.start();
        // 消费
        this.complete();
    }

    private void complete() {
        while (isRunning) {
            if (!scheduler.hasResponse()) {
                ElvesUtils.sleep(100);
                continue;
            }
            Response response = scheduler.nextResponse();
            Parser   parser   = response.getRequest().getParser();
            if (null != parser) {
                Result<?>     result   = parser.parse(response);
                List<Request> requests = result.getRequests();
                if (!ElvesUtils.isEmpty(requests)) {
                    requests.forEach(scheduler::addRequest);
                }
                if (null != result.getItem()) {
                    List<Pipeline> pipelines = response.getRequest().getSpider().getPipelines();
                    pipelines.forEach(pipeline -> pipeline.process(result.getItem(), response.getRequest()));
                }
            }
        }
    }

    public void stop(){
        isRunning = false;
        scheduler.clear();
        log.info("爬虫已经停止.");
    }

}

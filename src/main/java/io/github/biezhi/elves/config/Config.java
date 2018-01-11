package io.github.biezhi.elves.config;

import lombok.ToString;

/**
 * 爬虫全局配置
 *
 * @author biezhi
 * @date 2018/1/11
 */
@ToString
public class Config {

    private long   timeout         = 10_000;
    private long   delay           = 0;
    private int    parallelThreads = Runtime.getRuntime().availableProcessors() * 2;
    private String userAgent       = UserAgent.CHROME_FOR_MAC;
    private int queueSize;

    public static Config me() {
        return new Config();
    }

    public Config timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public long timeout() {
        return this.timeout;
    }

    public Config delay(long delay) {
        this.delay = delay;
        return this;
    }

    public long delay() {
        return this.delay;
    }

    public Config parallelThreads(int parallelThreads) {
        this.parallelThreads = parallelThreads;
        return this;
    }

    public int parallelThreads() {
        return this.parallelThreads;
    }

    public String userAgent() {
        return userAgent;
    }

    public Config userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public int queueSize() {
        return queueSize;
    }

    public Config queueSize(int queueSize) {
        this.queueSize = queueSize;
        return this;
    }

}
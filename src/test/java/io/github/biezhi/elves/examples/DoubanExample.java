package io.github.biezhi.elves.examples;

import io.github.biezhi.elves.Elves;
import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.spider.DoubanSpider;

/**
 * @author biezhi
 * @date 2018/1/11
 */
public class DoubanExample {

    public static void main(String[] args) {
        DoubanSpider doubanSpider = new DoubanSpider("豆瓣电影");
        Elves.me(doubanSpider, Config.me()).start();
    }
}

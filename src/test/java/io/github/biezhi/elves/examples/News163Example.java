package io.github.biezhi.elves.examples;

import io.github.biezhi.elves.Elves;
import io.github.biezhi.elves.spider.News163Spider;

/**
 * 网易新闻爬虫示例
 *
 * @author biezhi
 * @date 2018/1/11
 */
public class News163Example {

    public static void main(String[] args) {
        News163Spider news163Spider = new News163Spider("网易新闻");
        news163Spider.startUrls("http://news.163.com/rank");

        Elves.me(news163Spider).start();
    }

}

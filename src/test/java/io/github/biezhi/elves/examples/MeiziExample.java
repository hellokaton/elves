package io.github.biezhi.elves.examples;

import io.github.biezhi.elves.Elves;
import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.spider.MeiziSpider;

/**
 * 妹子图示例
 *
 * @author biezhi
 * @date 2018/1/12
 */
public class MeiziExample {

    public static void main(String[] args) {
        MeiziSpider meiziSpider = new MeiziSpider("妹子图");
        Elves.me(meiziSpider, Config.me().delay(3000)).start();
    }

}

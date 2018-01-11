package io.github.biezhi.elves.spider;

import io.github.biezhi.elves.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

/**
 * @author biezhi
 * @date 2018/1/11
 */
@Slf4j
public class DoubanSpider extends Spider {

    public DoubanSpider(String name) {
        super(name);
        this.startUrls(
                "https://movie.douban.com/tag/爱情",
                "https://movie.douban.com/tag/喜剧",
                "https://movie.douban.com/tag/动画",
                "https://movie.douban.com/tag/动作",
                "https://movie.douban.com/tag/史诗",
                "https://movie.douban.com/tag/犯罪");
    }

    @Override
    public Spider onStart() {
        this.requests.forEach(request -> {
            request.header("Refer", "https://movie.douban.com");
            request.cookie("bid", randomBid());
        });
        return this;
    }

    @Override
    public void parse(Response response) {
        log.info("响应[{}]", response.body());
    }

    private String randomBid() {
        String       base   = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random       random = new Random();
        StringBuffer sb     = new StringBuffer();
        for (int i = 0; i < 11; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}


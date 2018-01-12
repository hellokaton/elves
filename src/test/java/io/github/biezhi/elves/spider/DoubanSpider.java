package io.github.biezhi.elves.spider;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.pipeline.Pipeline;
import io.github.biezhi.elves.request.Parser;
import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import io.github.biezhi.elves.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
    public Spider started(Config config) {

        this.pipelines.add((Pipeline<String>) (item, request) -> log.info("保存到文件: {}", item));

        this.requests.forEach(request -> {
            request.header("Refer", "https://movie.douban.com");
            request.cookie("bid", randomBid());
        });
        return this;
    }

    @Override
    public Result<String> parse(Response response) {
        Result<String> result   = new Result<>();
        Elements       elements = response.body().css("#content table .pl2 a");
        log.info("elements size: {}", elements.size());

        List<Request<String>> requests = elements.stream()
                .map(element -> element.attr("href"))
                .map(href -> DoubanSpider.this.makeRequest(href, new DetailParser()))
                .collect(Collectors.toList());
        result.addRequests(requests);

        // 获取下一页 URL
        Elements nextEl = response.body().css("#content > div > div.article > div.paginator > span.next > a");
        if (null != nextEl && nextEl.size() > 0) {
            String          nextPageUrl = nextEl.get(0).attr("href");
            Request<String> nextReq     = DoubanSpider.this.makeRequest(nextPageUrl, this::parse);
            result.addRequest(nextReq);
        }

        return result;
    }

    static class DetailParser implements Parser<String> {
        @Override
        public Result<String> parse(Response response) {
            Elements elements = response.body().css("h1 span[property='v:itemreviewed']");
            String   text     = elements.get(0).text();
            return new Result<>(text);
        }
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
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
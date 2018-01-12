package io.github.biezhi.elves.spider;

import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.config.UserAgent;
import io.github.biezhi.elves.pipeline.Pipeline;
import io.github.biezhi.elves.request.Parser;
import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import io.github.biezhi.elves.response.Result;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author biezhi
 * @date 2018/1/12
 */
@Slf4j
public class MeiziSpider extends Spider {

    private String storageDir = "/Users/biezhi/Desktop/meizi";

    public MeiziSpider(String name) {
        super(name);
        this.startUrls(
                "http://www.meizitu.com/a/pure.html",
                "http://www.meizitu.com/a/cute.html",
                "http://www.meizitu.com/a/sexy.html",
                "http://www.meizitu.com/a/fuli.html",
                "http://www.meizitu.com/a/legs.html");
    }

    @Override
    public void onStart(Config config) {
        this.addPipeline((Pipeline<List<String>>) (item, request) -> {
            item.parallelStream().forEach(imgUrl -> {
                log.info("开始下载: {}", imgUrl);
                io.github.biezhi.request.Request.get(imgUrl)
                        .header("Referer", request.getUrl())
                        .header("User-Agent", UserAgent.CHROME_FOR_MAC)
                        .connectTimeout(config.timeout())
                        .readTimeout(config.timeout())
                        .receive(new File(storageDir, System.currentTimeMillis() + ".jpg"));
            });

            log.info("[{}] 图片下载 OJ8K.", request.getUrl());
        });
    }

    @Override
    protected Result parse(Response response) {
        Result   result   = new Result<>();
        Elements elements = response.body().css("#maincontent > div.inWrap > ul > li:nth-child(1) > div > div > a");
        log.info("elements size: {}", elements.size());

        List<Request<List<String>>> requests = elements.stream()
                .map(element -> element.attr("href"))
                .map(href -> MeiziSpider.this.makeRequest(href, new PictureParser()))
                .collect(Collectors.toList());
        result.addRequests(requests);

        // 获取下一页 URL
        Elements nextEl = response.body().css("#wp_page_numbers > ul > li:nth-child(17) > a");
        if (null != nextEl && nextEl.size() > 0) {
            String          nextPageUrl = "http://www.meizitu.com/a/" + nextEl.get(0).attr("href");
            Request<String> nextReq     = MeiziSpider.this.makeRequest(nextPageUrl, this::parse);
            result.addRequest(nextReq);
        }
        return result;
    }

    static class PictureParser implements Parser<List<String>> {
        @Override
        public Result<List<String>> parse(Response response) {
            Elements     elements = response.body().css("#picture > p > img");
            List<String> src      = elements.stream().map(element -> element.attr("src")).collect(Collectors.toList());
            return new Result<>(src);
        }
    }

}

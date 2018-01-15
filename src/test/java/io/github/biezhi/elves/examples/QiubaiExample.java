package io.github.biezhi.elves.examples;

import io.github.biezhi.elves.Elves;
import io.github.biezhi.elves.config.Config;
import io.github.biezhi.elves.pipeline.Pipeline;
import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import io.github.biezhi.elves.response.Result;
import io.github.biezhi.elves.spider.Spider;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 糗事百科示例
 *
 * @author biezhi
 * @date 2018/1/15
 */
public class QiubaiExample {

    private static final String BASE_URL = "https://www.qiushibaike.com";

    @Slf4j
    static class QiubaiSpider extends Spider {
        public QiubaiSpider(String name) {
            super(name);
            this.startUrls(BASE_URL);
        }

        @Override
        public void onStart(Config config) {
            this.addPipeline((Pipeline<List<String>>) (items, request) -> {
                log.info("=== 段子来了 ===");
                items.forEach(item -> System.out.println("\r\n" + item + "\r\n============END==========\r\n"));
            });
        }

        @Override
        protected Result parse(Response response) {
            Result result = new Result();

            List<String> items = response.body().css("#content-left div.article div.content span").stream()
                    .map(element -> element.text().replace("<br/>", "\r\n"))
                    .collect(Collectors.toList());

            result.setItem(items);

            // 下一页
            Optional<Element> nextEl = response.body().css("ul.pagination a span").stream()
                    .filter(element -> "下一页".equals(element.text()))
                    .map(Element::parent)
                    .findFirst();
            if (nextEl.isPresent()) {
                String          nextPageUrl = BASE_URL + nextEl.get().attr("href");
                Request<String> nextReq     = QiubaiSpider.this.makeRequest(nextPageUrl, this::parse);
                result.addRequest(nextReq);
            }
            return result;
        }
    }

    public static void main(String[] args) {
        QiubaiSpider qiubaiSpider = new QiubaiSpider("糗事百科");
        Elves.me(qiubaiSpider, Config.me().delay(2000)).start();
    }

}

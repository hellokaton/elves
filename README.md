# Elves

一个轻量级的爬虫框架设计与实现。

![](https://img.shields.io/travis/biezhi/elves.svg)
![](https://img.shields.io/maven-central/v/io.github.biezhi/elves.svg)
![](https://img.shields.io/badge/license-MIT-FF0080.svg)

## 特性

- 事件驱动
- 易于定制
- 多线程执行
- `CSS` 选择器和 `XPath` 支持
- 多数据源支持

## 架构图

<p align="center">
    <img src="docs/static/elves.png" width="70%"/>
</p>

## 快速上手

搭建一个爬虫程序需要进行这么几步操作

1. 编写一个爬虫类继承自 `Spider`
2. 设置要抓取的 URL 列表
3. 实现 `Spider` 的 `parse` 方法
4. 添加 `Pipeline` 处理 `parse` 过滤后的数据

举个栗子:

```java
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
    public void onStart(Config config) {
        this.addPipeline((Pipeline<String>) (item, request) -> log.info("保存到文件: {}", item));
        this.requests.forEach(this::resetRequest);
    }

    private Request resetRequest(Request request) {
        request.header("Refer", "https://movie.douban.com");
        request.cookie("bid", randomBid());
        return request;
    }

    @Override
    public Result<String> parse(Response response) {
        Result<String> result   = new Result<>();
        Elements       elements = response.body().css("#content table .pl2 a");
        List<Request> requests = elements.stream()
                .map(element -> element.attr("href"))
                .map(href -> DoubanSpider.this.makeRequest(href, new DetailParser()))
                .map(this::resetRequest)
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
    
}

public static void main(String[] args) {
    DoubanSpider doubanSpider = new DoubanSpider("豆瓣电影");
    Elves.me(doubanSpider, Config.me()).start();
}
```

## 爬虫例子

- [豆瓣电影](src/test/java/io/github/biezhi/elves/examples/DoubanExample.java)
- [网易新闻]()
- [糗事百科]()
- [妹。。。妹子图](src/test/java/io/github/biezhi/elves/examples/MeiziExample.java)

## 开源协议

[MIT](LICENSE)
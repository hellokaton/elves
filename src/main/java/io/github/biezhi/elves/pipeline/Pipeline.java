package io.github.biezhi.elves.pipeline;

import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.spider.Spider;

/**
 * 数据处理接口
 *
 * @author biezhi
 * @date 2018/1/12
 */
public interface Pipeline<T> {

    void process(T item, Request request);

}

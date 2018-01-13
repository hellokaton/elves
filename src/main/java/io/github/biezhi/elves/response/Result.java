package io.github.biezhi.elves.response;

import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.utils.ElvesUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 响应结果封装
 * <p>
 * 存储 Item 数据和新添加的 Request 列表
 *
 * @author biezhi
 * @date 2018/1/12
 */
@Data
@NoArgsConstructor
public class Result<T> {

    private List<Request> requests = new ArrayList<>();
    private T item;

    public Result(T item) {
        this.item = item;
    }

    public Result addRequest(Request request) {
        this.requests.add(request);
        return this;
    }

    public Result addRequests(List<Request> requests) {
        if (!ElvesUtils.isEmpty(requests)) {
            this.requests.addAll(requests);
        }
        return this;
    }

}

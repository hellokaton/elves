package io.github.biezhi.elves.scheduler;

import io.github.biezhi.elves.request.Request;
import io.github.biezhi.elves.response.Response;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 爬虫调度器
 *
 * @author biezhi
 * @date 2018/1/12
 */
@Slf4j
public class Scheduler {

    private BlockingQueue<Request>  pending = new LinkedBlockingQueue<>();
    private BlockingQueue<Response> result  = new LinkedBlockingQueue<>();

    public void addRequest(Request request) {
        try {
            this.pending.put(request);
        } catch (InterruptedException e) {
            log.error("向调度器添加 Request 出错", e);
        }
    }

    public void addResponse(Response response) {
        try {
            this.result.put(response);
        } catch (InterruptedException e) {
            log.error("向调度器添加 Response 出错", e);
        }
    }

    public boolean hasRequest() {
        return pending.size() > 0;
    }

    public Request nextRequest() {
        try {
            return pending.take();
        } catch (InterruptedException e) {
            log.error("从调度器获取 Request 出错", e);
            return null;
        }
    }

    public boolean hasResponse() {
        return result.size() > 0;
    }

    public Response nextResponse() {
        try {
            return result.take();
        } catch (InterruptedException e) {
            log.error("从调度器获取 Response 出错", e);
            return null;
        }
    }

    public void addRequests(List<Request> requests) {
        requests.forEach(this::addRequest);
    }

    public void clear() {
        pending.clear();
    }

}

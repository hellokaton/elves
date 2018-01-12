package io.github.biezhi.elves.request;

import io.github.biezhi.elves.response.Result;
import io.github.biezhi.elves.response.Response;

/**
 * @author biezhi
 * @date 2018/1/12
 */
public interface Parser<T> {

    Result<T> parse(Response response);

}

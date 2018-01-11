package io.github.biezhi.elves.spider;

import io.github.biezhi.elves.parser.Parser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author biezhi
 * @date 2018/1/11
 */
@Data
@NoArgsConstructor
public class Spider {

    private String name;
    private List<String> startUrls = new ArrayList<>();
    private Parser parser;

    public Spider(String name) {
        this.name = name;
    }

    public Spider startUrls(String...urls){
        this.startUrls.addAll(Arrays.asList(urls));
        return this;
    }

    public Spider parser(Parser parser){
        this.parser = parser;
        return this;
    }

}

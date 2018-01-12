package io.github.biezhi.elves.event;

import io.github.biezhi.elves.config.Config;

import java.util.*;
import java.util.function.Consumer;

/**
 * 事件管理器
 *
 * @author biezhi
 * @date 2018/1/11
 */
public class EventManager {

    private static final Map<ElvesEvent, List<Consumer<Config>>> elvesEventConsumerMap = new LinkedHashMap<>();

    public static List<Consumer<Config>> getEvent(ElvesEvent elvesEvent) {
        return elvesEventConsumerMap.get(elvesEvent);
    }

    public static void registerEvent(ElvesEvent elvesEvent, Consumer<Config> consumer) {
        List<Consumer<Config>> consumers = getEvent(elvesEvent);
        if (null == consumers) {
            consumers = new ArrayList<>();
        }
        consumers.add(consumer);
        elvesEventConsumerMap.put(elvesEvent, consumers);
    }

    public static void fireEvent(ElvesEvent elvesEvent, Config config) {
        Optional.ofNullable(elvesEventConsumerMap.get(elvesEvent)).ifPresent(consumers -> consumers.forEach(consumer -> consumer.accept(config)));
    }

}
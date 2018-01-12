package io.github.biezhi.elves.utils;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * Elves Utils
 *
 * @author biezhi
 * @date 2018/1/12
 */
public class ElvesUtils {

    public static void sleep(long time){
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <E> boolean isEmpty(Collection<E> collection){
        return null == collection || collection.size() == 0;
    }

}

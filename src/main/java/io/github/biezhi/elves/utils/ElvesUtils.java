package io.github.biezhi.elves.utils;

import java.util.concurrent.TimeUnit;

/**
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

}

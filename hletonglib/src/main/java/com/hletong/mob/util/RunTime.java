package com.hletong.mob.util;

import com.orhanobut.logger.Logger;

/**
 * Created by user on 6/30/2016.
 */
public class RunTime {
    private static long startTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static void end(String description) {
        long endTime = System.currentTimeMillis();
        Logger.e(description + ":" + (endTime - startTime));
    }

    public static void end() {
        end("total time");
    }
}

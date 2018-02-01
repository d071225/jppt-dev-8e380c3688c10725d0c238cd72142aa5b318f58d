package com.hletong.hyc.util;

import java.util.concurrent.ExecutorService;

/**
 * Created by ddq on 2017/3/16.
 */

public class Executors {
    private ExecutorService mExecutor;
    private static Executors executors;

    private Executors() {
        mExecutor = java.util.concurrent.Executors.newSingleThreadScheduledExecutor();
    }

    public static Executors getExecutors(){
        if (executors == null)
            executors = new Executors();
        return executors;
    }

    public void execute(Runnable r){
        mExecutor.execute(r);
    }

    public void destory(){
        mExecutor.shutdown();
    }
}

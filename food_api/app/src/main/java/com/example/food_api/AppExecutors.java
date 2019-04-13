package com.example.food_api;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    static AppExecutors instance;
    public static AppExecutors getInstance(){
        if(instance==null)
            instance=new AppExecutors();
        return instance;

    }

    private final ScheduledExecutorService mNetworkIo=Executors.newScheduledThreadPool(3);
    public ScheduledExecutorService getmNetworkIo(){
        return mNetworkIo;
    }

}

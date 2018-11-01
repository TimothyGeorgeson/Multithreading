package com.example.user.multithreading.jobs;

import android.util.EventLog;

import com.example.user.multithreading.model.MyMessageEvent;

import org.greenrobot.eventbus.EventBus;

public class MyEventHandler extends Thread {

    public void run(){
        super.run();

        MyMessageEvent myMessageEvent = new MyMessageEvent("Default data");

        EventBus.getDefault().post(myMessageEvent);

        myMessageEvent.setData("Task Running");
        EventBus.getDefault().post(myMessageEvent);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        myMessageEvent.setData("Task completed");
        EventBus.getDefault().post(myMessageEvent);
    }
}

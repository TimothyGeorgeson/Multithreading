package com.example.user.multithreading.jobs;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.example.user.multithreading.MyTask;
import com.example.user.multithreading.utils.ThreadUtils;

public class MyThread extends Thread {

    public static final String TAG = MyThread.class.getSimpleName() + "_TAG";

    int iterations;

    Handler handler = new Handler(Looper.getMainLooper());
    TextView tvMain;

    public MyThread(int iterations, TextView tvMain) {
        this.iterations = iterations;
        this.tvMain = tvMain;

    }

    @Override
    public void run() {
        super.run();

        //start the task
        try {
            MyTask.start(iterations, Thread.currentThread().getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        handler.post(new Runnable(){
            @Override
            public void run() {
                Log.d(TAG, "run: " + ThreadUtils.print((currentThread())));
                tvMain.setText("Update from myThread class");
            }
        });

        //use post delayed
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Log.d(TAG, "run: " + ThreadUtils.print((currentThread())));
                tvMain.setText("Delayed Update");
            }
        }, 2000);
    }
}

package com.example.user.multithreading;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.multithreading.jobs.MyAsyncTask;
import com.example.user.multithreading.jobs.MyEventHandler;
import com.example.user.multithreading.jobs.MyRunnable;
import com.example.user.multithreading.jobs.MyThread;
import com.example.user.multithreading.model.MyMessageEvent;
import com.example.user.multithreading.utils.HandlerUtils;
import com.example.user.multithreading.utils.ThreadUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity implements Handler.Callback{

    public static final String TAG = MainActivity.class.getSimpleName() + "_TAG";
    private TextView tvMain;
    private TextView tvSecond;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMain = findViewById(R.id.tvMain);
        tvSecond = findViewById(R.id.tvSecond);

        handler = new Handler(this);

    }

    public void onThreads(View view) {

        ThreadUtils.print(Thread.currentThread());

        MyThread myThread = new MyThread(5, tvMain);
        myThread.start();
    }

    public void onRunnable(View view) {
        ThreadUtils.print(Thread.currentThread());

        MyRunnable myRunnable = new MyRunnable(5, handler);

        switch (view.getId())
        {
            case (R.id.btnRunnable):
                Thread thread = new Thread(myRunnable);
                thread.start(); //created thread, and runnable runs in it
                break;
            case (R.id.btnRunnableRun):
                myRunnable.run(); //runs on the currently running thread (this time it's main)
                break;
        }
    }

    private void printCurrentThread() {
        Log.d(TAG, "onThreads: Current Thread: " + Thread.currentThread().getName());
    }

    public void onAsyncTask(View view) {

        ThreadUtils.print(Thread.currentThread());
        MyAsyncTask at1 = new MyAsyncTask(tvMain);
        MyAsyncTask at2 = new MyAsyncTask(tvSecond);
        //at1.execute("Initial params from activity");
        at1.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR, "Initial params from activity");
        at2.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR, "AT2 Initial params");
    }


    @Override
    public boolean handleMessage(Message msg) {

        tvMain.setText(HandlerUtils.getString(msg));

        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MyMessageEvent myMessageEvent) {
        tvMain.setText(myMessageEvent.getData());
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //BOTH of these are subscribed, they should both run
    public void onMessageEventSecond(MyMessageEvent myMessageEvent) {
        Toast.makeText(this, myMessageEvent.getData(), Toast.LENGTH_SHORT).show();
    }

    public void onEventHandler(View view) {
        MyEventHandler myEventHandler = new MyEventHandler();
        myEventHandler.start();
    }
}

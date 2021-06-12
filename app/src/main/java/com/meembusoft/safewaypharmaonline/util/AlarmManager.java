package com.meembusoft.safewaypharmaonline.util;

import android.os.Handler;
import android.os.HandlerThread;

import java.util.Random;

public class AlarmManager {
    // Callback for alarm trigger --------------------------------------------------------------------------------------
    public interface Callback {
        public void onTriggered();
    }
    // -----------------------------------------------------------------------------------------------------------------

    private class Timer implements Runnable {
        private long timeout;
        private boolean repeating;

        public Timer(long timeout, boolean repeating) {
            this.timeout = timeout;
            this.repeating = repeating;
        }

        @Override
        public void run() {
            mCallback.onTriggered();
            if (repeating) {
                mHandler.postDelayed(this, timeout);
            }
        }
    }

    private String mId;
    private Callback mCallback;
    private boolean mSet = false;
    private Handler mHandler;

    public AlarmManager(String id, Callback callback) {
        mId = id;
        mCallback = callback;
    }

    public void set(long timeout, boolean repeating) {
        synchronized (this) {
            if (!mSet) {
                HandlerThread thread = new HandlerThread(mId + "-" + new Random().nextLong());
                thread.start();
                mHandler = new Handler(thread.getLooper());
                mHandler.post(new Timer(timeout, repeating));
                mSet = true;
            }
        }
    }

    public void cancel() {
        synchronized (this) {
            if (mSet) {
                mSet = false;
                mHandler.getLooper().quit();
            }
        }
    }
}

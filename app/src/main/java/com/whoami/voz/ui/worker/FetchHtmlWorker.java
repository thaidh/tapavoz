package com.whoami.voz.ui.worker;


import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class FetchHtmlWorker extends Thread {
    public static final String TAG = "FetchHtmlWorker";

    private static List<FetchHtmlTask> queue = new ArrayList<FetchHtmlTask>();

    private static FetchHtmlWorker worker = null;

    private volatile boolean running = true;

    public synchronized static void startWaitingForDbTask() {
        if (worker == null) {
            synchronized (FetchHtmlWorker.class) {
                if (worker == null) {
                    worker = new FetchHtmlWorker();
                }
            }
        }
    }

    private FetchHtmlWorker() {
        super("FetchHtmlWorker");
        if (worker == null) {
            worker = this;
            start();
        }
    }

    public static void queueTask(FetchHtmlTask fetchHtmlTask) {
        startWaitingForDbTask();
        if (worker != null) {
            synchronized (worker) {
                if (fetchHtmlTask.isImportant()) {
                    queue.add(0, fetchHtmlTask);
                } else {
                    queue.add(fetchHtmlTask);
                }
                worker.notify();
            }
        }
    }

    public static boolean isEmpty() {
        if (worker == null) return true;
        synchronized (worker) {
            return queue == null || queue.isEmpty();
        }
    }

    public static boolean isEmptyDBQueueTask() {
        return queue.isEmpty();
    }

    @Override
    public void run() {
        Log.w(TAG, "Start FetchHtmlWorker");

        while (running) {
            synchronized (this) {
                if (queue.isEmpty()) {
                    Log.w(TAG, "Waiting for new db requests...");
                    try {
                        wait();
                    } catch (Exception e) {
                    }
                }
            }

            if (!running) {
                break;
            }

            doTask();
        }

        Log.w(TAG, "Stop FetchHtmlWorker");
        worker = null;
    }

    private void doTask() {
        try {
            Log.w(TAG, "Execute a HTML task");
            FetchHtmlTask fetchHtmlTask = queue.remove(0);
            if (fetchHtmlTask != null)
                fetchHtmlTask.doTask();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stopThread() {
        if (worker != null) {
            Log.w(TAG, "Try to stop FetchHtmlWorker");
            synchronized (worker) {
                while (!queue.isEmpty()) {
                    worker.doTask();
                }

                worker.running = false;
                worker.notify();
            }

            if (worker != null) {
                worker.interrupt();
            }
        }
    }
}

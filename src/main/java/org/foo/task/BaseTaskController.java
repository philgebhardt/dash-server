package org.foo.task;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phil on 2/14/16.
 */
public class BaseTaskController implements TaskController {

    private Map<String, Thread> threads;

    public BaseTaskController() {
        threads = new HashMap<>();
    }
    @Override
    public void putTask(String key, Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        threads.put(key, thread);
    }

    @Override
    public Thread getTaskThread(String key) {
        return threads.get(key);
    }

    @Override
    public void start(String key) throws RuntimeException {
        Thread thread = getTaskThread(key);
        if(thread==null) throw new RuntimeException(String.format("thread with key '%s' not found", key));
        thread.start();
    }

    @Override
    public void startAll() {
        threads.values().forEach(Thread::start);
    }

    @Override
    public void stop(String key) {
        Thread thread = getTaskThread(key);
        if(thread==null) throw new RuntimeException(String.format("thread with key '%s' not found", key));
        thread.interrupt();
    }

    @Override
    public void stopAll() {
        threads.values().forEach(Thread::interrupt);
    }
}

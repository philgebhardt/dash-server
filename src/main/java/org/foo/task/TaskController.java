package org.foo.task;

/**
 * Created by phil on 2/14/16.
 */
public interface TaskController {

    public void putTask(String key, Runnable r);
    public Thread getTaskThread(String key);

    public void start(String key);
    public void startAll();

    public void stop(String key);
    public void stopAll();
}

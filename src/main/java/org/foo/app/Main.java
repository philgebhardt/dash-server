package org.foo.app;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import org.foo.factory.ListenerFactory;
import org.foo.loader.ButtonLoader;
import org.foo.task.BaseTaskController;
import org.foo.task.TaskController;
import org.pcap4j.core.*;


@SuppressWarnings("javadoc")
public class Main {

  public static void main(String[] commandArgs) throws Exception {

    StartupConfig config = new StartupConfig(commandArgs);
    ListenerFactory listenerFactory = ListenerFactory.newInstance(config);
    ButtonLoader buttonLoader = new ButtonLoader(listenerFactory.getButtonDAO());
    buttonLoader.load(new File(".buttons.json"));

    TaskController controller = new BaseTaskController();
    controller.putTask("buttonlistener", listenerFactory.newButtonEventListener());
    controller.putTask("weblistener", listenerFactory.newWebListener());
    controller.startAll();

    try {
      _chill();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      // clean up
      controller.stopAll();
    }
  }

  private static void _chill() throws InterruptedException {
    while(true) {
      Thread.sleep(1*1000);
    }
  }

}
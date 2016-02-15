package org.foo;

import java.net.InetAddress;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.dao.impl.SQLiteButtonEventDAO;
import org.foo.button.model.ButtonEvent;
import org.foo.task.BaseTaskController;
import org.foo.task.TaskController;
import org.foo.thread.ButtonEventListener;
import org.foo.thread.SparkWebListener;
import org.pcap4j.core.*;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("javadoc")
public class Main {

  public static void main(String[] args) throws PcapNativeException, NotOpenException, SQLException {

    ButtonEventDAO dao = new SQLiteButtonEventDAO();
    TaskController controller = new BaseTaskController();
    controller.putTask("buttonlistener", new ButtonEventListener("localhost", "", dao));
    controller.putTask("weblistener", new SparkWebListener(dao));
    controller.startAll();

    while (true) {
      try {
        Thread.sleep(1 * 1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        controller.stopAll();
      }
    }
  }
}
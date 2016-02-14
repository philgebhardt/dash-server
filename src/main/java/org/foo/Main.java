package org.foo;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.dao.impl.JsonButtonEventDAO;
import org.foo.button.model.ButtonEvent;
import org.pcap4j.core.*;
import org.pcap4j.core.BpfProgram.BpfCompileMode;
import org.pcap4j.core.PcapNetworkInterface.PromiscuousMode;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SuppressWarnings("javadoc")
public class Main {

  private static final String COUNT_KEY
    = Main.class.getName() + ".count";
  private static final int COUNT
    = Integer.getInteger(COUNT_KEY, 5);

  private static final String READ_TIMEOUT_KEY
    = Main.class.getName() + ".readTimeout";
  private static final int READ_TIMEOUT
    = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]

  private static final String SNAPLEN_KEY
    = Main.class.getName() + ".snaplen";
  private static final int SNAPLEN
    = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]
  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static final ButtonEventDAO DAO = new JsonButtonEventDAO();

  public static void main(String[] args) throws PcapNativeException, NotOpenException {
    String ip = args.length > 0 ? args[0] : "localhost";
    String filter = args.length > 1 ? args[1] : "";

    System.out.println(COUNT_KEY + ": " + COUNT);
    System.out.println(READ_TIMEOUT_KEY + ": " + READ_TIMEOUT);
    System.out.println(SNAPLEN_KEY + ": " + SNAPLEN);
    System.out.println("\n");

    PcapNetworkInterface nif;
    try {
//      nif = new NifSelector().selectNetworkInterface();
      InetAddress addr = InetAddress.getByName(ip);
      nif = Pcaps.getDevByAddress(addr);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    if (nif == null) {
      return;
    }

    System.out.println(nif.getName() + "(" + nif.getDescription() + ")");

    final PcapHandle handle
      = nif.openLive(SNAPLEN, PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);

    if (filter.length() != 0) {
      handle.setFilter(
        filter,
        BpfCompileMode.OPTIMIZE
      );
    }

    PacketListener hardwareAddrListener
      = new PacketListener() {
          /*
              sniff for packets and analyze the hardware address
           */
          public void gotPacket(Packet packet) {
            if(packet instanceof EthernetPacket) {
              EthernetPacket eth = (EthernetPacket) packet;
              String addr = eth.getHeader().getSrcAddr().toString();
              LOGGER.info(addr);
              Date now = new Date();
              ButtonEvent event = new ButtonEvent();
              event.setId(addr);
              event.setDtmOccured(now);
              try {
                DAO.save(event);
              } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
              }
            }
          }
        };

    try {
      handle.loop(COUNT, hardwareAddrListener);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    handle.close();
  }

}
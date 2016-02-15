package org.foo.thread;

import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.ButtonEvent;
import org.foo.task.ButtonEventDAOTask;
import org.pcap4j.core.*;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Date;

/**
 * Created by phil on 2/14/16.
 */
public class ButtonEventListener extends ButtonEventDAOTask {

    private static final String COUNT_KEY
            = ButtonEventListener.class.getName() + ".count";
    private static final int COUNT
            = Integer.getInteger(COUNT_KEY, -1);

    private static final String READ_TIMEOUT_KEY
            = ButtonEventListener.class.getName() + ".readTimeout";
    private static final int READ_TIMEOUT
            = Integer.getInteger(READ_TIMEOUT_KEY, 10); // [ms]

    private static final String SNAPLEN_KEY
            = ButtonEventListener.class.getName() + ".snaplen";
    private static final int SNAPLEN
            = Integer.getInteger(SNAPLEN_KEY, 65536); // [bytes]
    private static final Logger LOGGER = LoggerFactory.getLogger(ButtonEventListener.class);
    private static final String IPADDRESS = ButtonEventListener.class.getName().toString() + "#IPADDRESS";
    private static final String FILTER = ButtonEventListener.class.getName().toString() + "#FILTER";
    private String ip;
    private String filter;

    public ButtonEventListener(String ip, String filter, ButtonEventDAO dao) {
        super(dao);
        this.filter = filter;
        this.ip = ip;
    }

    @Override
    public void run() {
        System.out.println(COUNT_KEY + ": " + COUNT);
        System.out.println(READ_TIMEOUT_KEY + ": " + READ_TIMEOUT);
        System.out.println(SNAPLEN_KEY + ": " + SNAPLEN);
        System.out.println("\n");

        PcapNetworkInterface nif;
        try {
            InetAddress addr = InetAddress.getByName(ip);
            nif = Pcaps.getDevByAddress(addr);
            if (nif == null) {
                return;
            }
            System.out.println(nif.getName() + "(" + nif.getDescription() + ")");
            final PcapHandle handle
                    = nif.openLive(SNAPLEN, PcapNetworkInterface.PromiscuousMode.PROMISCUOUS, READ_TIMEOUT);


            if (filter.length() != 0) {
                handle.setFilter(
                        filter,
                        BpfProgram.BpfCompileMode.OPTIMIZE
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
                            getButtonEventDAO().save(event);
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
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void handleError(Exception e) {
        throw new RuntimeException("CRASH", e);
    }
}
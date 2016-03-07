package org.foo.listener;

import org.foo.button.dao.ButtonDAO;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.ButtonEvent;
import org.foo.util.ButtonEventCache;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by phil on 2/15/16.
 */
public class ExistingButtonEventListener extends ButtonEventListener {


    private static final Logger LOGGER = LoggerFactory.getLogger(ExistingButtonEventListener.class);

    public ExistingButtonEventListener(String iface, ButtonEventDAO buttonEventDAO, ButtonDAO buttonDAO) {
        this(iface, "usedb", buttonEventDAO, buttonDAO);
    }

    public ExistingButtonEventListener(String iface, String filter, ButtonEventDAO buttonEventDAO, ButtonDAO buttonDAO) {
        super(buttonEventDAO, buttonDAO);
        setInterface(iface);
        setFilter(filter);
    }

    @Override
    public void handlePacket(Packet packet) {
        if(packet instanceof EthernetPacket) {
            EthernetPacket eth = (EthernetPacket) packet;
            String addr1 = eth.getHeader().getSrcAddr().toString();
            if(LOGGER.isDebugEnabled()) {
                LOGGER.debug(packet.toString());
            } else {
                LOGGER.info("signal received (addr={})", addr1);
            }
            Date now = new Date();
            ButtonEvent event = new ButtonEvent();
            event.setId(addr1);
            event.setDtmOccured(now);
            try {
                getButtonEventDAO().save(event);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
}

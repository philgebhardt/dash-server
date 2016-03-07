package org.foo.listener;

import org.foo.button.dao.ButtonDAO;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.dao.impl.SQLiteButtonDAO;
import org.foo.button.model.Button;
import org.foo.button.model.ButtonEvent;
import org.foo.factory.NameFactory;
import org.foo.task.ButtonEventDAOTask;
import org.foo.util.ButtonEventCache;
import org.foo.util.ButtonFilterUtils;
import org.pcap4j.core.*;
import org.pcap4j.packet.EthernetPacket;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by phil on 2/14/16.
 */
public class NewButtonEventListener extends ButtonEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewButtonEventListener.class);
    private ButtonDAO newButtonDAO;
    private ButtonDAO ignoreButtonDAO;

    private ButtonEventCache cache;
    private int occurences;

    public NewButtonEventListener(String iface, ButtonEventDAO buttonEventDAO, ButtonDAO buttonDAO, ButtonDAO ignoreButtonDAO, ButtonDAO newButtonDAO) {
        super(buttonEventDAO, buttonDAO);
        setInterface(iface);
        setOccurences(3);
        // allow the listener to listen for twice the number of packets necessary for discovery
        setCount(getOccurences()*2);
        this.ignoreButtonDAO = ignoreButtonDAO;
        this.newButtonDAO = newButtonDAO;
        this.cache = new ButtonEventCache();
        this.cache.setExpirationTime(30*1000); // 30 seconds expire time on discovery packets
        try {
            // exclude all currently known/ignored buttons
            Collection<Button> buttons = buttonDAO.findAll();
            buttons.addAll(ignoreButtonDAO.findAll());
            setFilter(ButtonFilterUtils.arpExcludeButtons(buttons));
        } catch(Exception e) {
            throw new RuntimeException("CRASH", e);
        }
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
            if(cache.put(event) == getOccurences()) {
                Button button = new Button();
                button.setId(addr1);
                button.setName(NameFactory.newName());
                try{
                    newButtonDAO.save(button);
                    LOGGER.info("button '{}' added to '{}'", event.getId(), ((SQLiteButtonDAO)newButtonDAO).getTableName());
                } catch (SQLException e) {
                    LOGGER.error("failed to save new button '{}' ({})", button.getId(), button.getName());
                }
            }
        }
    }
    public ButtonEventCache getCache() {
        return cache;
    }

    public void setCache(ButtonEventCache cache) {
        this.cache = cache;
    }

    public void setOccurences(int occurences) { this.occurences = occurences; }
    public int getOccurences() {
        return occurences;
    }
}
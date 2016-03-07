package org.foo.listener;

import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.Button;
import org.foo.button.dao.ButtonDAO;
import org.foo.task.ButtonEventDAOTask;
import org.foo.util.ButtonFilterUtils;
import org.pcap4j.core.*;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by phil on 2/14/16.
 */
public abstract class ButtonEventListener extends ButtonEventDAOTask {

    private int count = -1;
    private int readTimeOut = 10;
    
    private static final int SNAPLEN = 65536;
    private static final Logger LOGGER = LoggerFactory.getLogger(ButtonEventListener.class);
    private static final String FILTER_USEDB = "usedb";
    private String ip;
    private String filter;
    public ButtonEventListener(ButtonEventDAO buttonEventDAO, ButtonDAO buttonDAO) {
        super(buttonEventDAO, buttonDAO);
    }

    @Override
    public void run() {
        LOGGER.info("{} starting up.", this.getClass().getName());
        LOGGER.info("listening on interface: {} (filter={})", ip, filter);
        PcapNetworkInterface nif;
        try {
            InetAddress addr = InetAddress.getByName(ip);
            nif = Pcaps.getDevByAddress(addr);
            if (nif == null) {
                throw new RuntimeException("CRASH: no interfaces found");
            }
            final PcapHandle handle = nif.openLive(
                    SNAPLEN,
                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                    readTimeOut);

            if (filter.length() != 0) {
                handle.setFilter(
                        filter,
                        BpfProgram.BpfCompileMode.OPTIMIZE
                );
            }
            PacketListener hardwareAddrListener = this::handlePacket;
            try {
                handle.loop(count, hardwareAddrListener);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
            handle.close();
        } catch (Exception e) {
            handleError(e);
        }
    }

    public abstract void handlePacket(Packet packate);

    private String _parseFilter(String filterParam) {
        String filterStr = filterParam;
        if(FILTER_USEDB.equals(filterParam)) {
            try {
                Collection<Button> buttons = getButtonDAO().findAll();
                filterStr = ButtonFilterUtils.arpIncludeButtons(buttons);
            } catch (SQLException e) {
                throw new RuntimeException("CRASH", e);
            }
        }
        return filterStr;
    }

    private void handleError(Exception e) {
        throw new RuntimeException("CRASH", e);
    }

    public String getInterface() {
        return ip;
    }

    public void setInterface(String iface) {
        this.ip = iface;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = _parseFilter(filter);
    }

    public int getReadTimeOut() {
        return readTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut =  readTimeOut;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
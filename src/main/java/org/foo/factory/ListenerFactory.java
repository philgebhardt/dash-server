package org.foo.factory;

import org.foo.app.StartupConfig;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.dao.impl.SQLiteButtonDAO;
import org.foo.button.dao.impl.SQLiteButtonEventDAO;
import org.foo.button.dao.ButtonDAO;
import org.foo.listener.ButtonEventListener;
import org.foo.listener.ExistingButtonEventListener;
import org.foo.listener.NewButtonEventListener;
import org.foo.listener.SparkWebListener;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by phil on 2/15/16.
 */
public class ListenerFactory {

    private StartupConfig config;
    private ButtonEventDAO buttonEventDAO;
    private final ButtonDAO buttonDAO;
    private ButtonDAO newButtonDAO;
    private ButtonDAO ignoreButtonDAO;

    private ListenerFactory(StartupConfig config) {
        try {
            this.config = config;

            // Create DB Connections for the DAO Objects
            String dbPath = config.getProperty("db", ".dash.db");
            File dbFile = new File(dbPath);
            Connection conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbFile.getAbsolutePath()));
            this.buttonEventDAO = new SQLiteButtonEventDAO(conn);
            ((SQLiteButtonEventDAO)buttonEventDAO).setTableName("button_event");
            this.buttonDAO = new SQLiteButtonDAO(conn);
            ((SQLiteButtonDAO)buttonDAO).setTableName("button");
            this.ignoreButtonDAO = new SQLiteButtonDAO(conn);
            ((SQLiteButtonDAO)ignoreButtonDAO).setTableName("ignore_button");
            this.newButtonDAO = new SQLiteButtonDAO(conn);
            ((SQLiteButtonDAO)newButtonDAO).setTableName("new_button");
        } catch (Exception e) {
            throw new RuntimeException("CRASH", e);
        }
    }

    public ButtonEventListener newButtonEventListener() {
        String iface = config.getProperty("iface", "localhost");
        String filter = config.getProperty("filter", "usedb");
        return new ExistingButtonEventListener(iface, filter, getButtonEventDAO(), getButtonDAO());
    }

    public static ListenerFactory newInstance( StartupConfig config) {
        return new ListenerFactory(config);
    }

    public SparkWebListener newWebListener() {
        return new SparkWebListener(getButtonEventDAO(), getButtonDAO(), getIgnoreButtonDAO(), getNewButtonDAO(), this);
    }

    public ButtonEventDAO getButtonEventDAO() {
        return buttonEventDAO;
    }

    public void setButtonEventDAO(ButtonEventDAO buttonEventDAO) {
        this.buttonEventDAO = buttonEventDAO;
    }

    public ButtonDAO getButtonDAO() {
        return buttonDAO;
    }

    public Runnable newButtonDiscoveryListener() {
        String iface = config.getProperty("iface", "localhost");
        return new NewButtonEventListener(iface, getButtonEventDAO(), getButtonDAO(), getIgnoreButtonDAO(), getNewButtonDAO());
    }

    public ButtonDAO getNewButtonDAO() {
        return newButtonDAO;
    }

    public void setNewButtonDAO(ButtonDAO newButtonDAO) {
        this.newButtonDAO = newButtonDAO;
    }

    public ButtonDAO getIgnoreButtonDAO() {
        return ignoreButtonDAO;
    }

    public void setIgnoreButtonDAO(ButtonDAO ignoreButtonDAO) {
        this.ignoreButtonDAO = ignoreButtonDAO;
    }
}

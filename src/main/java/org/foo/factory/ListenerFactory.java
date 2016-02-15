package org.foo.factory;

import org.foo.app.StartupConfig;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.dao.impl.SQLiteButtonDAO;
import org.foo.button.dao.impl.SQLiteButtonEventDAO;
import org.foo.button.dao.ButtonDAO;
import org.foo.thread.ButtonEventListener;
import org.foo.thread.SparkWebListener;

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

    private ListenerFactory(StartupConfig config) {
        try {
            this.config = config;

            // Create DB Connections for the DAO Objects
            String dbPath = config.getProperty("db", ".dash.db");
            File dbFile = new File(dbPath);
            Connection conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbFile.getAbsolutePath()));
            this.buttonEventDAO = new SQLiteButtonEventDAO(conn);
            this.buttonDAO = new SQLiteButtonDAO(conn);

        } catch (Exception e) {
            throw new RuntimeException("CRASH", e);
        }
    }

    public ButtonEventListener newButtonEventListener() {
        String iface = config.getProperty("iface", "localhost");
        String filter = config.getProperty("filter", "");
        return new ButtonEventListener(iface, filter, getButtonEventDAO(), getButtonDAO());
    }

    public static ListenerFactory newInstance( StartupConfig config) {
        return new ListenerFactory(config);
    }

    public SparkWebListener newWebListener() {
        return new SparkWebListener(getButtonEventDAO(), getButtonDAO());
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
}

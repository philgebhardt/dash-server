package org.foo.factory;

import static org.assertj.core.api.Assertions.*;

import org.foo.app.StartupConfig;
import org.foo.listener.ButtonEventListener;
import org.foo.util.SQLiteDBTest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by phil on 2/15/16.
 */
public class ListenerFactoryTest extends SQLiteDBTest {
    private StartupConfig config;

    @BeforeMethod
    public void setup() throws SQLException, IOException {
        setupDB();
    }

    @AfterClass
    public void teardown() throws IOException, SQLException {
        teardownDB();
    }

    @Test
    public void testSimple() {
        String[] args = new String[] {
                "iface=192.168.0.100",
                "filter=arp",
                "db="+getDBFile().getAbsolutePath()
        };
        config = new StartupConfig(args);
        ListenerFactory factory = ListenerFactory.newInstance(config);
        ButtonEventListener eventListener = factory.newButtonEventListener();
        String iface = eventListener.getInterface();
        assertThat(iface).isEqualTo("192.168.0.100");
    }

    @Test
    public void testUsedb() {
        String[] args = new String[] {
                "iface=192.168.0.100",
                "filter=usedb",
                "db="+getDBFile().getAbsolutePath()
        };
        config = new StartupConfig(args);
        ListenerFactory factory = ListenerFactory.newInstance(config);
        ButtonEventListener eventListener = factory.newButtonEventListener();
        String filter = eventListener.getFilter();
        System.out.println(filter);
        // TODO: Implement a way to test validity of filter given a n arbitrary button_table state
    }
}

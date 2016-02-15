package org.foo.app;

import static org.assertj.core.api.Assertions.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by phil on 2/15/16.
 */
public class StartupConfigTest {
    @Test
    public void cliTest() {
        String[] args = new String[] {
                "iface=192.168.0.100",
                "filter=arp"
        };
        StartupConfig config = new StartupConfig(args);
        assertThat(config.getProperty("iface", "localhost")).isEqualTo("192.168.0.100");
        assertThat(config.getProperty("filter", "")).isEqualTo("arp");
    }
}

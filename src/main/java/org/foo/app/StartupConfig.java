package org.foo.app;

import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.dao.impl.SQLiteButtonEventDAO;

import java.util.Properties;
/**
 * Created by phil on 2/15/16.
 */
public class StartupConfig {

    private Properties properties;
    public StartupConfig(String[] commandArgs) {
        try {
            this.properties = System.getProperties();
            for (String arg : commandArgs) {
                System.out.printf("cli arg = %s\n",arg);
                String[] pair = arg.split("=");
                properties.setProperty(pair[0], pair[1]);
            }
        } catch (Exception e) {
            throw new RuntimeException("CRASH", e);
        }
    }

    public String getProperty(String key, String defaultValue) {
        return (properties.get(key) != null) ? (String) properties.get(key) : defaultValue;
    }
}
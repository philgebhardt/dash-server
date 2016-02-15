package org.foo.loader;

import org.foo.button.dao.impl.SQLiteButtonDAO;
import org.foo.button.dao.ButtonDAO;
import org.foo.util.SQLiteDBTest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by phil on 2/15/16.
 */
public class ButtonLoaderTest extends SQLiteDBTest {

    @BeforeMethod
    public void setup() throws IOException, SQLException {
        setupDB();
    }

    @AfterMethod
    public void teardown() throws IOException, SQLException {
        teardownDB();
    }

    @Test
    public void test() throws SQLException, IOException {
        ButtonDAO buttonDAO = new SQLiteButtonDAO(getConn());
        ButtonLoader loader = new ButtonLoader(buttonDAO);
        loader.load(new File("src/test/resources/buttons.json"));
        assertThat(buttonDAO.findAll()).hasSize(2);

        // Assert that `upsert` works
        loader.load(new File("src/test/resources/buttons.json"));
        assertThat(buttonDAO.findAll()).hasSize(2);
    }
}

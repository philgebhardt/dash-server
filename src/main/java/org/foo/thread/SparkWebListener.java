package org.foo.thread;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.ButtonEvent;
import org.foo.task.ButtonEventDAOTask;

import java.text.SimpleDateFormat;
import java.util.Collection;

import static spark.Spark.get;

/**
 * Created by phil on 2/14/16.
 */
public class SparkWebListener extends ButtonEventDAOTask {

    private ObjectMapper mapper;

    public SparkWebListener(ButtonEventDAO dao) {
        super(dao);
        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
    }

    @Override
    public void run() {

        get("/events", (req, res) -> {
            Collection<ButtonEvent> events = getButtonEventDAO().findAll();
            res.header("Content-Type", "application/json");
            return mapper.writeValueAsString(events);
        });

        get("/events/:id", (req, res) -> {
            Collection<ButtonEvent> events = getButtonEventDAO().findAllById(req.params("id"));
            res.header("Content-Type", "application/json");
            return mapper.writeValueAsString(events);
        });

        get("/events/:id", (req, res) -> {
            Collection<ButtonEvent> events = getButtonEventDAO().findAllById(req.params("id"));
            res.header("Content-Type", "application/json");
            return mapper.writeValueAsString(events);
        });
    }
}

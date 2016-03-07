package org.foo.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.foo.button.dao.ButtonEventDAO;
import org.foo.button.model.Button;
import org.foo.button.model.ButtonEvent;
import org.foo.button.dao.ButtonDAO;
import org.foo.factory.ListenerFactory;
import org.foo.task.BaseTaskController;
import org.foo.task.ButtonEventDAOTask;
import org.foo.task.TaskController;
import org.foo.util.JsonDateDeserializer;
import spark.Request;
import spark.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by phil on 2/14/16.
 */
public class SparkWebListener extends ButtonEventDAOTask {

    private ObjectMapper mapper;
    private TaskController controller;
    private ListenerFactory listenerFactory;
    private ButtonDAO newButtonDAO;
    private ButtonDAO ignoreButtonDAO;

    public SparkWebListener(ButtonEventDAO buttonEventDAO, ButtonDAO buttonDAO, ButtonDAO ignoreButtonDAO, ButtonDAO newButtonDAO, ListenerFactory listenerFactory) {
        super(buttonEventDAO, buttonDAO);
        this.ignoreButtonDAO = ignoreButtonDAO;
        this.newButtonDAO = newButtonDAO;
        this.listenerFactory = listenerFactory;
        this.controller = new BaseTaskController();
        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"));
    }

    @Override
    public void run() {
        loadEventsApi();
        loadButtonsApi();
        loadDiscoveryApi();
    }

    private void loadDiscoveryApi() {
        post("/discovery/button", (req, res) -> {
            String id = String.format("ButtonDiscoveryTask%d",System.currentTimeMillis());
            Runnable listener = listenerFactory.newButtonDiscoveryListener();
            controller.putTask(id, listener);
            controller.start(id);
            res.status(200);
            return id;
        });
    }

    private void loadEventsApi() {
        // TODO: Get paths to work with trailing slashes
        get("/events", (req, res) -> {
            DateParams dateParams = _getDateParams(req);
            Collection<ButtonEvent> events = getButtonEventDAO().findAll(dateParams.FROM, dateParams.TILL);
            _prepareResponse(req, res);
            return mapper.writeValueAsString(events);
        });

        get("/events/:id", (req, res) -> {
            String id = req.params("id");
            DateParams dateParams = _getDateParams(req);
            Collection<ButtonEvent> events = getButtonEventDAO().findById(id, dateParams.FROM, dateParams.TILL);
            _prepareResponse(req, res);
            return mapper.writeValueAsString(events);
        });
    }

    private void loadButtonsApi() {
        get("/buttons", (req, res) -> {
            Collection<Button> buttons = getButtonDAO().findAll();
            _prepareResponse(req, res);
            return mapper.writeValueAsString(buttons);
        });

        get("/buttons/:id", (req, res) -> {
            String id = req.params("id");
            Button button = getButtonDAO().findById(id);
            _prepareResponse(req, res);
            return mapper.writeValueAsString(button);
        });

        post("/buttons", (req, res) -> {
            Button button = mapper.readValue(req.body(), Button.class);
            getButtonDAO().save(button);
            res.status(204);
            return "";
        });

        get("/new/buttons", (req, res) -> {
            Collection<Button> buttons = getNewButtonDAO().findAll();
            _prepareResponse(req, res);
            return mapper.writeValueAsString(buttons);
        });

        post("/new/buttons/:id", (req, res) -> {
            String id = req.params("id");
            Button button = getNewButtonDAO().findById(id);
            getButtonDAO().save(button);
            getNewButtonDAO().delete(button);
            res.status(204);
            return "";
        });

        get("/ignore/buttons", (req, res) -> {
            Collection<Button> buttons = getIgnoreButtonDAO().findAll();
            _prepareResponse(req, res);
            return mapper.writeValueAsString(buttons);
        });

        post("/ignore/buttons", (req, res) -> {
            Collection<Button> buttons = mapper.readValue(req.body(), new TypeReference<Collection<Button>>() {
            });
            getIgnoreButtonDAO().saveAll(buttons);
            getNewButtonDAO().deleteAll(buttons);
            res.status(204);
            return "";
        });
    }

    private DateParams _getDateParams(Request req) throws ParseException {
        String fromStr = req.queryParams("from");
        String tillStr = req.queryParams("till");
        Date from = (fromStr!=null) ? JsonDateDeserializer.stringToDate(fromStr) : null;
        Date till = (tillStr!=null) ? JsonDateDeserializer.stringToDate(tillStr) : null;
        return new DateParams(from,till);
    }

    private void _prepareResponse(Request req, Response res) {
        res.header("Content-Type", "application/json");

    }

    public TaskController getController() {
        return controller;
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

    private class DateParams {
        public final Date FROM;
        public final Date TILL;

        public DateParams(Date from, Date till) {
            FROM=from;
            TILL=till;
        }
    }
}

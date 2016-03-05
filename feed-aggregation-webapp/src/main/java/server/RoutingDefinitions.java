package server;

import controller.AuthenticateController;
import controller.ContentController;
import controller.impl.AuthenticateControllerImpl;
import controller.impl.ContentControllerImpl;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class RoutingDefinitions {
    private Router mainRouter;
    private static Logger logger = LoggerFactory.getLogger(RoutingDefinitions.class);

    public RoutingDefinitions(Vertx vertx){
        this.mainRouter = Router.router(vertx);

        // Set a static server to serve static resources, e.g. the index page
        logger.info("Setting up static handler");
        this.mainRouter.route().handler(
            StaticHandler.create().setWebRoot("app").setIndexPage("index-async.html"));

        ContentController contentController = new ContentControllerImpl(vertx, mainRouter);
        AuthenticateController authenticateController = new AuthenticateControllerImpl();
    }

    public Router getMainRouter() {
        return mainRouter;
    }
}

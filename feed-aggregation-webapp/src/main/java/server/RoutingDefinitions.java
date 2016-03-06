package server;

import controller.FiveHundredPxController;
import controller.ContentController;
import controller.InstagramOauthController;
import controller.impl.FiveHundredPxControllerImpl;
import controller.impl.ContentControllerImpl;
import controller.impl.InstagramOauthControllerImpl;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class RoutingDefinitions {
    private Router mainRouter;
    private static Logger logger = LoggerFactory.getLogger(RoutingDefinitions.class);

    public RoutingDefinitions(Vertx vertx, Router mainRouter){
        this.mainRouter = mainRouter;

        // Set a static server to serve static resources, e.g. the index page
        logger.info("Setting up static handler");

        InstagramOauthController instagramOauthController = new InstagramOauthControllerImpl(vertx, mainRouter);
        FiveHundredPxController fiveHundredPxController = new FiveHundredPxControllerImpl();
        ContentController contentController = new ContentControllerImpl(vertx, mainRouter);

        this.mainRouter.route().handler(
                StaticHandler.create().setWebRoot("app").setIndexPage("index-async.html"));

    }

    public Router getMainRouter() {
        return mainRouter;
    }
}

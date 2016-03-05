package controller.impl;

import common.exception.PreconditionException;
import common.utils.Preconditions;
import controller.ContentController;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class ContentControllerImpl implements ContentController {

    private static Logger logger = LoggerFactory.getLogger(ContentControllerImpl.class);
    private Vertx vertx;
    private Router subRouter;

    public ContentControllerImpl(Vertx vertx, Router mainRouter) {
        this.vertx = vertx;
        init(mainRouter);
    }

    private void init(Router mainRouter) {
        logger.info("Initializing Content controller");
        this.subRouter = Router.router(vertx);

        subRouter.get("/:id").handler(this::handleContentRetrieval);
//        subRouter.get().handler(this::handleContentList);

        mainRouter.mountSubRouter("/content", subRouter);

        logger.info("Initialize Content controller done");
    }

    private void handleContentRetrieval(RoutingContext routingContext) {
        handleContentRetrieval(routingContext.request().getParam("id"));
    }

    @Override
    public void handleContentRetrieval(String id){
        try{
            Preconditions.checkNotNull(id);
        } catch (PreconditionException e){
            logger.debug("Precondition failed, content retrieval id is null.");
        }


    };
}

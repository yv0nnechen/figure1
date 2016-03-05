package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertxHttpServer extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(VertxHttpServer.class);
    //TODO externalize the config
    private static int port = 8080;

    @Override
    public void start(Future<Void> fut) {
        RoutingDefinitions routingDefinitions = new RoutingDefinitions(vertx);

        this.vertx
                .createHttpServer()
                .requestHandler(routingDefinitions.getMainRouter()::accept)
                .listen(port, result -> {
                    if (result.succeeded()) {
                        logger.info("Started http server at port {}", port);
                        fut.complete();
                    } else {
                        logger.error("Start http server error.", result.cause());
                        fut.fail(result.cause());
                    }
                });
    }

    @Override
    public void stop() throws Exception {
        logger.info("Stopping http server at port {}", port);
        super.stop();
    }
}
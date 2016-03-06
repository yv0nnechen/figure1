package server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.ext.web.Router;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.impl.InstagramAuthProvider;

//TODO session handling
public class VertxHttpServer extends AbstractVerticle {
    private static Logger logger = LoggerFactory.getLogger(VertxHttpServer.class);
    //TODO externalize the config
    public static final int port = 8080;
    public static final String host = "http://localhost";
    @Override
    public void start(Future<Void> fut) {
        Router router = Router.router(vertx);
        RoutingDefinitions routingDefinitions = new RoutingDefinitions(vertx, router);

        this.vertx
                .createHttpServer()
                .requestHandler(router::accept)
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
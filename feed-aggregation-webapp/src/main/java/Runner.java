import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.VertxHttpServer;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class Runner {

    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) throws Exception {
        LOGGER.info("Runner started");

        //TODO to add other options like verticle configurations
        DeploymentOptions deploymentOptions =  new DeploymentOptions();

        Vertx vertx = Vertx.vertx();
        CountDownLatch latch = new CountDownLatch(1);
        VertxHttpServer server = new VertxHttpServer();
        vertx.deployVerticle(server, deploymentOptions, result -> {
            if (result.succeeded()) {
                LOGGER.info("Starting Runner with {} configuration", deploymentOptions.toJson().toString());
                latch.countDown();
            }
            else {
                LOGGER.error("Error starting Runner", result.cause());
                throw new RuntimeException(result.cause());
            }
        });

        latch.await();
    }


}

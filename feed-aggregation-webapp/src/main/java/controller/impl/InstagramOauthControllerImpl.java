package controller.impl;

import controller.InstagramOauthController;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.model.Verifier;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.exceptions.InstagramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.impl.InstagramAuthProvider;
import server.VertxHttpServer;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class InstagramOauthControllerImpl implements InstagramOauthController {

    private static Logger logger = LoggerFactory.getLogger(InstagramOauthControllerImpl.class);
    private final String AUTH_ROOT = "/auth/inst";

    private Vertx vertx;
    private InstagramService instagramService;
    private Instagram instagram;
    public InstagramOauthControllerImpl(Vertx vertx, Router mainRouter) {
        this.vertx = vertx;
        init(mainRouter);
    }

    private void init(Router mainRouter) {
        logger.info("Initializing Auth controller");

        Router subRouter = Router.router(this.vertx);
        subRouter.get("/step1").handler(this::stepOne);
        subRouter.get("/step2").handler(this::stepTwo);
        mainRouter.mountSubRouter(AUTH_ROOT, subRouter);

        logger.info("Initialize Auth controller done");
    }


    private void stepOne(RoutingContext routingContext){
        String twoStepCallbackUrl = VertxHttpServer.host+":"+ VertxHttpServer.port+AUTH_ROOT+"/step2";
        instagramService = new InstagramAuthService()
                .apiKey(InstagramAuthProvider.getInstance().getClientId())
                .apiSecret(InstagramAuthProvider.getInstance().getClientSecret())
                .callback(twoStepCallbackUrl)
                .build();
        routingContext.response().setStatusCode(302);
        routingContext.response().headers().add("Location", instagramService.getAuthorizationUrl());
        routingContext.response().end();
    }

    private void stepTwo(RoutingContext routingContext) {
        String code = routingContext.request().getParam("code");
        Verifier verifier = new Verifier(code);
        Token accessToken = instagramService.getAccessToken(verifier);

        service.oauth.InstagramService.service.setInstagram(new Instagram(accessToken));
//        try{
        routingContext.response().setStatusCode(302);
        routingContext.response().headers().add("Location", "http://localhost:8080");
        routingContext.response().end();
//        } catch (InstagramException e){
//            routingContext.response().setStatusCode(500).end();
//        }
    }


}

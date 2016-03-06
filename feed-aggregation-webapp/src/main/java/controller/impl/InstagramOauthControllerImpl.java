package controller.impl;

import controller.InstagramOauthController;
import exception.OAuthException;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import model.OAuthCredentials;
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
import service.oauth.instagram.impl.InstagramClient;
import service.oauth.instagram.impl.InstagramOAuth2Worker;

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
//        instagramService = new InstagramAuthService()
//                .apiKey(InstagramAuthProvider.getInstance().getClientId())
//                .apiSecret(InstagramAuthProvider.getInstance().getClientSecret())
//                .callback(twoStepCallbackUrl)
//                .build();
        InstagramOAuth2Worker worker = new InstagramOAuth2Worker(InstagramAuthProvider.getInstance().getClientId(), InstagramAuthProvider.getInstance().getClientSecret(), twoStepCallbackUrl);
        routingContext.response().setStatusCode(302);
        try {
            routingContext.response().headers().add("Location", worker.buildAuthorizationURL());
        } catch (OAuthException e) {
            e.printStackTrace();
        }
        routingContext.response().end();
    }

    private void stepTwo(RoutingContext routingContext) {
        String twoStepCallbackUrl = VertxHttpServer.host+":"+ VertxHttpServer.port+AUTH_ROOT+"/step2";

        String code = routingContext.request().getParam("code");
//        Verifier verifier = new Verifier(code);
//        Token accessToken = instagramService.getAccessToken(verifier);
        InstagramOAuth2Worker worker = new InstagramOAuth2Worker(InstagramAuthProvider.getInstance().getClientId(), InstagramAuthProvider.getInstance().getClientSecret(), twoStepCallbackUrl);

        try {
            OAuthCredentials oAuthCredentials = worker.generateTokens(code);
//            service.oauth.InstagramService.service.setInstagram(new Instagram(oAuthCredentials.getAccessToken(), ""));
            service.oauth.InstagramService.service.setInstagramClient(new InstagramClient(InstagramAuthProvider.getInstance().getClientId(),oAuthCredentials));

        } catch (OAuthException e) {
            routingContext.response().setStatusCode(500).end();
            return;
        }
//        try{
        routingContext.response().setStatusCode(302);
        routingContext.response().headers().add("Location", "http://localhost:8080/#/gallery");
        routingContext.response().end();
//        } catch (InstagramClientException e){
//            routingContext.response().setStatusCode(500).end();
//        }
    }


}

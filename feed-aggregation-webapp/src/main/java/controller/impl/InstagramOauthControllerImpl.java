package controller.impl;

import controller.InstagramOauthController;
import exception.OAuthException;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import model.OAuthCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.impl.InstagramAuthProvider;
import server.VertxHttpServer;
import service.instagram.impl.InstagramClientImpl;
import service.impl.InstagramFeedService;
import service.oauth.instagram.InstagramOAuth2Worker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class InstagramOauthControllerImpl implements InstagramOauthController {

    private static Logger logger = LoggerFactory.getLogger(InstagramOauthControllerImpl.class);
    private final String AUTH_ROOT = "/auth/inst";

    private Vertx vertx;
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
        InstagramOAuth2Worker worker = new InstagramOAuth2Worker(InstagramAuthProvider.getInstance().getClientId(), InstagramAuthProvider.getInstance().getClientSecret(), twoStepCallbackUrl);
        routingContext.response().setStatusCode(302);
        try {
            routingContext.response().headers().add("Location", worker.buildAuthorizationURL());
        } catch (OAuthException e) {
            logger.error("Instagram Oauth failed get access failed", e);
        }
        routingContext.response().end();
    }

    private void stepTwo(RoutingContext routingContext) {
        String twoStepCallbackUrl = VertxHttpServer.host+":"+ VertxHttpServer.port+AUTH_ROOT+"/step2";

        String code = routingContext.request().getParam("code");
        InstagramOAuth2Worker worker = new InstagramOAuth2Worker(InstagramAuthProvider.getInstance().getClientId(), InstagramAuthProvider.getInstance().getClientSecret(), twoStepCallbackUrl);

        try {
            OAuthCredentials oAuthCredentials = worker.generateTokens(code);
            routingContext.response().setStatusCode(302);
            Cookie cookie = new DefaultCookie("inst-token", Json.encode(oAuthCredentials));
            cookie.setMaxAge(60*60); //1 hour in seconds
            cookie.setPath("/");
            routingContext.response().putHeader("Set-Cookie", ServerCookieEncoder.LAX.encode(cookie));
            routingContext.response().headers().add("Location", "/#/gallery");
            routingContext.response().end();

        } catch (OAuthException e) {
            logger.error("Instagram Oauth failed get access failed", e);
            routingContext.response().setStatusCode(500).end();
            return;
        }
    }



}

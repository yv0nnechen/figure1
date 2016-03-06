package controller.impl;

import common.model.content.Feed;
import controller.ContentController;
import controller.InstagramOauthController;
import exception.FeedServiceException;
import exception.InstagramClientException;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Cookie;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.impl.CookieImpl;
import model.InstagramCredentials;
import model.OAuthCredentials;
import model.OAuthVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.impl.InstagramFeedService;
import service.impl.PixelFeedService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static io.vertx.core.http.HttpHeaders.COOKIE;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class ContentControllerImpl implements ContentController {

    private static Logger logger = LoggerFactory.getLogger(ContentControllerImpl.class);

    private Vertx vertx;
    private InstagramFeedService instagramFeedService;
    private PixelFeedService pixelFeedService;

    public ContentControllerImpl(Vertx vertx, Router mainRouter) {
        this.vertx = vertx;
        this.instagramFeedService = InstagramFeedService.service;
        this.pixelFeedService = PixelFeedService.service;
        init(mainRouter);
    }

    private void init(Router mainRouter) {
        logger.info("Initializing Content controller");

        Router subRouter = Router.router(this.vertx);
        subRouter.get().handler(this::listContent);
        mainRouter.mountSubRouter("/content", subRouter);

        logger.info("Initialize Content controller done");
    }

    private void listContent(RoutingContext routingContext) {
        OAuthCredentials oAuthCredentials = getCrdentialFromCookie(routingContext);
        try {
//            List<Feed> feeds = instagramFeedService.getFeeds(oAuthCredentials);
            List<Feed> feeds = pixelFeedService.getFeeds(oAuthCredentials);
            routingContext.response().end(Json.encode(feeds));
        } catch (FeedServiceException e) {
            logger.error("Get feeds failed", e);
            routingContext.response().setStatusCode(500).end();
        }
    }


    private OAuthCredentials getCrdentialFromCookie(RoutingContext routingContext){
        String cookieHeader = routingContext.request().headers().get(COOKIE);
        if (cookieHeader != null) {
            Set<io.netty.handler.codec.http.cookie.Cookie> nettyCookies = ServerCookieDecoder.LAX.decode(cookieHeader);
            for (io.netty.handler.codec.http.cookie.Cookie cookie : nettyCookies) {
                Cookie ourCookie = new CookieImpl(cookie);
                routingContext.addCookie(ourCookie);
            }
        }

        Cookie cookie = routingContext.getCookie(InstagramOauthController.INST_TOKEN_COOKIE);
        return Json.decodeValue(cookie.getValue(), InstagramCredentials.class);
    }

}

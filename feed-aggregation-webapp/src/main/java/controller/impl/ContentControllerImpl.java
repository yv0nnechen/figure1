package controller.impl;

import common.model.content.Feed;
import common.model.content.PaginatedFeeds;
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
import model.PixelCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.impl.InstagramFeedService;
import service.impl.PixelFeedService;
import service.instagram.InstagramClient;
import service.pixel.PixelClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        subRouter.get("/pixel").handler(this::listPixelContent);
        subRouter.get("/instagram").handler(this::listInstagramContent);
        subRouter.get("/pixel/:id").handler(this::getPixelContent);
        subRouter.get("/instagram/:id").handler(this::getInstagramContent);
        mainRouter.mountSubRouter("/content", subRouter);

        logger.info("Initialize Content controller done");
    }

    private void getInstagramContent(RoutingContext routingContext) {
        OAuthCredentials oAuthCredentials = getCrdentialFromCookie(routingContext);
        if(oAuthCredentials.getAccessToken()!=null){
            try {
                String id = routingContext.request().getParam("id");
                Feed feed = instagramFeedService.getFeed(oAuthCredentials, id);
                routingContext.response().end(Json.encode(feed));
            } catch (FeedServiceException e) {
                logger.error("Get feeds failed", e);
                routingContext.response().setStatusCode(500).end();
            }
        } else {
            routingContext.response().setStatusCode(401).end();
        }
    }

    private void getPixelContent(RoutingContext routingContext) {
        try {
            String id = routingContext.request().getParam("id");
            Feed feeds = pixelFeedService.getFeed(new PixelCredentials(), id);
            routingContext.response().end(Json.encode(feeds));
        } catch (FeedServiceException e) {
            logger.error("Get feeds failed", e);
            routingContext.response().setStatusCode(500).end();
        }
    }

    private void listInstagramContent(RoutingContext routingContext) {
        OAuthCredentials oAuthCredentials = getCrdentialFromCookie(routingContext);
        if(oAuthCredentials.getAccessToken()!=null){
            try {
                Map<String, Object> query = new HashMap<>();
                query.put(InstagramClient.QueryParam.COUNT, routingContext.request().getParam("count"));
                query.put(InstagramClient.QueryParam.MAX_ID, routingContext.request().getParam("max_id"));
                query.put(InstagramClient.QueryParam.MIN_ID, routingContext.request().getParam("min_id"));
                PaginatedFeeds feeds = instagramFeedService.getFeeds(oAuthCredentials, query);
                routingContext.response().end(Json.encode(feeds));
            } catch (FeedServiceException e) {
                logger.error("Get feeds failed", e);
                routingContext.response().setStatusCode(500).end();
            }
        } else {
            routingContext.response().setStatusCode(401).end();
        }
    }

    private void listPixelContent(RoutingContext routingContext) {
        try {
            Map<String, Object> query = new HashMap<>();
            query.put(PixelClient.QueryParam.PAGE, routingContext.request().getParam("page"));
            query.put(PixelClient.QueryParam.FEATURE, routingContext.request().getParam("feature"));
            PaginatedFeeds feeds = pixelFeedService.getFeeds(new PixelCredentials(), query);
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
        if(cookie!=null){
            return Json.decodeValue(cookie.getValue(), InstagramCredentials.class);
        } else {
            return new InstagramCredentials();
        }
    }

}

package controller.impl;

import common.model.content.Feed;
import controller.ContentController;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.codec.binary.StringUtils;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.oauth.InstagramService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class ContentControllerImpl implements ContentController {

    private static Logger logger = LoggerFactory.getLogger(ContentControllerImpl.class);

    private Vertx vertx;
    public ContentControllerImpl(Vertx vertx, Router mainRouter) {
        this.vertx = vertx;
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
        try{
            List<MediaFeedData> instFeeds = InstagramService.service.getInstagram().getUserRecentMedia().getData();
            List<Feed> feedList = instFeeds.stream().map(mediaFeedData -> {
                return new Feed.FeedBuilder()
                        .setLocation(mediaFeedData.getImages().getLowResolution().getImageUrl())
                        .setLikeCount(mediaFeedData.getLikes()!=null?mediaFeedData.getLikes().getCount():null)
                        .setCommentCount(mediaFeedData.getComments()!=null?mediaFeedData.getComments().getCount():null)
                        .setUserName(mediaFeedData.getCaption()!=null? (mediaFeedData.getCaption().getFrom()!=null?mediaFeedData.getCaption().getFrom().getUsername():null):null)
                        .setCaption(mediaFeedData.getCaption()!=null?mediaFeedData.getCaption().getText():null)
                        .setCreatedTime(mediaFeedData.getCreatedTime()!=null?Long.parseLong(mediaFeedData.getCreatedTime()):null)
                        .createFeed();
            }).collect(Collectors.toList());
            routingContext.response().end(Json.encode(feedList));
        } catch (InstagramException e){
            routingContext.response().setStatusCode(500).end();
        }



    }

}

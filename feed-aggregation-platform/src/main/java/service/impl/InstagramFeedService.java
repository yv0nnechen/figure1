package service.impl;

import common.model.content.ContentType;
import common.model.content.Feed;
import common.model.content.PaginatedFeeds;
import common.model.content.Pagination;
import common.utils.JsonUtils;
import exception.FeedServiceException;
import exception.InstagramClientException;
import io.vertx.core.json.JsonObject;
import model.OAuthCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.impl.InstagramAuthProvider;
import service.FeedService;
import service.instagram.InstagramClient;
import service.instagram.impl.InstagramClientImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class InstagramFeedService implements FeedService{
    private static final Logger logger = LoggerFactory.getLogger(InstagramFeedService.class);

    public static final InstagramFeedService service = new InstagramFeedService();

    private InstagramFeedService() {}

    @Override
    public PaginatedFeeds getFeeds(OAuthCredentials oAuthCredentials) throws FeedServiceException {
        logger.debug("going to load feeds with default params");
        InstagramClient instagramClient = new InstagramClientImpl(InstagramAuthProvider.getInstance().getClientId(), oAuthCredentials);
        try {
            return parseToFeeds(instagramClient.getUserRecentMedia().toString());
        } catch (InstagramClientException | IOException | URISyntaxException e) {
            logger.error("error getting feeds", e);
            throw new FeedServiceException(e);
        }
    }

    @Override
    public PaginatedFeeds getFeeds(OAuthCredentials oAuthCredentials, Map<String, Object> params) throws FeedServiceException {
        if(params == null){
            return getFeeds(oAuthCredentials);
        }
        logger.debug("going to load feeds with params count {}, min_id {}, max_id {}", params.get(InstagramClient.QueryParam.COUNT), params.get(InstagramClient.QueryParam.MAX_ID), params.get(InstagramClient.QueryParam.MAX_ID));
        InstagramClient instagramClient = new InstagramClientImpl(InstagramAuthProvider.getInstance().getClientId(), oAuthCredentials);
        try {
            return parseToFeeds(instagramClient
                    .getUserRecentMedia(params.get(InstagramClient.QueryParam.COUNT)!=null? (Integer) params.get(InstagramClient.QueryParam.COUNT) :0,
                            (String) params.get(InstagramClient.QueryParam.MIN_ID),
                            (String) params.get(InstagramClient.QueryParam.MAX_ID)
            ).toString());
        } catch (InstagramClientException | IOException | URISyntaxException e) {
            logger.error("error getting feeds", e);
            throw new FeedServiceException(e);
        }
    }

    @Override
    public Feed getFeed(OAuthCredentials oAuthCredentials, String id) throws FeedServiceException {
        logger.debug("Going to load one instagram feed [{}]", id);
        InstagramClient instagramClient = new InstagramClientImpl(InstagramAuthProvider.getInstance().getClientId(), oAuthCredentials);
        try {
            return parseToFeed(instagramClient.getMediaInfo(id).toString());
        } catch (InstagramClientException | IOException | URISyntaxException e) {
            logger.error("error getting feed {}", id, e);
            throw new FeedServiceException(e);
        }
    }

    private PaginatedFeeds parseToFeeds(String json){
        JsonObject jsonObject = new JsonObject(json);
        Pagination pagination = new Pagination();

        List<Feed> feeds = jsonObject.getJsonArray("data")
                .stream()
                .map(jsonObj -> parseToFeed(jsonObj.toString()))
                .collect(Collectors.toList());
        if(feeds.size()>0){
            pagination.setMinId(feeds.get(0).getId());
            pagination.setMaxId(feeds.get(feeds.size()-1).getId());
        }
        PaginatedFeeds paginatedFeeds = new PaginatedFeeds();
        paginatedFeeds.setFeeds(feeds);
        paginatedFeeds.setPagination(pagination);
        return paginatedFeeds;
    }

    //TODO should be able to move to abstract classes
    private Feed parseToFeed(String jsonObj){
        Object parsedJson = JsonUtils.getParsedJson(jsonObj);
        return new Feed.FeedBuilder()
                .setContentType(ContentType.INSTAGRAM)
                .setId((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.id", null))
                .setLocation((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.images.low_resolution.url", null))
                .setLikeCount((Integer) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.likes.count", null))
                .setCommentCount((Integer) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.comments.count", null))
                .setUserName((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.caption.from.username", null))
                .setCaption((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.caption.text", null))
                .setCreatedTime(Long.parseLong((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.created_time", null)))
                .createFeed();
    }

}

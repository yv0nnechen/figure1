package service.impl;

import common.model.content.ContentType;
import common.model.content.Feed;
import common.model.content.PaginatedFeeds;
import common.model.content.Pagination;
import common.utils.JsonUtils;
import exception.FeedServiceException;
import exception.PixelClientException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import model.OAuthCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import provider.impl.PixelAuthProvider;
import service.FeedService;
import service.pixel.PixelClient;
import service.pixel.impl.PixelClientImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class PixelFeedService implements FeedService{
    private static final Logger logger = LoggerFactory.getLogger(PixelFeedService.class);

    public static final PixelFeedService service = new PixelFeedService();

    private PixelFeedService() {}

    //TODO need auth credentials later

    @Override
    public PaginatedFeeds getFeeds(OAuthCredentials oAuthCredentials) throws FeedServiceException {
        logger.debug("going to load feeds with default params");
        PixelClient pixelClient = new PixelClientImpl(PixelAuthProvider.getInstance().getClientId());
        try {
            return parseToFeeds(pixelClient.getPhotos().toString());
        } catch (PixelClientException | IOException | URISyntaxException e) {
            logger.error("error getting feeds", e);
            throw new FeedServiceException(e);
        }
    }

    @Override
    public PaginatedFeeds getFeeds(OAuthCredentials oAuthCredentials, Map<String, Object> params) throws FeedServiceException {
        if(params == null){
            return getFeeds(oAuthCredentials);
        }
        logger.debug("going to load feeds with params page count {}", params.get(PixelClient.QueryParam.COUNT));
        PixelClient pixelClient = new PixelClientImpl(PixelAuthProvider.getInstance().getClientId());
        try {
            if(params.get(PixelClient.QueryParam.PAGE) == null){
                return parseToFeeds(pixelClient.getPhotos().toString());
            }
            String s = pixelClient.getPhotos(Integer.parseInt((String) params.get(PixelClient.QueryParam.PAGE))).toString();
            return parseToFeeds(s);
        } catch (PixelClientException | IOException | URISyntaxException e) {
            logger.error("error getting feeds", e);
            throw new FeedServiceException(e);
        }
    }

    @Override
    public Feed getFeed(OAuthCredentials oAuthCredentials, String id) throws FeedServiceException {
        logger.debug("Going to load one instagram feed [{}]", id);
        PixelClient pixelClient = new PixelClientImpl(PixelAuthProvider.getInstance().getClientId());
        try {
            return parseToFeed(pixelClient.getPhoto(id).toString());
        } catch (PixelClientException | IOException | URISyntaxException e) {
            logger.error("error getting feed {}", id, e);
            throw new FeedServiceException(e);
        }
    }

    private PaginatedFeeds parseToFeeds(String json){
        JsonObject jsonObject = new JsonObject(json);
        Pagination pagination = new Pagination();
        pagination.setPage(jsonObject.getInteger("current_page"));

        List<Feed> feeds = jsonObject.getJsonArray("photos")
                .stream()
                .map(jsonObj -> parseToFeed(jsonObj.toString()))
                .collect(Collectors.toList());
        PaginatedFeeds paginatedFeeds = new PaginatedFeeds();
        paginatedFeeds.setFeeds(feeds);
        paginatedFeeds.setPagination(pagination);
        return paginatedFeeds;
    }

    private Feed parseToFeed(String jsonObj){
        Object parsedJson = JsonUtils.getParsedJson(jsonObj);
        String date = (String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.created_at", null);
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        long createdTime = dateTime.toEpochSecond(ZoneOffset.UTC);
        return new Feed.FeedBuilder()
                .setContentType(ContentType.PIXEL)
                .setId(String.valueOf((Integer) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.id", null)))
                .setLocation((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.image_url", null))
                .setLikeCount((Integer) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.votes_count", null))
                .setCommentCount((Integer) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.comments_count", null))
                .setUserName((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.user.username", null))
                .setCaption((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.name", null))
                .setCreatedTime(createdTime)
                .createFeed();
    }
}

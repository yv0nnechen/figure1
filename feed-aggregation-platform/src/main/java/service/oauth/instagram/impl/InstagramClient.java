package service.oauth.instagram.impl;

import common.model.content.Feed;
import common.utils.JsonUtils;
import common.utils.Preconditions;
import exception.InstagramClientException;
import http.HttpClientExecutor;
import http.HttpMethods;
import http.SimpleHttpResponse;
import io.vertx.core.json.JsonObject;
import model.OAuthCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Yvonne on 2016-03-06.
 * TODO should really create models like metadata, pagination and and data,
 * in each data blob, we need POJO for each instances like MediaFeed, Likes, Comments.. but I got no time for this!!!!
 */
public class InstagramClient {
    private static final String API_ENDPOINT = "https://api.instagram.com/v1";
    private String clientId;

    private String accessToken;

    private static final Logger logger = LoggerFactory.getLogger(InstagramClient.class);


    public InstagramClient(String clientId, OAuthCredentials oAuthCredentials) {
        Preconditions.checkBothNotNull(oAuthCredentials, clientId, "accessToken and clientId must not be null");
        this.clientId = clientId;
        this.accessToken = oAuthCredentials.getAccessToken();
    }

    /**
     * Get current user's recent media
     *
     * @return a MediaFeedObject
     * @throws InstagramClientException
     * @author tolstovdmit
     */
    public List<Feed> getUserRecentMedia() throws InstagramClientException, IOException, URISyntaxException {
        logger.info("Getting current user recent media...");

        return performRequestAndGetFeeds(HttpMethods.GET, InstagramAPIConstants.USERS_SELF_RECENT_MEDIA, null);
    }

    /**
     * Get current user's recent media with parameters.
     *
     *
     * @param count
     *            Count of media to return.
     * @param minId
     * @param maxId
     * @return a MediaFeedObject
     * @throws InstagramClientException
     * @author tolstovdmit
     */
    public List<Feed> getUserRecentMedia(int count, String minId, String maxId) throws InstagramClientException, IOException, URISyntaxException {
        logger.info("Getting current user recent media...");

        Map<String, String> params = new HashMap<String, String>();

        if (maxId != null) {
            params.put(QueryParam.MAX_ID, String.valueOf(maxId));
        }

        if (minId != null) {
            params.put(QueryParam.MIN_ID, String.valueOf(minId));
        }

        if (count != 0) {
            params.put(QueryParam.COUNT, String.valueOf(count));
        }

        return performRequestAndGetFeeds(HttpMethods.GET, InstagramAPIConstants.USERS_SELF_RECENT_MEDIA, params);
    }

    /**
     * Get information about a media object.
     *
     * @param mediaId
     *            mediaId of the Media object.
     * @return a mediaFeed object.
     * @throws InstagramClientException
     *             if any error occurs.
     */
    public Feed getMediaInfo(String mediaId) throws InstagramClientException, IOException, URISyntaxException {
        Preconditions.checkNotNull(mediaId, "mediaId cannot be null.");

        String apiMethod = String.format(InstagramAPIConstants.MEDIA_BY_ID, mediaId);

        return performRequestAndGetFeed(HttpMethods.GET, apiMethod, null);
    }

    protected List<Feed> performRequestAndGetFeeds(HttpMethods verbs, String url,
                                                                     Map<String, String> params) throws IOException, URISyntaxException, InstagramClientException {
        switch (verbs){
            case GET:
                SimpleHttpResponse simpleHttpResponse = doGET(url, params);
                if(simpleHttpResponse.getStatusCode()>=200&& simpleHttpResponse.getStatusCode()<300) {
                    return parseToFeeds(simpleHttpResponse.getRawResponse());
                } else {
                    throw handleError(simpleHttpResponse);
                }
            default:
                throw new UnsupportedOperationException("No such http method supported yet: "+verbs.name());
        }
    }

    protected Feed performRequestAndGetFeed(HttpMethods verbs, String url,
                                                   Map<String, String> params) throws IOException, URISyntaxException, InstagramClientException {
        switch (verbs){
            case GET:
                SimpleHttpResponse simpleHttpResponse = doGET(url, params);
                if(simpleHttpResponse.getStatusCode()>=200&& simpleHttpResponse.getStatusCode()<300) {
                    return parseToFeed(simpleHttpResponse.getRawResponse());
                } else {
                    throw handleError(simpleHttpResponse);
                }
            default:
                throw new UnsupportedOperationException("No such http method supported yet: "+verbs.name());
        }
    }

    private List<Feed> parseToFeeds(String json){
        JsonObject jsonObject = new JsonObject(json);
        List<Feed> feeds = jsonObject.getJsonArray("data")
                .stream()
                .map(jsonObj -> parseToFeed(jsonObj.toString()))
                .collect(Collectors.toList());
        return feeds;
    }

    private Feed parseToFeed(String jsonObj){
        Object parsedJson = JsonUtils.getParsedJson(jsonObj);
        return new Feed.FeedBuilder()
                .setId((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.id", null))
                .setLocation((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.images.low_resolution.url", null))
                .setLikeCount((Integer) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.likes.count", null))
                .setCommentCount((Integer) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.comments.count", null))
                .setUserName((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.caption.from.username", null))
                .setCaption((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.caption.text", null))
                .setCreatedTime(Long.parseLong((String) JsonUtils.readPathFromParsedJsonWithDefault(parsedJson, "$.created_time", null)))
                .createFeed();
    }

    private InstagramClientException handleError(SimpleHttpResponse simpleHttpResponse) {
        JsonObject errorObject = new JsonObject(simpleHttpResponse.getRawResponse());
        logger.warn("Instagram client request failed. Error Type: {}, Description: {}", errorObject.getString("error_type"), errorObject.getString("error_message"));
        return new InstagramClientException(errorObject.getString("error_type")+": "+errorObject.getString("error_message"));
    }

    private SimpleHttpResponse doGET(String url, Map<String, String> params) throws IOException, URISyntaxException {
        URIBuilder builder = new URIBuilder(API_ENDPOINT+url);

        if(params!=null){
            params.keySet().stream().forEach(paramKey-> {
                builder.setParameter(paramKey, params.get(paramKey));
            });
        }
        builder.setParameter(OAuthConstants.ACCESS_TOKEN, accessToken);

        URI uri = builder.build();
        HttpGet httpget = new HttpGet(uri);
        logger.info("About to perform GET {}", uri);
        return HttpClientExecutor.getInstance().perform(httpget);
    }

    public class QueryParam {
        /**
         * MAX_ID	Return media earlier than this max_id.
         */
        final static String MAX_ID = "max_id";

        /**
         * MAX_ID	Return media earlier than this max_id.
         */
        final static String MIN_ID = "min_id";

        /**
         * COUNT	Count of media to return.
         */
        final static String COUNT = "count";
    }
}

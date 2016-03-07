package service.instagram.impl;

import common.model.content.Feed;
import common.utils.JsonUtils;
import common.utils.Preconditions;
import exception.InstagramClientException;
import http.HttpClientExecutor;
import http.HttpMethods;
import http.SimpleHttpResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import model.OAuthCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.instagram.InstagramClient;
import service.oauth.instagram.OAuthConstants;

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
public class InstagramClientImpl implements InstagramClient {
    private String clientId;

    private String accessToken;

    private static final Logger logger = LoggerFactory.getLogger(InstagramClientImpl.class);


    public InstagramClientImpl(String clientId, OAuthCredentials oAuthCredentials) {
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
    public JsonObject getUserRecentMedia() throws InstagramClientException, IOException, URISyntaxException {
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
    public JsonObject getUserRecentMedia(int count, String minId, String maxId) throws InstagramClientException, IOException, URISyntaxException {
        logger.info("Getting current user recent media... Count = {}, minId = {}, maxId = {}", count, minId, maxId);

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
    public JsonObject getMediaInfo(String mediaId) throws InstagramClientException, IOException, URISyntaxException {
        Preconditions.checkNotNull(mediaId, "mediaId cannot be null.");

        String apiMethod = String.format(InstagramAPIConstants.MEDIA_BY_ID, mediaId);

        return performRequestAndGetFeed(HttpMethods.GET, apiMethod, null);
    }

    protected JsonObject performRequestAndGetFeeds(HttpMethods verbs, String url,
                                                                     Map<String, String> params) throws IOException, URISyntaxException, InstagramClientException {
        switch (verbs){
            case GET:
                SimpleHttpResponse simpleHttpResponse = doGET(url, params);
                if(simpleHttpResponse.getStatusCode()>=200&& simpleHttpResponse.getStatusCode()<300) {
                    return new JsonObject(simpleHttpResponse.getRawResponse());
                } else {
                    throw handleError(simpleHttpResponse);
                }
            default:
                throw new UnsupportedOperationException("No such http method supported yet: "+verbs.name());
        }
    }

    protected JsonObject performRequestAndGetFeed(HttpMethods verbs, String url,
                                                   Map<String, String> params) throws IOException, URISyntaxException, InstagramClientException {
        switch (verbs){
            case GET:
                SimpleHttpResponse simpleHttpResponse = doGET(url, params);
                if(simpleHttpResponse.getStatusCode()>=200&& simpleHttpResponse.getStatusCode()<300) {
                    return new JsonObject(simpleHttpResponse.getRawResponse()).getJsonObject("data");
                } else {
                    throw handleError(simpleHttpResponse);
                }
            default:
                throw new UnsupportedOperationException("No such http method supported yet: "+verbs.name());
        }
    }

    private InstagramClientException handleError(SimpleHttpResponse simpleHttpResponse) {
        JsonObject errorObject = new JsonObject(simpleHttpResponse.getRawResponse());
        logger.warn("Instagram client request failed. Error Type: {}, Description: {}", errorObject.getString("error_type"), errorObject.getString("error_message"));
        return new InstagramClientException(errorObject.getString("error_type")+": "+errorObject.getString("error_message"));
    }

    private SimpleHttpResponse doGET(String url, Map<String, String> params) throws IOException, URISyntaxException {
        if(params==null){
            params = new HashMap<>();
        }
        params.put(OAuthConstants.ACCESS_TOKEN, accessToken);
        logger.info("About to perform GET {}", API_ENDPOINT+url);
        return HttpClientExecutor.getInstance().doGET(API_ENDPOINT+url, params);
    }
}

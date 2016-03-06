package service.pixel.impl;

import common.utils.Preconditions;
import exception.PixelClientException;
import http.HttpClientExecutor;
import http.HttpMethods;
import http.SimpleHttpResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.pixel.PixelClient;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class PixelClientImpl implements PixelClient{
    private String consumerKey;

    private static final Logger logger = LoggerFactory.getLogger(PixelClientImpl.class);

    //TODO may need some auth credentials later
    public PixelClientImpl(String consumerKey) {
        Preconditions.checkNotNull(consumerKey, "consumer key must not be null");
        this.consumerKey = consumerKey;
    }

    @Override
    public JsonArray getPhotos() throws PixelClientException, IOException, URISyntaxException {
        logger.info("Getting current user photos...");

        return performRequestAndGetFeeds(HttpMethods.GET, PixelAPIConstants.PHOTOS, null);
    }

    @Override
    public JsonArray getPhotos(int pageNumber) throws PixelClientException, IOException, URISyntaxException {
        logger.info("Getting current user photos...");

        Map<String, String> params = new HashMap<String, String>();

        if (pageNumber != 0) {
            params.put(QueryParam.PAGE, String.valueOf(pageNumber));
        }

        return performRequestAndGetFeeds(HttpMethods.GET, PixelAPIConstants.PHOTOS, params);
    }

    @Override
    public JsonObject getPhoto(String id) throws IOException, URISyntaxException, PixelClientException {
        Preconditions.checkNotNull(id, "photo id cannot be null.");

        String apiMethod = String.format(PixelAPIConstants.PHOTO_BY_ID, id);

        return performRequestAndGetFeed(HttpMethods.GET, apiMethod, null);
    }

    protected JsonArray performRequestAndGetFeeds(HttpMethods verbs, String url,
                                                  Map<String, String> params) throws IOException, URISyntaxException, PixelClientException {
        switch (verbs){
            case GET:
                SimpleHttpResponse simpleHttpResponse = doGET(url, params);
                if(simpleHttpResponse.getStatusCode()>=200 && simpleHttpResponse.getStatusCode()<300) {
                    return new JsonObject(simpleHttpResponse.getRawResponse()).getJsonArray("photos");
                } else {
                    throw handleError(simpleHttpResponse);
                }
            default:
                throw new UnsupportedOperationException("No such http method supported yet: "+verbs.name());
        }
    }

    protected JsonObject performRequestAndGetFeed(HttpMethods verbs, String url,
                                                  Map<String, String> params) throws IOException, URISyntaxException, PixelClientException {
        switch (verbs){
            case GET:
                SimpleHttpResponse simpleHttpResponse = doGET(url, params);
                if(simpleHttpResponse.getStatusCode()>=200&& simpleHttpResponse.getStatusCode()<300) {
                    return new JsonObject(simpleHttpResponse.getRawResponse()).getJsonObject("photo");
                } else {
                    throw handleError(simpleHttpResponse);
                }
            default:
                throw new UnsupportedOperationException("No such http method supported yet: "+verbs.name());
        }
    }

    private PixelClientException handleError(SimpleHttpResponse simpleHttpResponse) {
        return new PixelClientException(simpleHttpResponse.getRawResponse());
    }

    private SimpleHttpResponse doGET(String url, Map<String, String> params) throws IOException, URISyntaxException {
        if(params==null){
            params = new HashMap<>();
        }
        params.put(PixelAuthConstants.CONSUMER_KEY, consumerKey);
        params.put(QueryParam.FEATURE, Feature.FRESH_TODAY);
        logger.info("About to perform GET {}", API_ENDPOINT+url);
        return HttpClientExecutor.getInstance().doGET(API_ENDPOINT+url, params);
    }


}

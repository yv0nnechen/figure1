package service.oauth.instagram.impl;

import common.utils.Preconditions;
import exception.OAuthException;
import http.HttpClientExecutor;
import http.SimpleHttpResponse;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import model.InstagramCredentials;
import model.OAuthCredentials;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.oauth.OAuthWorker;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class InstagramOAuth2Worker implements OAuthWorker {
    private String apiKey;

    private String apiSecret;

    private String callback;

    private static CloseableHttpClient httpClient;
    private static final Logger logger = LoggerFactory.getLogger(InstagramOAuth2Worker.class);
    static {
        //init here so that it's started on system startup
        //TODO close the client
        httpClient = HttpClients.createDefault();
    }

    public InstagramOAuth2Worker(String apiKey, String apiSecret, String callback) {
        Preconditions.checkEmptyString(apiKey, "You must provide an api key");
        Preconditions.checkEmptyString(apiSecret, "You must provide an api secret");
        Preconditions.checkEmptyString(callback, "You must provide a callback url");
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.callback = callback;
    }

    @Override
    public String buildAuthorizationURL() throws OAuthException {
        try {
            return String.format(InstagramEndpointConstants.AUTHORIZE_URL, apiKey, URLEncoder.encode(callback, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new OAuthException(e);
        }
    }

    @Override
    public OAuthCredentials generateTokens(String code) throws OAuthException {
        JsonObject bodyJson = new JsonObject();
        bodyJson.put(OAuthConstants.CLIENT_ID, apiKey)
                .put(OAuthConstants.CLIENT_SECRET, apiSecret)
                .put(OAuthConstants.CODE, code)
                .put(OAuthConstants.GRANT_TYPE, "authorization_code")
                //the redirect_uri you used in the authorization request. Note: this has to be the same value as in the authorization request.
                .put(OAuthConstants.REDIRECT_URI, callback);
        HttpPost uploadFile = new HttpPost(InstagramEndpointConstants.ACCESS_TOKEN_ENDPOINT);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody(OAuthConstants.CLIENT_ID, apiKey);
        builder.addTextBody(OAuthConstants.CLIENT_SECRET, apiSecret);
        builder.addTextBody(OAuthConstants.CODE, code);
        builder.addTextBody(OAuthConstants.GRANT_TYPE, "authorization_code");
        builder.addTextBody(OAuthConstants.REDIRECT_URI, callback);
        HttpEntity multipart = builder.build();

        uploadFile.setEntity(multipart);
        try {
            SimpleHttpResponse response = HttpClientExecutor.getInstance().perform(uploadFile);
            JsonObject responseJson = new JsonObject(response.getRawResponse());
            if(response.getStatusCode()==200){
                logger.info("Access Token retrieved successful.");
                InstagramCredentials credential = new InstagramCredentials();
                credential.setAccessToken(responseJson.getString(OAuthConstants.ACCESS_TOKEN));
                return credential;
            } else {
                logger.error("Access Token retrieval failed. raw response {}", response.getRawResponse());
                throw new OAuthException(responseJson.getString("error_description"));
            }
        } catch (IOException e) {
            logger.error("Error in http execution.", e);
            throw new OAuthException(e);
        }
    }


}

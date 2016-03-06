package service.oauth.instagram.impl;

import exception.OAuthException;
import service.oauth.OAuthService;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class InstagramAuthServiceImpl implements OAuthService{

    private final String API_ENDPOINT= "https://api.instagram.com/oauth/authorize";


    @Override
    public String buildRedirectURL(String clientId, String callbackUrl) throws OAuthException {
        return String.format("https://api.instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=code", clientId, callbackUrl);
    }

    @Override
    public void postCallbackAuthorize(String referrer, String requestToken, String oauthVerifier, String name) throws OAuthException {

    }
}

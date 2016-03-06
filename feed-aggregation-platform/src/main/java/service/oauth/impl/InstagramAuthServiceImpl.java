package service.oauth.impl;

import com.sun.org.apache.regexp.internal.RE;
import exception.OAuthServiceException;
import provider.impl.InstagramAuthProvider;
import service.oauth.OAuthService;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class InstagramAuthServiceImpl implements OAuthService{

    private final String API_ENDPOINT= "https://api.instagram.com/oauth/authorize";
    private final String REDIRECT_URL;

    public InstagramAuthServiceImpl(String appUrl) {
        REDIRECT_URL = String.format("https://api.instagram.com/oauth/authorize/?client_id=%s&redirect_uri=%s&response_type=code", InstagramAuthProvider.getInstance().getClientId(), appUrl);
    }

    @Override
    public String buildRedirectURL(String referrer) throws OAuthServiceException {
        return REDIRECT_URL;
    }

    @Override
    public void postCallbackAuthorize(String referrer, String requestToken, String oauthVerifier, String name) throws OAuthServiceException {

    }
}

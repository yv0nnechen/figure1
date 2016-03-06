package service.oauth;

import exception.OAuthException;

/**
 * Created by Yvonne on 2016-03-05.
 */
public interface OAuthService {
    public String buildRedirectURL(String referrer, String callback) throws OAuthException;

    public void postCallbackAuthorize(String referrer, String requestToken, String oauthVerifier, String name) throws OAuthException;

//    public void attemptTokenRefresh() throws OAuthException;
//
//    public boolean isRefreshable();
}

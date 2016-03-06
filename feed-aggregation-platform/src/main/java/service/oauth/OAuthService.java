package service.oauth;

import exception.OAuthServiceException;

/**
 * Created by Yvonne on 2016-03-05.
 */
public interface OAuthService {
    public String buildRedirectURL(String referrer) throws OAuthServiceException;

    public void postCallbackAuthorize(String referrer, String requestToken, String oauthVerifier, String name) throws OAuthServiceException;

//    public void attemptTokenRefresh() throws OAuthServiceException;
//
//    public boolean isRefreshable();
}

package model;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class InstagramCredentials extends OAuthCredentials {
    @Override
    public OAuthVersion getOAuthVersion() {
        return OAuthVersion.OAUTH_2;
    }
}

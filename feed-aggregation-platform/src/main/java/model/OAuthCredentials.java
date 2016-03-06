package model;

import java.io.Serializable;

/**
 * Created by Yvonne on 2016-03-06.
 */
public abstract class OAuthCredentials implements Serializable {

    private String accessToken;

    public abstract OAuthVersion getOAuthVersion();

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}

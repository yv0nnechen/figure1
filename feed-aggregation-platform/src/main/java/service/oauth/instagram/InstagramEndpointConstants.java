package service.oauth.instagram;

/**
 * Created by Yvonne on 2016-03-06.
 */
public final class InstagramEndpointConstants {

    public static final String INSTAGRAM_OAUTH_URL_BASE = "https://api.instagram.com/oauth";

    public static final String ACCESS_TOKEN_ENDPOINT = INSTAGRAM_OAUTH_URL_BASE + "/access_token";

    public static final String AUTHORIZE_URL = INSTAGRAM_OAUTH_URL_BASE + "/authorize/?client_id=%s&redirect_uri=%s&response_type=code";

}
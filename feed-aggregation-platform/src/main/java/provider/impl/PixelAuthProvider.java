package provider.impl;

import provider.OAuthProvider;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class PixelAuthProvider implements OAuthProvider {
    //TODO Should move to use system properties or load from a local secured file
    private static final String clientId = "iZGTkoZXEI9QfzjkF4gDXPTuVDoiUgD5qG2qLSR8";
    private static final String clientSecret = "y3M9d8VKoExBRxPYAiYE6ZUulp40HYspBlJxpmJ0";

    private static PixelAuthProvider instance;

    /**
     * Singleton Pattern for retrieving the instagram auth provider
     * @return
     */
    public static PixelAuthProvider getInstance(){
        if(instance == null){
            instance = new PixelAuthProvider();
        }
        return instance;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }
}

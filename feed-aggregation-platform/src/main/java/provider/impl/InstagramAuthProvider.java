package provider.impl;

import provider.OAuthProvider;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class InstagramAuthProvider implements OAuthProvider{
    private static final String clientId = "deacb3a914c542e9b19aba04a77dd952";
    private static final String clientSecret = "effdcc6a4a5a42bab5b6d306edad5347";

    private static InstagramAuthProvider instance;

    /**
     * Singleton Pattern for retrieving the instagram auth provider
     * @return
     */
    public static InstagramAuthProvider getInstance(){
        if(instance == null){
            instance = new InstagramAuthProvider();
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

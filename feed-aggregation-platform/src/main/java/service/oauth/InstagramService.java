package service.oauth;

import org.jinstagram.Instagram;
import service.oauth.instagram.impl.InstagramClient;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class InstagramService {

    private String clientId;

    private String accessToken;

    private Instagram instagram;

    private InstagramClient instagramClient;

    public static InstagramService service = new InstagramService();

    public Instagram getInstagram(){
        return instagram;
    };

    public void setInstagram(Instagram instagram) {
        this.instagram = instagram;
    }

    public InstagramClient getInstagramClient() {
        return instagramClient;
    }

    public void setInstagramClient(InstagramClient instagramClient) {
        this.instagramClient = instagramClient;
    }
}

package service.oauth;

import org.jinstagram.Instagram;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class InstagramService {

    private Instagram instagram;

    public static InstagramService service = new InstagramService();

    public Instagram getInstagram(){
        return instagram;
    };

    public void setInstagram(Instagram instagram) {
        this.instagram = instagram;
    }
}

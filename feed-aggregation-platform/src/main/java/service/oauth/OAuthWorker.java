package service.oauth;

import exception.OAuthException;
import model.OAuthCredentials;

import java.io.UnsupportedEncodingException;

/**
 * Created by Yvonne on 2016-03-06.
 */
public interface OAuthWorker {

    /**
     * Handles the request (1st) phase of the OAuth dance. Generates a URL containing tokens to be authenticated by the end user
     * @return
     */
    String buildAuthorizationURL() throws UnsupportedEncodingException, OAuthException;

    /**
     * Handles the grant phase in the OAuth dance. After the user has granted us access, this method is called with the authenticated tokens
     * @return
     */
    OAuthCredentials generateTokens(String codeValue) throws OAuthException;

    /**
     * TODO not implemented
     * Handles the refresh phase.
     * @return
     */
//    String performTokenRefresh();



}

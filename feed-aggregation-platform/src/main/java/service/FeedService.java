package service;

import common.model.content.Feed;
import common.model.content.PaginatedFeeds;
import exception.FeedServiceException;
import exception.InstagramClientException;
import model.OAuthCredentials;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by Yvonne on 2016-03-06.
 */
public interface FeedService {
    PaginatedFeeds getFeeds(OAuthCredentials oAuthCredentials) throws FeedServiceException;
    //with pagination
    PaginatedFeeds getFeeds(OAuthCredentials oAuthCredentials, Map<String, Object> params)  throws FeedServiceException;

    Feed getFeed(OAuthCredentials oAuthCredentials, String id) throws FeedServiceException;
}

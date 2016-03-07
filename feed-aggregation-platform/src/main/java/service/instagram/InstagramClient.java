package service.instagram;

import common.model.content.Feed;
import exception.InstagramClientException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Yvonne on 2016-03-06.
 */
public interface InstagramClient {
    JsonObject getUserRecentMedia() throws InstagramClientException, IOException, URISyntaxException;
    JsonObject getUserRecentMedia(int count, String minId, String maxId) throws InstagramClientException, IOException, URISyntaxException;
    JsonObject getMediaInfo(String mediaId) throws InstagramClientException, IOException, URISyntaxException;

    String API_ENDPOINT = "https://api.instagram.com/v1";

    class QueryParam {
        /**
         * MAX_ID	Return media earlier than this max_id.
         */
        public final static String MAX_ID = "max_id";

        /**
         * MAX_ID	Return media earlier than this max_id.
         */
        public final static String MIN_ID = "min_id";

        /**
         * COUNT	Count of media to return.
         */
        public final static String COUNT = "count";
    }
}

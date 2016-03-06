package service.pixel;

import exception.PixelClientException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Yvonne on 2016-03-06.
 */
public interface PixelClient {
    String API_ENDPOINT = "https://api.500px.com/v1";

    JsonArray getPhotos() throws PixelClientException, IOException, URISyntaxException;
    JsonArray getPhotos(int pageNumber) throws PixelClientException, IOException, URISyntaxException;
    JsonObject getPhoto(String id) throws IOException, URISyntaxException, PixelClientException;

    class QueryParam {
        /**
         * PAGE page of photo stream. 1-based
         *
         */
        public final static String PAGE = "page";

        /**
         * MAX_ID	Return media earlier than this max_id.
         */
        public final static String FEATURE = "min_id";

        /**
         * COUNT	Count of media to return.
         */
        public final static String COUNT = "count";
    }

    class Feature {
        public final static String POPULAR = "popular";
        public final static String HIGHEST_RATED = "highest_rated";
        public final static String UPCOMING = "upcoming";
        public final static String EDITORS = "editors";
        public final static String FRESH_TODAY = "fresh_today";
        public final static String FRESH_YESTERDAY = "fresh_yesterday";
        public final static String FRESH_WEEK = "fresh_week";
    }
}

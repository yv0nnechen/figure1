package http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class HttpClientExecutor {
    private static CloseableHttpClient httpClient;
    private static HttpClientExecutor instance;
    private static final Logger logger = LoggerFactory.getLogger(HttpClientExecutor.class);
    static {
        //init here so that it's started on system startup
        //TODO close the client
        httpClient = HttpClients.createDefault();
        instance = new HttpClientExecutor();
    }

    public static HttpClientExecutor getInstance() {
        return instance;
    }

    public SimpleHttpResponse perform(HttpUriRequest httpRequest) throws IOException {

        CloseableHttpResponse response = null;
        try {
            response= httpClient.execute(httpRequest);

            HttpEntity responseEntity = response.getEntity();
            String retSrc = EntityUtils.toString(responseEntity);
            logger.info("{} {} performed successful.", httpRequest.getMethod(), httpRequest.getURI());
            return new SimpleHttpResponse(response.getStatusLine().getStatusCode(), retSrc);
        } catch (IOException e) {
            logger.error("Error in http execution.", e);
            //propagate the error
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}

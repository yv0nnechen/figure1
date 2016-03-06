package http;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class SimpleHttpResponse {
    private int statusCode;
    private String rawResponse;

    public SimpleHttpResponse(int statusCode, String rawResponse) {
        this.statusCode = statusCode;
        this.rawResponse = rawResponse;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getRawResponse() {
        return rawResponse;
    }
}

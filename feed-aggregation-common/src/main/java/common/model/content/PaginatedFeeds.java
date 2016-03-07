package common.model.content;

import java.util.List;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class PaginatedFeeds {
    private List<Feed> feeds;
    private Pagination pagination;

    public List<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}

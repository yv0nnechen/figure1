package common.model.content;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class Pagination {
    private int page;
    //used for return media earlier than this max_id
    private String maxId;
    //used for return media later than this min_id
    private String minId;
    private int count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getMaxId() {
        return maxId;
    }

    public void setMaxId(String maxId) {
        this.maxId = maxId;
    }

    public String getMinId() {
        return minId;
    }

    public void setMinId(String minId) {
        this.minId = minId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

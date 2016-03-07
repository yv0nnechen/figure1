package common.model.content;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class Pagination {
    private int page;
    private String max_id;
    private String min_id;
    private int count;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getMax_id() {
        return max_id;
    }

    public void setMax_id(String max_id) {
        this.max_id = max_id;
    }

    public String getMin_id() {
        return min_id;
    }

    public void setMin_id(String min_id) {
        this.min_id = min_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

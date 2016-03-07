package common.model.content;

/**
 * Created by Yvonne on 2016-03-06.
 */
public class Feed {
    private ContentType contentType;
    private String id;
    private String location;
    private int likeCount;
    private int commentCount;
    private String userName;
    private String caption;
    private long createdTime;

    private Feed() {}

    private Feed(ContentType contentType, String id, String location, int likeCount, int commentCount, String userName, String caption, long createdTime) {
        this.contentType = contentType;
        this.id = id;
        this.location = location;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.userName = userName;
        this.caption = caption;
        this.createdTime = createdTime;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public static class FeedBuilder {
        private ContentType contentType;
        private String id;
        private String location;
        private int likeCount;
        private int commentCount;
        private String userName;
        private String caption;
        private long createdTime;

        public FeedBuilder setContentType(ContentType contentType) {
            this.contentType = contentType;
            return this;
        }

        public FeedBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public FeedBuilder setLocation(String location) {
            this.location = location;
            return this;
        }

        public FeedBuilder setLikeCount(int likeCount) {
            this.likeCount = likeCount;
            return this;
        }

        public FeedBuilder setCommentCount(int commentCount) {
            this.commentCount = commentCount;
            return this;
        }

        public FeedBuilder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public FeedBuilder setCaption(String caption) {
            this.caption = caption;
            return this;
        }

        public FeedBuilder setCreatedTime(long createdTime) {
            this.createdTime = createdTime;
            return this;
        }

        public Feed createFeed() {
            return new Feed(contentType, id, location, likeCount, commentCount, userName, caption, createdTime);
        }
    }

}

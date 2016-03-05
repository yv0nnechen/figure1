package common.model.content;

/**
 * Created by Yvonne on 2016-03-05.
 */
public class ContentDescriptor {
    private String id;
    private String ownerId;
    private String originalFileName;
    private EndpointReference endpointReference;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public EndpointReference getEndpointReference() {
        return endpointReference;
    }

    public void setEndpointReference(EndpointReference endpointReference) {
        this.endpointReference = endpointReference;
    }
}

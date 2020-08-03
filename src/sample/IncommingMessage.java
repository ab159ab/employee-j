package sample;

public class IncommingMessage {

    String internalTime;
    String imageStatus;

    public String getInternalTime() {
        return internalTime;
    }

    public void setInternalTime(String internalTime) {
        this.internalTime = internalTime;
    }

    public String getimageStatus() {
        return imageStatus;
    }

    public void setimageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public IncommingMessage(String internalTime, String imageStatus) {
        this.internalTime = internalTime;
        this.imageStatus = imageStatus;
    }
}

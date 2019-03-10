package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 3/2/2018.
 */

public class NotificationsModel {

    private String singleNotifications ;
    private String justPic ;


    public NotificationsModel() {
    }

    public NotificationsModel(String singleNotifications, String justPic) {
        this.singleNotifications = singleNotifications;
        this.justPic = justPic;
    }

    public String getJustPic() {
        return justPic;
    }

    public void setJustPic(String justPic) {
        this.justPic = justPic;
    }

    public String getSingleNotifications() {
        return singleNotifications;
    }

    public void setSingleNotifications(String singleNotifications) {
        this.singleNotifications = singleNotifications;
    }
}

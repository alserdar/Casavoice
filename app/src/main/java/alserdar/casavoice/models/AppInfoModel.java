package alserdar.casavoice.models;

import java.util.Date;

public class AppInfoModel {


    public AppInfoModel() {
    }

    private String facebookUserName ;
    private String userName ;
    private String theUID ;
    private String country ;
    private String status ;
    private Date joinApp ;
    private Date lastSeen ;

    public AppInfoModel(String facebookUserName, String userName, String theUID, String country, String status, Date joinApp, Date lastSeen) {
        this.facebookUserName = facebookUserName;
        this.userName = userName;
        this.theUID = theUID;
        this.country = country;
        this.status = status;
        this.joinApp = joinApp;
        this.lastSeen = lastSeen;
    }

    public String getFacebookUserName() {
        return facebookUserName;
    }

    public void setFacebookUserName(String facebookUserName) {
        this.facebookUserName = facebookUserName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTheUID() {
        return theUID;
    }

    public void setTheUID(String theUID) {
        this.theUID = theUID;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getJoinApp() {
        return joinApp;
    }

    public void setJoinApp(Date joinApp) {
        this.joinApp = joinApp;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }
}

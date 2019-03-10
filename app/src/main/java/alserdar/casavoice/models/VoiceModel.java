package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 2/25/2018.
 */

public class VoiceModel {

    public VoiceModel() {}


    private String roomId ;
    private String mics ;
    private String userName ;
    private String userUID ;
    private String nickName ;
    private String status ;
    private String theUri ;

    public VoiceModel(String roomId, String mics, String userName, String userUID, String nickName, String status, String theUri) {
        this.roomId = roomId;
        this.mics = mics;
        this.userName = userName;
        this.userUID = userUID;
        this.nickName = nickName;
        this.status = status;
        this.theUri = theUri;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getTheUri() {
        return theUri;
    }

    public void setTheUri(String theUri) {
        this.theUri = theUri;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getMics() {
        return mics;
    }

    public void setMics(String mics) {
        this.mics = mics;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}

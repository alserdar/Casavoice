package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 1/21/2018.
 */

public class FaveRoomsModel {

    private String faveRoomOwner ;
    private String faveRoomId ;
    private String faveRoomName ;
    private String faveRoomOwnerUID ;

    public FaveRoomsModel() {
    }

    public FaveRoomsModel(String faveRoomOwner , String faveRoomOwnerUID , String faveRoomId, String faveRoomName) {
        this.faveRoomOwner = faveRoomOwner;
        this.faveRoomId = faveRoomId;
        this.faveRoomName = faveRoomName;
        this.faveRoomOwnerUID = faveRoomOwnerUID ;
    }

    public String getFaveRoomOwnerUID() {
        return faveRoomOwnerUID;
    }

    public void setFaveRoomOwnerUID(String faveRoomOwnerUID) {
        this.faveRoomOwnerUID = faveRoomOwnerUID;
    }

    public String getFaveRoomOwner() {
        return faveRoomOwner;
    }

    public void setFaveRoomOwner(String faveRoomOwner) {
        this.faveRoomOwner = faveRoomOwner;
    }

    public String getFaveRoomId() {
        return faveRoomId;
    }

    public void setFaveRoomId(String faveRoomId) {
        this.faveRoomId = faveRoomId;
    }

    public String getFaveRoomName() {
        return faveRoomName;
    }

    public void setFaveRoomName(String faveRoomName) {
        this.faveRoomName = faveRoomName;
    }
}

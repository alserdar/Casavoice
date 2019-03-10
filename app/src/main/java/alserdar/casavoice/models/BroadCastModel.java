package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 2/7/2018.
 */

public class BroadCastModel {

    private String sender ;
    private String rec ;
    private String gift ;
    private int exact ;
    private String roomIdForBroadcasting ;

    public BroadCastModel() {
    }


    public BroadCastModel(String sender, String rec, String gift, int exact , String roomIdForBroadcasting) {
        this.sender = sender;
        this.rec = rec;
        this.gift = gift;
        this.exact = exact;
        this.roomIdForBroadcasting = roomIdForBroadcasting;
    }

    public String getRoomIdForBroadcasting() {
        return roomIdForBroadcasting;
    }

    public void setRoomIdForBroadcasting(String roomIdForBroadcasting) {
        this.roomIdForBroadcasting = roomIdForBroadcasting;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRec() {
        return rec;
    }

    public void setRec(String rec) {
        this.rec = rec;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public int getExact() {
        return exact;
    }

    public void setExact(int exact) {
        this.exact = exact;
    }
}

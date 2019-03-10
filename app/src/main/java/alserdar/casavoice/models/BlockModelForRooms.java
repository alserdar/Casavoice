package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 12/24/2017.
 */

public class BlockModelForRooms {

    private String blockedForRooms ;
    private String blockedForRoomsUID ;

    public BlockModelForRooms() {
    }


    public BlockModelForRooms(String blockedForRooms, String blockedForRoomsUID) {
        this.blockedForRooms = blockedForRooms;
        this.blockedForRoomsUID = blockedForRoomsUID;
    }

    public String getBlockedForRoomsUID() {
        return blockedForRoomsUID;
    }

    public void setBlockedForRoomsUID(String blockedForRoomsUID) {
        this.blockedForRoomsUID = blockedForRoomsUID;
    }

    public String getBlockedForRooms() {
        return blockedForRooms;
    }

    public void setBlockedForRooms(String blockedForRooms) {
        this.blockedForRooms = blockedForRooms;
    }
}

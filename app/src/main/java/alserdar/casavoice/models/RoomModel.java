package alserdar.casavoice.models;

import java.util.Date;
import java.util.List;

/**
 * Created by ALAZIZY on 11/28/2017.
 */

public class RoomModel {

    public RoomModel() {
    }

    private String roomName  , roomOwnerUID , roomId, roomOwner , infoAboutRoom , emptyShitAboutRoom , peopleInTheRoom , lockPeriod;
    private boolean roomCreated = false ;
    private boolean roomLocked = false ;
    private Long maxUsersInTheRoom ;
    private List<String> onlineUsers;
    private long pass ;
    private Date startLockDate;
    private Date endLockDate;


    public RoomModel(String roomName, String roomOwner, String infoAboutRoom, String emptyShitAboutRoom,
                     String peopleInTheRoom, boolean roomCreated,
                     boolean roomLocked, Long maxUsersInTheRoom, String roomId, List<String> onlineUsers) {
        this.roomName = roomName;
        this.roomOwner = roomOwner;
        this.infoAboutRoom = infoAboutRoom;
        this.emptyShitAboutRoom = emptyShitAboutRoom;
        this.peopleInTheRoom = peopleInTheRoom;
        this.roomCreated = roomCreated;
        this.roomLocked = roomLocked;
        this.maxUsersInTheRoom = maxUsersInTheRoom;
        this.roomId = roomId;
        this.onlineUsers = onlineUsers;
    }

    public String getRoomOwnerUID() {
        return roomOwnerUID;
    }

    public void setRoomOwnerUID(String roomOwnerUID) {
        this.roomOwnerUID = roomOwnerUID;
    }

    public String getLockPeriod() {
        return lockPeriod;
    }

    public void setLockPeriod(String lockPeriod) {
        this.lockPeriod = lockPeriod;
    }

    public Date getStartLockDate() {
        return startLockDate;
    }

    public void setStartLockDate(Date startLockDate) {
        this.startLockDate = startLockDate;
    }

    public Date getEndLockDate() {
        return endLockDate;
    }

    public void setEndLockDate(Date endLockDate) {
        this.endLockDate = endLockDate;
    }

    public long getPass() {
        return pass;
    }

    public void setPass(long pass) {
        this.pass = pass;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public String getInfoAboutRoom() {
        return infoAboutRoom;
    }

    public void setInfoAboutRoom(String infoAboutRoom) {
        this.infoAboutRoom = infoAboutRoom;
    }

    public String getEmptyShitAboutRoom() {
        return emptyShitAboutRoom;
    }

    public void setEmptyShitAboutRoom(String emptyShitAboutRoom) {
        this.emptyShitAboutRoom = emptyShitAboutRoom;
    }

    public String getPeopleInTheRoom() {
        return peopleInTheRoom;
    }

    public void setPeopleInTheRoom(String peopleInTheRoom) {
        this.peopleInTheRoom = peopleInTheRoom;
    }

    public boolean isRoomCreated() {
        return roomCreated;
    }

    public void setRoomCreated(boolean roomCreated) {
        this.roomCreated = roomCreated;
    }

    public boolean isRoomLocked() {
        return roomLocked;
    }

    public void setRoomLocked(boolean roomLocked) {
        this.roomLocked = roomLocked;
    }

    public Long getMaxUsersInTheRoom() {
        return maxUsersInTheRoom;
    }

    public void setMaxUsersInTheRoom(Long maxUsersInTheRoom) {
        this.maxUsersInTheRoom = maxUsersInTheRoom;
    }

    public List<String> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(List<String> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }
}

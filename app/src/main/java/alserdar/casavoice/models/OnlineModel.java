package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 12/23/2017.
 */

public class OnlineModel {

    private String onlineDude ;
    private String onlineDudeUID ;
    private String onlineDudeNickname ;


    public OnlineModel(String onlineDude, String onlineDudeUID, String onlineDudeNickname) {
        this.onlineDude = onlineDude;
        this.onlineDudeUID = onlineDudeUID;
        this.onlineDudeNickname = onlineDudeNickname ;
    }

    public String getOnlineDudeUID() {
        return onlineDudeUID;
    }

    public void setOnlineDudeUID(String onlineDudeUID) {
        this.onlineDudeUID = onlineDudeUID;
    }

    public OnlineModel() {
    }

    public String getOnlineDudeNickname() {
        return onlineDudeNickname;
    }

    public void setOnlineDudeNickname(String onlineDudeNickname) {
        this.onlineDudeNickname = onlineDudeNickname;
    }

    public String getOnlineDude() {
        return onlineDude;
    }

    public void setOnlineDude(String onlineDude) {
        this.onlineDude = onlineDude;
    }
}

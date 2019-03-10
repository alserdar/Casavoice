package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 1/16/2018.
 */

public class MyMessageListModel {

    private String to ;
    private String toUID ;
    private String messageDate ;

    public MyMessageListModel() {
    }

    public MyMessageListModel(String to, String messageDate , String toUID) {
        this.to = to;
        this.messageDate = messageDate;
        this.toUID = toUID ;
    }

    public String getToUID() {
        return toUID;
    }

    public void setToUID(String toUID) {
        this.toUID = toUID;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }
}

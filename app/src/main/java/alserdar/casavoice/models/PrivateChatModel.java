package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 11/30/2017.
 */

public class PrivateChatModel {
    private String privateMessageText ;
    private String messageFrom ;
    private String messageFromUID ;
    private String messageFromNickName ;
    private String messageDate ;


    public PrivateChatModel() {
    }

    public PrivateChatModel(String privateMessageText, String messageFrom, String messageFromNickName, String messageDate , String messageFromUID) {
        this.privateMessageText = privateMessageText;
        this.messageFrom = messageFrom;
        this.messageFromNickName = messageFromNickName;
        this.messageDate = messageDate;
        this.messageFromUID = messageFromUID ;
    }

    public String getMessageFromUID() {
        return messageFromUID;
    }

    public void setMessageFromUID(String messageFromUID) {
        this.messageFromUID = messageFromUID;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageFromNickName() {
        return messageFromNickName;
    }

    public void setMessageFromNickName(String messageFromNickName) {
        this.messageFromNickName = messageFromNickName;
    }

    public String getPrivateMessageText() {
        return privateMessageText;
    }

    public void setPrivateMessageText(String privateMessageText) {
        this.privateMessageText = privateMessageText;
    }

    public String getMessageFrom() {
        return messageFrom;
    }

    public void setMessageFrom(String messageFrom) {
        this.messageFrom = messageFrom;
    }
}

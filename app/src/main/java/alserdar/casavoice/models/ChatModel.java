package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 11/30/2017.
 */

public class ChatModel {
    private String messageText ;
    private String messageUser ;
    private String messageUserUID ;
    private String messageNickName ;
    private int gifts ;
    private String theFuckinPath ;
    private String theVip ;


    public ChatModel() {
    }

    public ChatModel(String messageText, String messageUser,
                     String messageUserUID, String messageNickName, String theFuckinPath , String theVip , int gifts) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageUserUID = messageUserUID;
        this.messageNickName = messageNickName;
        this.gifts = gifts;
        this.theFuckinPath = theFuckinPath;
        this.theVip = theVip;
    }

    public String getMessageUserUID() {
        return messageUserUID;
    }

    public void setMessageUserUID(String messageUserUID) {
        this.messageUserUID = messageUserUID;
    }

    public String getTheVip() {
        return theVip;
    }

    public void setTheVip(String theVip) {
        this.theVip = theVip;
    }

    public String getMessageNickName() {
        return messageNickName;
    }

    public void setMessageNickName(String messageNickName) {
        this.messageNickName = messageNickName;
    }

    public String getTheFuckinPath() {
        return theFuckinPath;
    }

    public void setTheFuckinPath(String theFuckinPath) {
        this.theFuckinPath = theFuckinPath;
    }

    public int getGifts() {
        return gifts;
    }

    public void setGifts(int gifts) {
        this.gifts = gifts;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

}

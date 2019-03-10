package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 1/15/2018.
 */

public class FollowingModel {

    private String theFollowingDudes ;
    private String theFollowingDudesUID ;


    public FollowingModel() {
    }

    public FollowingModel(String theFollowingDudes, String theFollowingDudesUID) {
        this.theFollowingDudes = theFollowingDudes;
        this.theFollowingDudesUID = theFollowingDudesUID;
    }

    public String getTheFollowingDudesUID() {
        return theFollowingDudesUID;
    }

    public void setTheFollowingDudesUID(String theFollowingDudesUID) {
        this.theFollowingDudesUID = theFollowingDudesUID;
    }

    public String getTheFollowingDudes() {
        return theFollowingDudes;
    }

    public void setTheFollowingDudes(String theFollowingDudes) {
        this.theFollowingDudes = theFollowingDudes;
    }
}

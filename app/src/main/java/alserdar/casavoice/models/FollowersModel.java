package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 1/15/2018.
 */

public class FollowersModel {



    private String theFollowersDudes ;
    private String theFollowersDudesUID ;

    public FollowersModel() {
    }

    public FollowersModel(String theFollowersDudes, String theFollowersDudesUID) {
        this.theFollowersDudes = theFollowersDudes;
        this.theFollowersDudesUID = theFollowersDudesUID;
    }

    public String getTheFollowersDudesUID() {
        return theFollowersDudesUID;
    }

    public void setTheFollowersDudesUID(String theFollowersDudesUID) {
        this.theFollowersDudesUID = theFollowersDudesUID;
    }

    public String getTheFollowersDudes() {
        return theFollowersDudes;
    }

    public void setTheFollowersDudes(String theFollowersDudes) {
        this.theFollowersDudes = theFollowersDudes;
    }
}

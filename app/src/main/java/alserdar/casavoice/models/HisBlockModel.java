package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 1/18/2018.
 */

public class HisBlockModel {

    private String blockMe ;
    private String blockMeUID ;

    public HisBlockModel() {
    }

    public HisBlockModel(String blockMe, String blockMeUID) {
        this.blockMe = blockMe;
        this.blockMeUID = blockMeUID;
    }

    public String getBlockMeUID() {
        return blockMeUID;
    }

    public void setBlockMeUID(String blockMeUID) {
        this.blockMeUID = blockMeUID;
    }

    public String getBlockMe() {
        return blockMe;
    }

    public void setBlockMe(String blockMe) {
        this.blockMe = blockMe;
    }
}

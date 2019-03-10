package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 12/24/2017.
 */

public class MyBlockModel {

    private String blockedHim ;
    private String blockedHimUID ;

    public MyBlockModel() {
    }


    public MyBlockModel(String blockedHim, String blockedHimUID) {
        this.blockedHim = blockedHim;
        this.blockedHimUID = blockedHimUID;
    }

    public String getBlockedHimUID() {
        return blockedHimUID;
    }

    public void setBlockedHimUID(String blockedHimUID) {
        this.blockedHimUID = blockedHimUID;
    }

    public String getBlockedHim() {
        return blockedHim;
    }

    public void setBlockedHim(String blockedHim) {
        this.blockedHim = blockedHim;
    }
}

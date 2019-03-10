package alserdar.casavoice.models;

/**
 * Created by ALAZIZY on 12/24/2017.
 */

public class AdminsModel {

    private String admins ;
    private String adminsUID ;

    public AdminsModel() {
    }


    public AdminsModel(String admins, String adminsUID) {
        this.admins = admins;
        this.adminsUID = adminsUID;
    }

    public String getAdminsUID() {
        return adminsUID;
    }

    public void setAdminsUID(String adminsUID) {
        this.adminsUID = adminsUID;
    }

    public String getAdmins() {
        return admins;
    }

    public void setAdmins(String admins) {
        this.admins = admins;
    }
}

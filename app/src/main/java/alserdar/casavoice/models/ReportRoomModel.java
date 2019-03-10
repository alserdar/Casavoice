package alserdar.casavoice.models;

public class ReportRoomModel {

    public ReportRoomModel() {
    }

    private String reportSender ;
    private String reportSenderID;
    private String roomName ;
    private String roomId ;
    private String theReport ;
    private String roomOwner ;


    public ReportRoomModel(String reportSender, String reportSenderID, String roomName, String roomId, String theReport , String roomOwner) {
        this.reportSender = reportSender;
        this.reportSenderID = reportSenderID;
        this.roomName = roomName;
        this.roomId = roomId;
        this.theReport = theReport;
        this.roomOwner = roomOwner ;
    }

    public String getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(String roomOwner) {
        this.roomOwner = roomOwner;
    }

    public String getReportSender() {
        return reportSender;
    }

    public void setReportSender(String reportSender) {
        this.reportSender = reportSender;
    }

    public String getReportSenderID() {
        return reportSenderID;
    }

    public void setReportSenderID(String reportSenderID) {
        this.reportSenderID = reportSenderID;
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

    public String getTheReport() {
        return theReport;
    }

    public void setTheReport(String theReport) {
        this.theReport = theReport;
    }
}

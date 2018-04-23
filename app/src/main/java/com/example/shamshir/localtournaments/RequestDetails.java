package com.example.shamshir.localtournaments;

public class RequestDetails {



    private String requestId;
    private String Requestor;
    private String mobileNumber;
    private String clubName;

    public RequestDetails(String requestId, String requestor, String mobileNumber, String clubName) {
        this.requestId = requestId;
        Requestor = requestor;
        this.mobileNumber = mobileNumber;
        this.clubName = clubName;
    }

    public RequestDetails(){

    }
    public String getRequestId() {
        return requestId;
    }

    public String getRequestor() {
        return Requestor;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getClubName() {
        return clubName;
    }
}

package com.example.shamshir.localtournaments;

public class ProfileDetails {



    private String id;
    private String name;
    private String mobileNumber;
    private String password;
    private String clubName;


    public ProfileDetails(String id, String name, String mobileNumber, String password, String clubName) {
        this.id = id;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.clubName = clubName;
    }


    public ProfileDetails() {}

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getClubName() {
        return clubName;
    }
}

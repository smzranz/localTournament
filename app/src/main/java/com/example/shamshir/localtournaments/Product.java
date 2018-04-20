package com.example.shamshir.localtournaments;

import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Belal on 10/18/2017.
 */


public class Product {
    private String id;
    private String title;
    private String place;
    private String date;
    private String price;
    private String selectedGame;
    private String image;
    private String contact;
    private String totalTeams;
    private String moreInfo;
    private String district;

    private String clientID;




    public Product(String id, String title, String place, String date, String selectedGame, String price, String image, String contact, String totalTeams, String moreInfo, String district, String clientID) {
        this.id = id;
        this.title = title;
        this.place = place;
        this.date = date;
        this.price = price;
        this.image = image;
        this.selectedGame = selectedGame;
        this.contact = contact;
        this.district = district;

        this.totalTeams = totalTeams;
        this.moreInfo = moreInfo;
        this.clientID = clientID;
    }

    public Product() {}


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPlace() {
        return place;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public String getSelectedGame() {
        return selectedGame;
    }

    public String getContact() {
        return contact;
    }

    public String getTotalTeams() {
        return totalTeams;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public String getDistrict() {
        return district;
    }

    public String getClientID() {
        return clientID;
    }
}


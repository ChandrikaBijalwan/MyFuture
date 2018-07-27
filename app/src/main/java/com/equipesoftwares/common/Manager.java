package com.equipesoftwares.common;

import android.content.Context;

import com.equipesoftwares.services.AppController;

/**
 * Created by My_Machine on 20/09/2016.
 */
public class Manager {
    private static Manager manager;
    private Context context;
    double currentLatitude, currentLongitude;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static Manager getInstance() {
        if (manager == null) {
            manager = new Manager();
        }
        return manager;
    }

    public Manager() {
        try {
            context = AppController.getAppContext();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // Getter-Setter Methods
    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public void setCurrentLatitude(double currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public void setCurrentLongitude(double currentLongitude) {
        this.currentLongitude = currentLongitude;
    }
}


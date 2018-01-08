package com.lifeband.lifeband.application;

import android.app.Application;

import com.android.volley.toolbox.Volley;

public class LifebandApplication extends Application {

    private static GlobalVars globalVars;

    private static GlobalServices globalServices;

    public GlobalVars getGlobalVars() {
        if(globalVars == null) {
            globalVars = new GlobalVars();
        }
        return globalVars;
    }

    public GlobalServices getGlobalServices() {
        if(globalServices == null) {
            globalServices = new GlobalServices(Volley.newRequestQueue(getApplicationContext()));
        }
        return globalServices;
    }

}

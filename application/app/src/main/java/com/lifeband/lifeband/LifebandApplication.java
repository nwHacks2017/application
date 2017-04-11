package com.lifeband.lifeband;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class LifebandApplication extends Application {

    private static RequestQueue requestQueue;

    private static BackendClient backendClient;

    private static PatientRepository patientRepository;

    public RequestQueue getRequestQueue() {
        return Volley.newRequestQueue(getApplicationContext());
    }

    public BackendClient getBackendClient() {
        if(backendClient == null) {
            backendClient = new BackendClient(getRequestQueue());
        }
        return backendClient;
    }

    public PatientRepository getPatientRepository() {
        if(patientRepository == null) {
            patientRepository = new PatientRepository(getBackendClient());
        }
        return patientRepository;
    }

}

package com.lifeband.lifeband.application;

import com.android.volley.RequestQueue;
import com.lifeband.lifeband.BackendClient;
import com.lifeband.lifeband.PatientRepository;

public class GlobalServices {

    private static RequestQueue requestQueue;

    private static BackendClient backendClient;

    private static PatientRepository patientRepository;

    public GlobalServices(RequestQueue requestQueue) {
        GlobalServices.requestQueue = requestQueue;
    }

    public RequestQueue getRequestQueue() {
        // Set by constructor
        return requestQueue;
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

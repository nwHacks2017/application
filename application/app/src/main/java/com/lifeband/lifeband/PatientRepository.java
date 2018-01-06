package com.lifeband.lifeband;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.lifeband.lifeband.exception.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class PatientRepository {

    private static final String TAG = PatientRepository.class.getSimpleName();

    private BackendClient backendClient;

    public PatientRepository(BackendClient backendClient) {
        this.backendClient = backendClient;
    }

    public void getPatientById(String id, BackendClient.VolleyCallback callback) {
        Uri.Builder url = BackendClient.getBaseUrl();
        url.appendPath("patient");
        url.appendPath(id);

        Log.d(TAG, "Sending patient ID to server.");
        backendClient.sendRequest(Request.Method.GET, url.build(), callback);
    }

}
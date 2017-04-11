package com.lifeband.lifeband;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.lifeband.lifeband.exception.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

public class PatientRepository {

    private static final String TAG = PatientRepository.class.getSimpleName();

    private BackendClient backendClient;

    public PatientRepository(BackendClient backendClient) {
        this.backendClient = backendClient;
    }

    public PatientData getPatientById(String id) throws ServerException {
        Uri.Builder url = BackendClient.getBaseUrl();
        url.appendPath("patient");
        url.appendPath(id);

        JSONObject response = backendClient.sendRequest(Request.Method.GET, url.build());

        PatientData patientData = null;
        try {
            patientData = PatientData.fromJsonObject(response);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON response from server.");
            e.printStackTrace();
            // TODO: Throw exception
        }
        return patientData;
    }

}
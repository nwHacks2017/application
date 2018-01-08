package com.lifeband.lifeband.activity;

/*
 * Receives tag data from NfcActivity.java and waits for request from server.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.lifeband.lifeband.R;
import com.lifeband.lifeband.application.GlobalVars;
import com.lifeband.lifeband.application.LifebandApplication;
import com.lifeband.lifeband.data.PatientData;
import com.lifeband.lifeband.service.BackendClient;

import org.json.JSONException;
import org.json.JSONObject;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = LoadingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        String tagData = getIntent().getStringExtra("tagData");
        Log.d(TAG, "Receiving Extra tagData: " + tagData);

        getGlobalVars().setCurrentPatientData(null);
        requestPatientData(tagData);
    }

    private void requestPatientData(String tagData) {

        ((LifebandApplication) getApplication())
                .getGlobalServices()
                .getPatientRepository()
                .getPatientById(tagData, new BackendClient.VolleyCallback() {
                    @Override
                    public void onSuccessResponse(JSONObject response) {
                        PatientData patientData = null;

                        try {
                            patientData = PatientData.fromJsonObject(response);
                        } catch (JSONException e) {
                            Log.e(TAG, "Failed to parse JSON response from server.");
                            e.printStackTrace();
                            // TODO: Throw exception
                        }

                        getGlobalVars().setCurrentPatientData(patientData);

                        Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(newIntent);
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                        error.printStackTrace();
                        String errorMessage= "Empty error message";

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            errorMessage = "Could not connect to the server.";
                        } else if (error instanceof AuthFailureError) {
                            errorMessage = "Failed to authenticate with the server.";
                        } else if (error instanceof ServerError) {
                            errorMessage = "The server returned an internal error.";
                        } else if (error instanceof NetworkError) {
                            errorMessage = "Unknown network error.";
                        } else if (error instanceof ParseError) {
                            errorMessage = "The server response could not be understood.";
                        }

                        Toast.makeText(getApplication(), errorMessage, Toast.LENGTH_LONG).show();
                        finish();
                    }
                });

        // Data is returned asynchronously
    }

    private GlobalVars getGlobalVars() {
        return ((LifebandApplication) getApplication()).getGlobalVars();
    }

}
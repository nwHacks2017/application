package com.lifeband.lifeband.activity;

/*
 * Receives tag data from NfcActivity.java and waits for request from server.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

import java.net.HttpURLConnection;
import java.util.Optional;

public class LoadingActivity extends AppCompatActivity {

    private static final String TAG = LoadingActivity.class.getSimpleName();

    private Boolean successResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loading);

        String tagData = getIntent().getStringExtra("tagData");
        Log.d(TAG, "Receiving Extra tagData: " + tagData);

        getGlobalVars().setCurrentPatientData(null);
        successResponse = false;
        requestPatientData(tagData);
        waitForServerResponse();
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
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, error.toString());
                        error.printStackTrace();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(
                                    getApplication(),
                                    "Could not connect to the server.",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Failed to authenticate with the server.",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "The server returned an internal error.",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Unknown etwork error.",
                                    Toast.LENGTH_LONG
                            ).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "The server response could not be understood.",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                });

        // Data is returned asynchronously
    }

    private void waitForServerResponse() {

        successResponse = false;

        new CountDownTimer(6000, 200) {

            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "Checking if patient data has been received from the server.");
                if (getGlobalVars().getCurrentPatientData() != null) {
                    successResponse = true;
                    Intent newIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(newIntent);
                }
            }

            public void onFinish() {
                if (!successResponse) {
                    Log.i(TAG, "Timed out while retrieving patient data from server.");
                    // Notification to user is handled by callback response
                    finish();
                }
            }

        }.start();

    }

    private GlobalVars getGlobalVars() {
        return ((LifebandApplication) getApplication()).getGlobalVars();
    }

}
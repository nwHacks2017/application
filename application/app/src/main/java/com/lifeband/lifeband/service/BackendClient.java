package com.lifeband.lifeband.service;

import android.net.Uri;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class BackendClient {

    private RequestQueue requestQueue;

    private static final String TAG = BackendClient.class.getSimpleName();
    private static final String URL = "http://ec2-52-26-139-171.us-west-2.compute.amazonaws.com";
    private static final String PORT = "8080";

    public BackendClient(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static Uri.Builder getBaseUrl() {
        if(PORT.isEmpty()) {
            return Uri.parse(URL).buildUpon();
        }
        return Uri.parse(URL + ":" + PORT).buildUpon();
    }

    /**
     * Sends a request to the backend server and returns the parsed response.
     *
     * @param method com.android.volley.Request.Method value representing the tyep of REST call
     * @param url Full URL to send the request to
     */
    public void sendRequest(int method, Uri url, final VolleyCallback callback) {

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
            method, url.toString(), null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Received response from server.");
                    callback.onSuccessResponse(response);
                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    callback.onErrorResponse(error);
                }

            }
        );

        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Log.d(TAG, "Sending request to server.");
        requestQueue.add(jsonObjRequest);
    }

    public interface VolleyCallback {

        void onSuccessResponse(JSONObject response);

        void onErrorResponse(VolleyError error);

    }

}


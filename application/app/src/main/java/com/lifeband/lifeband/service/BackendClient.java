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

    private static final String TAG = BackendClient.class.getSimpleName();
    private static final String URL = "http://ec2-52-26-139-171.us-west-2.compute.amazonaws.com";
    private static final String PORT = "8080";

    private RequestQueue requestQueue;

    public BackendClient(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static Uri.Builder getBaseUrl() {
        if (PORT.isEmpty()) {
            return Uri.parse(URL).buildUpon();
        }
        return Uri.parse(URL + ":" + PORT).buildUpon();
    }

    /**
     * Sends a request to the backend server and returns the parsed response through the callback.
     *
     * @param method   com.android.volley.Request.Method value representing the type of REST call
     * @param url      Full URL (and port) to send the request to
     * @param callback Callback function to handle responses, success or failure
     */
    public void sendRequest(int method, Uri url, final VolleyCallback callback) {

        JsonObjectRequest request = new JsonObjectRequest(
                method, url.toString(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Received response from server " + URL);
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

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Log.d(TAG, "Sending request to server " + URL);
        requestQueue.add(request);
    }

    public interface VolleyCallback {

        void onSuccessResponse(JSONObject response);

        void onErrorResponse(VolleyError error);

    }

}


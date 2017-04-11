package com.lifeband.lifeband.activity;

import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import static com.lifeband.lifeband.LifebandExceptions.ServerException;

public class BackendClient {

    private static final String TAG = BackendClient.class.getSimpleName();
    private static final String URL = "http://ec2-52-26-139-171.us-west-2.compute.amazonaws.com";
    private static final String PORT = "8080";

    private RequestQueue requestQueue;

    private ServerException tmpException;
    private JSONObject tmpReturnObject;

    public BackendClient(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public static Uri.Builder getBaseUrl() {
        return Uri.parse(URL + PORT).buildUpon();
    }

    /**
     * Sends a request to the backend server and returns the parsed response.
     *
     * @param method com.android.volley.Request.Method value representing the tyep of REST call
     * @param url Full URL to send the request to
     * @return Patient data
     * @throws ServerException If the server returns an error
     */
    public JSONObject sendRequest(int method, Uri url) throws ServerException {
        clearTmpVars();

        JsonObjectRequest jsonObjRequest = new JsonObjectRequest(
            method, url.toString(), null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "Received response from server.");
                    tmpReturnObject = response;
                }
            }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                // For AuthFailure, you can re login with user credentials.
                // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                // In this case you can check how client is forming the api and debug accordingly.
                // For ServerError 5xx, you can do retry or handle accordingly.
            if( error instanceof NetworkError) {
            } else if( error instanceof ServerError) {
            } else if( error instanceof AuthFailureError) {
            } else if( error instanceof ParseError) {
            } else if( error instanceof NoConnectionError) {
            } else if( error instanceof TimeoutError) {
            }

            tmpException = new ServerException(ServerException.Reason.UNHANDLED_ERROR);
            }
        });

        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        Log.d(TAG, "Sending request to server.");
        requestQueue.add(jsonObjRequest);

        try {
            requestQueue.wait();
        } catch (InterruptedException e) {
            tmpException = new ServerException(
                    ServerException.Reason.UNHANDLED_ERROR,
                    "Thread was interrupted"
            );
        }
        if(tmpException != null) {
            throw tmpException;
        }

        return tmpReturnObject;
    }

    /**
     * Should be called at the beginning of each method with a request.
     */
    private void clearTmpVars() {
        tmpReturnObject = null;
        tmpException = null;
    }

}
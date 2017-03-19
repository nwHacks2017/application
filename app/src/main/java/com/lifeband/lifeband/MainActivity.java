package com.lifeband.lifeband;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private Button mTrigger;
    private RequestQueue mVolleyQueue;
    private ListView mListView;
    private ProgressDialog mProgress;
    private List<DataModel> mDataList;

    private ImageLoader mImageLoader;

    private PatientData patientData;

    private final String TAG_REQUEST = "MY_TAG";

    private class DataModel {
        private String mImageUrl;
        private String mTitle;
        public String getTitle() {
            return mTitle;
        }
        public void setTitle(String mTitle) {
            this.mTitle = mTitle;
        }

    }



    JsonObjectRequest jsonObjRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialise Volley Request Queue.
        mVolleyQueue = Volley.newRequestQueue(this);

        int max_cache_size = 1000000;

        //Memory cache is always faster than DiskCache. Check it our for yourself.
        //mImageLoader = new ImageLoader(mVolleyQueue, new BitmapCache(max_cache_size));

        mDataList = new ArrayList<DataModel>();

        mTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                makeSampleHttpRequest();
            }
        });
    }



    public void onDestroy() {
        super.onDestroy();
    }

    public void onStop() {
        super.onStop();
        if(mProgress != null)
            mProgress.dismiss();
        // Keep the list of requests dispatched in a List<Request<T>> mRequestList;
		/*
		 for( Request<T> req : mRequestList) {
		 	req.cancel();
		 }
		 */
        //jsonObjRequest.cancel();
        //( or )
        //mVolleyQueue.cancelAll(TAG_REQUEST);
    }

    private void makeSampleHttpRequest() {

        String url = "http://ec2-52-26-139-171.us-west-2.compute.amazonaws.com:8787";
        Uri.Builder builder = Uri.parse(url).buildUpon();
        builder.appendPath("patients");
        builder.appendQueryParameter("tagId", "tmp");


        jsonObjRequest = new JsonObjectRequest(Request.Method.GET, builder.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    patientData = PatientData.fromJsonObject(response);
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("JSON parse error");
                }
                stopProgress();
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

                stopProgress();
                showToast(error.getMessage());
            }
        });

        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjRequest.setTag(TAG_REQUEST);
        mVolleyQueue.add(jsonObjRequest);
    }

    private void showProgress() {
        mProgress = ProgressDialog.show(this, "", "Loading...");
    }

    private void stopProgress() {
        mProgress.cancel();
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

}


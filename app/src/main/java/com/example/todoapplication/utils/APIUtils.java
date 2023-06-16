package com.example.todoapplication.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class APIUtils {
    private static APIUtils mInstance;
    private RequestQueue mRequestQueue;

    private APIUtils(Context context) {
        mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized APIUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new APIUtils(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}

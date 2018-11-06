package com.rijalasepnugroho.absenapp.helper;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class Server {
    private static final String TAG = Server.class.getCanonicalName();
    private static Server mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private Server(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized Server getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new Server(context.getApplicationContext());
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.addMarker(TAG);
        req.setRetryPolicy(new DefaultRetryPolicy(
                120000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(req);
    }


}

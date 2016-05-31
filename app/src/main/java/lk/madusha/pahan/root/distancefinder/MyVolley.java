package lk.madusha.pahan.root.distancefinder;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MyVolley {
    private static RequestQueue mRequestQueue;

    private MyVolley()
    {

    }

    static void init(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);
    }


    public static RequestQueue getRequestQueue()
    {

        if (mRequestQueue != null)
        {
            return mRequestQueue;
        }
        else
        {
            return null;//throw new IllegalStateException("RequestQueue not initialized");
        }
    }

    public static RequestQueue newRequestQueue(Context context)
    {
        return Volley.newRequestQueue(context);
    }
}
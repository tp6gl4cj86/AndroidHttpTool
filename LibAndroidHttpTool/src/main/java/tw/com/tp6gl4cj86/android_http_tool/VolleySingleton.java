package tw.com.tp6gl4cj86.android_http_tool;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import tw.com.tp6gl4cj86.android_http_tool.HttpStack.OkHttpStack;

/**
 * Created by tp6gl4cj86 on 2016/12/5.
 */

public class VolleySingleton
{
    private static VolleySingleton mInstance;
    private        RequestQueue    mRequestQueue;
    private static Context         mContext;

    private VolleySingleton(Context context)
    {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue()
    {
        if (mRequestQueue == null)
        {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext(), new OkHttpStack(new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                                                                                                                               .build()));
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueue().add(req);
    }

}

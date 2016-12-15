package tw.com.tp6gl4cj86.android_http_tool;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by tp6gl4cj86 on 2016/12/5.
 */

public class VolleySingleton
{
    private static VolleySingleton mInstance;
    private        RequestQueue    mRequestQueue;
    //    private        ImageLoader     mImageLoader;
    private static Context         mContext;

    private VolleySingleton(Context context)
    {
        mContext = context;
        mRequestQueue = getRequestQueue();

        //        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache()
        //        {
        //            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);
        //
        //            @Override
        //            public Bitmap getBitmap(String url)
        //            {
        //                return cache.get(url);
        //            }
        //
        //            @Override
        //            public void putBitmap(String url, Bitmap bitmap)
        //            {
        //                cache.put(url, bitmap);
        //            }
        //        });
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

    //    public ImageLoader getImageLoader()
    //    {
    //        return mImageLoader;
    //    }
}

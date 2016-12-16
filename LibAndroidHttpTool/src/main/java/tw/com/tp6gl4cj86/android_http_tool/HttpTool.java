package tw.com.tp6gl4cj86.android_http_tool;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tw.com.tp6gl4cj86.android_http_tool.Listener.HttpListener;
import tw.com.tp6gl4cj86.android_http_tool.Listener.HttpListenerAdapter;


/**
 * Created by tp6gl4cj86 on 2016/6/14.
 */
public class HttpTool
{

    private static HttpListenerAdapter mStaticHttpListenerAdapter;

    public static void setStaticHttpListenerAdapter(HttpListenerAdapter mStaticHttpListenerAdapter)
    {
        HttpTool.mStaticHttpListenerAdapter = mStaticHttpListenerAdapter;
    }

    public static void post(Activity activity, String Url)
    {
        post(activity, Url, new HashMap<String, String>(), null);
    }

    public static void post(Activity activity, String Url, Map<String, String> params)
    {
        post(activity, Url, params, null);
    }

    public static void post(Activity activity, String Url, final HttpListener httpListener)
    {
        post(activity, Url, new HashMap<String, String>(), httpListener);
    }

    public static void post(final Activity activity, final String Url, final Map<String, String> params, final HttpListener httpListener)
    {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                if (response != null)
                {
                    if (!activity.isFinishing())
                    {
                        try
                        {
                            final String log = "Url : " + Url + "\nparams : " + (params != null ? params.toString() : "") + "\nResponse : " + response.toString();

                            if (httpListener != null)
                            {
                                httpListener.onSuccess(response, log);
                            }

                            if (mStaticHttpListenerAdapter != null)
                            {
                                mStaticHttpListenerAdapter.onSuccess(response, log);
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                if (!activity.isFinishing())
                {
                    final String errorStr = "VolleyError : " + error.getMessage() + "\nVolleyError body " + new String(error.networkResponse.data);

                    if (httpListener != null)
                    {
                        httpListener.onFailure(errorStr);
                    }

                    if (mStaticHttpListenerAdapter != null)
                    {
                        mStaticHttpListenerAdapter.onFailure(errorStr);
                    }
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };

        VolleySingleton.getInstance(activity)
                       .addToRequestQueue(jsonObjectRequest);
    }

}

package tw.com.tp6gl4cj86.android_http_tool;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

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

    public static void post(Activity activity, String url)
    {
        post(activity, url, new HashMap<String, String>(), null);
    }

    public static void post(Activity activity, String url, Map<String, String> params)
    {
        post(activity, url, params, null);
    }

    public static void post(Activity activity, String url, final HttpListener httpListener)
    {
        post(activity, url, new HashMap<String, String>(), httpListener);
    }

    public static void post(Activity activity, String url, Map<String, String> params, HttpListener httpListener)
    {
        requestJSON(Request.Method.POST, activity, url, params, httpListener);
    }

    public static void get(Activity activity, String url)
    {
        get(activity, url, new HashMap<String, String>(), null);
    }

    public static void get(Activity activity, String url, Map<String, String> params)
    {
        get(activity, url, params, null);
    }

    public static void get(Activity activity, String url, final HttpListener httpListener)
    {
        get(activity, url, new HashMap<String, String>(), httpListener);
    }

    public static void get(Activity activity, String url, Map<String, String> params, HttpListener httpListener)
    {
        requestString(Request.Method.GET, activity, url, params, httpListener);
    }

    public static void requestString(final int method, final Activity activity, final String url, final Map<String, String> params, final HttpListener httpListener)
    {
        final StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                if (response != null)
                {
                    if (!activity.isFinishing())
                    {
                        final String log = "url : " + url + "\nparams : " + (params != null ? params.toString() : "") + "\nResponse : " + response;

                        if (httpListener != null)
                        {
                            httpListener.onSuccess(response, log);
                        }

                        if (mStaticHttpListenerAdapter != null)
                        {
                            mStaticHttpListenerAdapter.onSuccess(response, log);
                        }
                    }
                }
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                httpToolOnErrorResponse(error, activity, httpListener);
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
                       .addToRequestQueue(stringRequest);
    }

    public static void requestJSON(final int method, final Activity activity, final String url, final Map<String, String> params, final HttpListener httpListener)
    {
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>()
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
                            final String log = "url : " + url + "\nparams : " + (params != null ? params.toString() : "") + "\nResponse : " + response.toString();

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
                httpToolOnErrorResponse(error, activity, httpListener);
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

    private static void httpToolOnErrorResponse(VolleyError error, Activity activity, HttpListener httpListener)
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

}

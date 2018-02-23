package tw.com.tp6gl4cj86.android_http_tool;

import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import tw.com.tp6gl4cj86.android_http_tool.Listener.HttpListener;
import tw.com.tp6gl4cj86.android_http_tool.Listener.HttpListenerAdapter;
import tw.com.tp6gl4cj86.android_http_tool.Request.DataPart;
import tw.com.tp6gl4cj86.android_http_tool.Request.VolleyMultipartRequest;


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

    public static void post(Context context, String url)
    {
        post(context, url, new HashMap<String, String>(), null);
    }

    public static void post(Context context, String url, Map<String, String> params)
    {
        post(context, url, params, null);
    }

    public static void post(Context context, String url, final HttpListener httpListener)
    {
        post(context, url, new HashMap<String, String>(), httpListener);
    }

    public static void post(Context context, String url, Map<String, String> params, HttpListener httpListener)
    {
        requestJSON(Request.Method.POST, context, url, params, httpListener);
    }

    public static void postWithFile(Context context, String url, Map<String, String> params, Map<String, DataPart> fileParams, HttpListener httpListener)
    {
        requestJSONWithFile(context, url, params, fileParams, httpListener);
    }

    public static void get(Context context, String url)
    {
        get(context, url, new HashMap<String, String>(), null);
    }

    public static void get(Context context, String url, Map<String, String> params)
    {
        get(context, url, params, null);
    }

    public static void get(Context context, String url, final HttpListener httpListener)
    {
        get(context, url, new HashMap<String, String>(), httpListener);
    }

    public static void get(Context context, String url, Map<String, String> params, HttpListener httpListener)
    {
        url += "?Olis=Android";
        for (String s : params.keySet())
        {
            url += ("&" + s + "=" + params.get(s));
        }
        requestJSON(Request.Method.GET, context, url, params, httpListener);
    }

    public static void getWithParmas(Context context, String url, HttpListener httpListener)
    {
        requestJSON(Request.Method.GET, context, url, null, httpListener);
    }

    public static void requestJSON(final int method, final Context context, final String url, final Map<String, String> params, final HttpListener httpListener)
    {
        //        final UTF8_JsonObjectRequest jsonObjectRequest = new UTF8_JsonObjectRequest(method, url, new JSONObject(params), new Response.Listener<JSONObject>()
        //        {
        //            @Override
        //            public void onResponse(JSONObject response)
        //            {
        //                httpToolOnSuccessResponse(context, getSuccessLog(parseMethod(method) + " " + url, params, response.toString()), httpListener, response);
        //            }
        //        }, getErrorListener(context, httpListener, parseMethod(method) + " " + url, params));
        //
        //        jsonObjectRequest.setShouldCache(false);
        //        VolleySingleton.getInstance(context)
        //                       .addToRequestQueue(jsonObjectRequest);

        final StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                httpToolOnSuccessResponse(context, getSuccessLog(parseMethod(method) + " " + url, params, response), httpListener, response);
            }
        }, getErrorListener(context, httpListener, parseMethod(method) + " " + url, params))
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                if (params != null)
                {
                    return params;
                }
                return super.getParams();
            }
        };

        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(getRetryPolicy());
        VolleySingleton.getInstance(context)
                       .addToRequestQueue(stringRequest);
    }

    public static void requestJSONWithFile(final Context context, final String url, final Map<String, String> params, final Map<String, DataPart> fileParams, final HttpListener httpListener)
    {
        final VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response)
            {
                final String responseStr = new String(response.data);
                httpToolOnSuccessResponse(context, getSuccessLog("POST " + url, params, responseStr), httpListener, responseStr);
            }
        }, getErrorListener(context, httpListener, "POST " + url, params))
        {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData()
            {
                return fileParams;
            }
        };

        multipartRequest.setShouldCache(false);
        multipartRequest.setRetryPolicy(getRetryPolicy());
        VolleySingleton.getInstance(context)
                       .addToRequestQueue(multipartRequest);
    }

    public static DataPart getDataPart(File file, String mimeType) throws IOException
    {
        return new DataPart(file.getAbsolutePath(), FileUtils.readFileToByteArray(file), mimeType);
    }

    private static String getSuccessLog(String url, Map<String, String> params, String response)
    {
        return "Url      : " + url + "\nParams   : " + parseParams(params) + "\nResponse : " + response;
    }

    private static void httpToolOnSuccessResponse(Context context, String log, HttpListener httpListener, String response)
    {
        if (response != null)
        {
            if (context == null || (context instanceof Activity && !((Activity) context).isFinishing()))
            {
                if (httpListener != null)
                {
                    httpListener.onSuccess(response, log);

                    try
                    {
                        httpListener.onSuccess(new JSONObject(response), log);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    httpListener.onFinished();
                }

                if (mStaticHttpListenerAdapter != null)
                {
                    mStaticHttpListenerAdapter.onSuccess(response, log);

                    try
                    {
                        mStaticHttpListenerAdapter.onSuccess(new JSONObject(response), log);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    mStaticHttpListenerAdapter.onFinished();
                }
            }
        }
    }

    private static Response.ErrorListener getErrorListener(final Context context, final HttpListener httpListener, final String url, final Map<String, String> params)
    {
        return new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                httpToolOnErrorResponse(error, context, httpListener, url, params);
            }
        };
    }

    private static void httpToolOnErrorResponse(VolleyError error, Context context, HttpListener httpListener, String url, Map<String, String> params)
    {
        if (context == null || (context instanceof Activity && !((Activity) context).isFinishing()))
        {
            final int statusCode = error != null && error.networkResponse != null ? error.networkResponse.statusCode : -1;
            final String message = error != null ? error.getMessage() : "";
            final String body = error != null && error.networkResponse != null && error.networkResponse.data != null ? new String(error.networkResponse.data) : "";
            String log = "Status Code   : " + statusCode;
            log += "\nUrl           : " + url;
            log += "\nParams        : " + parseParams(params);
            log += "\nError message : " + message;
            log += "\nError body    : " + body;

            if (httpListener != null)
            {
                httpListener.onFailure(statusCode, body, log);
                httpListener.onFinished();
            }

            if (mStaticHttpListenerAdapter != null)
            {
                mStaticHttpListenerAdapter.onFailure(statusCode, body, log);
                mStaticHttpListenerAdapter.onFinished();
            }
        }
    }

    private static String parseMethod(int method)
    {
        switch (method)
        {
            default:
            case Request.Method.GET:
                return "GET";
            case Request.Method.POST:
                return "POST";
            case Request.Method.PUT:
                return "PUT";
            case Request.Method.PATCH:
                return "PATCH";
            case Request.Method.DELETE:
                return "DELETE";
        }
    }

    private static String parseParams(Map<String, String> params)
    {
        return params != null ? params.toString() : "";
    }


    private static DefaultRetryPolicy retryPolicy       = new DefaultRetryPolicy();
    private static int                initialTimeoutMs  = 10000;
    private static int                maxNumRetries     = 1;
    private static float              backoffMultiplier = DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;

    public static void setRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier)
    {
        HttpTool.initialTimeoutMs = initialTimeoutMs;
        HttpTool.maxNumRetries = maxNumRetries;
        HttpTool.backoffMultiplier = backoffMultiplier;

        retryPolicy = new DefaultRetryPolicy(initialTimeoutMs, maxNumRetries, backoffMultiplier);
    }

    private static DefaultRetryPolicy getRetryPolicy()
    {
        if (retryPolicy == null)
        {
            retryPolicy = new DefaultRetryPolicy(initialTimeoutMs, maxNumRetries, backoffMultiplier);
        }
        return retryPolicy;
    }

}

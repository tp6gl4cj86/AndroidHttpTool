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
import java.lang.ref.WeakReference;
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

    public static void post(Context context, String url, Map<String, String> header, Map<String, String> params, HttpListener httpListener)
    {
        requestJSON(Request.Method.POST, context, url, header, params, httpListener);
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
        requestJSON(method, context, url, new HashMap<String, String>(), params, httpListener);
    }

    private static void requestJSON(final int method, Context context, final String url, final Map<String, String> header, final Map<String, String> params, final HttpListener httpListener)
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

        final WeakReference<Context> mWeakContext = new WeakReference<>(context);

        final StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                httpToolOnSuccessResponse(mWeakContext, getSuccessLog(parseMethod(method) + " " + url, params, response), httpListener, response);
            }
        }, getErrorListener(mWeakContext, httpListener, parseMethod(method) + " " + url, params))
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                if (header != null)
                {
                    return header;
                }
                else
                {
                    return super.getHeaders();
                }
            }
        };

        if (mStaticHttpListenerAdapter != null)
        {
            mStaticHttpListenerAdapter.onStart();
        }
        if (httpListener != null)
        {
            httpListener.onStart();
        }

        stringRequest.setShouldCache(false);
        stringRequest.setRetryPolicy(getRetryPolicy());
        stringRequest.setTag(getTagFromUrl(url));
        VolleySingleton.getInstance(context)
                       .addToRequestQueue(stringRequest);
    }

    public static void requestJSONWithFile(final Context context, final String url, final Map<String, String> params, final Map<String, DataPart> fileParams, final HttpListener httpListener)
    {
        final WeakReference<Context> mWeakContext = new WeakReference<>(context);

        final VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response)
            {
                final String responseStr = new String(response.data);
                httpToolOnSuccessResponse(mWeakContext, getSuccessLog("POST " + url, params, responseStr), httpListener, responseStr);
            }
        }, getErrorListener(mWeakContext, httpListener, "POST " + url, params))
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

        if (mStaticHttpListenerAdapter != null)
        {
            mStaticHttpListenerAdapter.onStart();
        }
        if (httpListener != null)
        {
            httpListener.onStart();
        }

        multipartRequest.setShouldCache(false);
        multipartRequest.setRetryPolicy(getRetryPolicy());
        multipartRequest.setTag(getTagFromUrl(url));
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

    private static void httpToolOnSuccessResponse(WeakReference<Context> mWeakContext, String log, HttpListener httpListener, String response)
    {
        if (response != null)
        {
            if (mWeakContext.get() == null || (mWeakContext.get() instanceof Activity && !((Activity) mWeakContext.get()).isFinishing()))
            {
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
            }
        }
    }

    private static Response.ErrorListener getErrorListener(final WeakReference<Context> mWeakContext, final HttpListener httpListener, final String url, final Map<String, String> params)
    {
        return new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                httpToolOnErrorResponse(error, mWeakContext, httpListener, url, params);
            }
        };
    }

    private static void httpToolOnErrorResponse(VolleyError error, final WeakReference<Context> mWeakContext, HttpListener httpListener, String url, Map<String, String> params)
    {
        if (mWeakContext.get() == null || (mWeakContext.get() instanceof Activity && !((Activity) mWeakContext.get()).isFinishing()))
        {
            final int statusCode = error != null && error.networkResponse != null ? error.networkResponse.statusCode : -1;
            final String message = error != null ? error.getMessage() : "";
            final String body = error != null && error.networkResponse != null && error.networkResponse.data != null ? new String(error.networkResponse.data) : "";
            String log = "Status Code   : " + statusCode;
            log += "\nUrl           : " + url;
            log += "\nParams        : " + parseParams(params);
            log += "\nError message : " + message;
            log += "\nError body    : " + body;

            if (mStaticHttpListenerAdapter != null)
            {
                mStaticHttpListenerAdapter.onFailure(statusCode, body, log);
                mStaticHttpListenerAdapter.onFinished();
            }

            if (httpListener != null)
            {
                httpListener.onFailure(statusCode, body, log);
                httpListener.onFinished();
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
    private static int                maxNumRetries     = 0;
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

    public static String getTagFromUrl(String url)
    {
        if (url.contains("?"))
        {
            return url.substring(0, url.indexOf("?"));
        }
        else
        {
            return url;
        }
    }

    public static void cancel(Context context, String tag)
    {
        VolleySingleton.getInstance(context)
                       .getRequestQueue()
                       .cancelAll(tag);
    }

}

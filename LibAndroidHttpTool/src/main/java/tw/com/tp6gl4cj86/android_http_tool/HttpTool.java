package tw.com.tp6gl4cj86.android_http_tool;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
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
    private static String              userAgent = "";

    public static void initUserAgent(Context context, String appName)
    {
        HttpTool.userAgent = getUserAgent(context, appName);
    }

    public static void setUserAgent(String userAgent)
    {
        HttpTool.userAgent = userAgent;
    }

    public static void setStaticHttpListenerAdapter(HttpListenerAdapter mStaticHttpListenerAdapter)
    {
        HttpTool.mStaticHttpListenerAdapter = mStaticHttpListenerAdapter;
    }

    public static void post(Context context, String url)
    {
        post(context, url, new HashMap<>(), null);
    }

    public static void post(Context context, String url, Object params)
    {
        post(context, url, params, null);
    }

    public static void post(Context context, String url, final HttpListener httpListener)
    {
        post(context, url, new HashMap<>(), httpListener);
    }

    public static void post(Context context, String url, Object params, HttpListener httpListener)
    {
        requestJSON(Request.Method.POST, context, url, params, httpListener);
    }

    public static void post(Context context, String url, Map<String, String> header, Object params, HttpListener httpListener)
    {
        requestJSON(Request.Method.POST, context, url, header, params, httpListener);
    }

    public static void postWithFile(Context context, String url, Map<String, String> params, Map<String, DataPart> fileParams, HttpListener httpListener)
    {
        requestJSONWithFile(context, url, params, fileParams, httpListener);
    }

    public static void get(Context context, String url)
    {
        get(context, url, new HashMap<>(), null);
    }

    public static void get(Context context, String url, Map<String, String> params)
    {
        get(context, url, params, null);
    }

    public static void get(Context context, String url, final HttpListener httpListener)
    {
        get(context, url, new HashMap<>(), httpListener);
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

    public static void requestJSON(final int method, final Context context, final String url, final Object params, final HttpListener httpListener)
    {
        requestJSON(method, context, url, new HashMap<>(), params, httpListener);
    }

    public static void requestJSON(final int method, Context context, final String url, final Map<String, String> header, Object params, final HttpListener httpListener)
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

        final Request request;
        if (params instanceof JSONObject)
        {
            final JSONObject paramsJson = ((JSONObject) params);
            final Map<String, String> apiParams = new HashMap<>();
            final Iterator<String> keys = paramsJson.keys();
            while (keys.hasNext())
            {
                final String key = keys.next();
                apiParams.put(key, ((JSONObject) params).optString(key));
            }

            request = new JsonObjectRequest(method, url, paramsJson, response -> httpToolOnSuccessResponse(mWeakContext, getSuccessLog(parseMethod(method) + " " + url, apiParams, response.toString()), httpListener, response.toString()), getErrorListener(mWeakContext, httpListener, parseMethod(method) + " " + url, apiParams))
            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    if (!userAgent.isEmpty())
                    {
                        if (header != null)
                        {
                            header.put("User-Agent", userAgent);
                        }
                        else if (super.getHeaders() != null)
                        {
                            super.getHeaders()
                                 .put("User-Agent", userAgent);
                        }
                    }
                    return header != null ? header : super.getHeaders();
                }
            };
        }
        else
        {
            final Map<String, String> apiParams;
            if (params instanceof Map)
            {
                apiParams = (Map<String, String>) params;
            }
            else
            {
                apiParams = new HashMap<>();
            }

            request = new StringRequest(method, url, response -> httpToolOnSuccessResponse(mWeakContext, getSuccessLog(parseMethod(method) + " " + url, apiParams, response), httpListener, response), getErrorListener(mWeakContext, httpListener, parseMethod(method) + " " + url, apiParams))
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError
                {
                    return params != null ? apiParams : super.getParams();
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    if (!userAgent.isEmpty())
                    {
                        if (header != null)
                        {
                            header.put("User-Agent", userAgent);
                        }
                        else if (super.getHeaders() != null)
                        {
                            super.getHeaders()
                                 .put("User-Agent", userAgent);
                        }
                    }
                    return header != null ? header : super.getHeaders();
                }
            };
        }

        if (mStaticHttpListenerAdapter != null)
        {
            mStaticHttpListenerAdapter.onStart();
        }
        if (httpListener != null)
        {
            httpListener.onStart();
        }

        request.setShouldCache(false);
        request.setRetryPolicy(getRetryPolicy());
        request.setTag(getTagFromUrl(url));
        VolleySingleton.getInstance(context)
                       .addToRequestQueue(request);
    }

    public static void requestJSONWithFile(final Context context, final String url, final Map<String, String> params, final Map<String, DataPart> fileParams, final HttpListener httpListener)
    {
        final WeakReference<Context> mWeakContext = new WeakReference<>(context);

        final VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, response -> {
            final String responseStr = new String(response.data);
            httpToolOnSuccessResponse(mWeakContext, getSuccessLog("POST " + url, params, responseStr), httpListener, responseStr);
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                if (!userAgent.isEmpty())
                {
                    if (super.getHeaders() != null)
                    {
                        super.getHeaders()
                             .put("User-Agent", userAgent);
                    }
                }
                return super.getHeaders();
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
        return error -> httpToolOnErrorResponse(error, mWeakContext, httpListener, url, params);
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

    private static String getUserAgent(Context context, String appName)
    {
        String appVersion = "";
        try
        {
            appVersion = context.getPackageManager()
                                .getPackageInfo(context.getPackageName(), 0).versionName; // 應用版本
        }
        catch (Exception ignore) {}

        final String osVersion = Build.VERSION.RELEASE; // 系統版本
        final int sdkVersion = Build.VERSION.SDK_INT; // SDK 版本
        final String deviceModel = Build.MODEL; // 裝置型號
        final String manufacturer = Build.MANUFACTURER; // 製造商

        return appName + " " + appVersion + " (Android " + osVersion + "; SDK " + sdkVersion + "; " + manufacturer + " " + deviceModel + ")";
    }
}

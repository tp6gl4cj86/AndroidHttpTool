package tw.com.tp6gl4cj86.android_http_tool;

import android.app.Activity;

import com.android.volley.AuthFailureError;
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

    public static void postWithFile(Activity activity, String url, Map<String, String> params, Map<String, DataPart> fileParams, HttpListener httpListener)
    {
        requestJSONWithFile(activity, url, params, fileParams, httpListener);
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
        url += "?Olis=Android";
        for (String s : params.keySet())
        {
            url += ("&" + s + "=" + params.get(s));
        }
        requestJSON(Request.Method.GET, activity, url, params, httpListener);
    }

    public static void getWithParmas(Activity activity, String url, HttpListener httpListener)
    {
        requestJSON(Request.Method.GET, activity, url, null, httpListener);
    }

    public static void requestJSON(final int method, final Activity activity, final String url, final Map<String, String> params, final HttpListener httpListener)
    {
        //        final UTF8_JsonObjectRequest jsonObjectRequest = new UTF8_JsonObjectRequest(method, url, new JSONObject(params), new Response.Listener<JSONObject>()
        //        {
        //            @Override
        //            public void onResponse(JSONObject response)
        //            {
        //                httpToolOnSuccessResponse(activity, getSuccessLog(parseMethod(method) + " " + url, params, response.toString()), httpListener, response);
        //            }
        //        }, getErrorListener(activity, httpListener, parseMethod(method) + " " + url, params));
        //
        //        jsonObjectRequest.setShouldCache(false);
        //        VolleySingleton.getInstance(activity)
        //                       .addToRequestQueue(jsonObjectRequest);

        final StringRequest stringRequest = new StringRequest(method, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    httpToolOnSuccessResponse(activity, getSuccessLog(parseMethod(method) + " " + url, params, response), httpListener, new JSONObject(response));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, getErrorListener(activity, httpListener, parseMethod(method) + " " + url, params))
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
        VolleySingleton.getInstance(activity)
                       .addToRequestQueue(stringRequest);
    }

    public static void requestJSONWithFile(final Activity activity, final String url, final Map<String, String> params, final Map<String, DataPart> fileParams, final HttpListener httpListener)
    {
        final VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>()
        {
            @Override
            public void onResponse(NetworkResponse response)
            {
                try
                {
                    final JSONObject json = new JSONObject(new String(response.data));

                    httpToolOnSuccessResponse(activity, getSuccessLog("POST " + url, params, json.toString()), httpListener, json);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, getErrorListener(activity, httpListener, "POST " + url, params))
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
        VolleySingleton.getInstance(activity)
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

    private static void httpToolOnSuccessResponse(Activity activity, String log, HttpListener httpListener, JSONObject jsonResponse)
    {
        if (jsonResponse != null)
        {
            if (activity == null || !activity.isFinishing())
            {
                if (httpListener != null)
                {
                    try
                    {
                        httpListener.onSuccess(jsonResponse, log);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    httpListener.onFinished();
                }

                if (mStaticHttpListenerAdapter != null)
                {
                    try
                    {
                        mStaticHttpListenerAdapter.onSuccess(jsonResponse, log);
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

    private static Response.ErrorListener getErrorListener(final Activity activity, final HttpListener httpListener, final String url, final Map<String, String> params)
    {
        return new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                httpToolOnErrorResponse(error, activity, httpListener, url, params);
            }
        };
    }

    private static void httpToolOnErrorResponse(VolleyError error, Activity activity, HttpListener httpListener, String url, Map<String, String> params)
    {
        if (activity == null || !activity.isFinishing())
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

}

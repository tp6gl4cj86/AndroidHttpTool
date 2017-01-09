package tw.com.tp6gl4cj86.android_http_tool.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

/**
 * Created by tp6gl4cj86 on 2017/1/7.
 */

public class UTF8_JsonObjectRequest extends JsonObjectRequest
{


    public UTF8_JsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
    {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public UTF8_JsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener)
    {
        super(url, jsonRequest, listener, errorListener);
    }

    //    @Override
    //    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response)
    //    {
    //        try
    //        {
    //            return Response.success(new JSONObject(new String(response.data, "UTF-8")), HttpHeaderParser.parseCacheHeaders(response));
    //        }
    //        catch (UnsupportedEncodingException e)
    //        {
    //            return Response.error(new ParseError(e));
    //        }
    //        catch (JSONException je)
    //        {
    //            return Response.error(new ParseError(je));
    //        }
    //    }
}

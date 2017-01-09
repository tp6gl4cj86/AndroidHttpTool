package tw.com.tp6gl4cj86.android_http_tool.Request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by tp6gl4cj86 on 2017/1/7.
 */

public class UTF8_StringRequest extends StringRequest
{
    public UTF8_StringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(method, url, listener, errorListener);
    }

    public UTF8_StringRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener)
    {
        super(url, listener, errorListener);
    }

    //    @Override
    //    protected Response<String> parseNetworkResponse(NetworkResponse response)
    //    {
    //        try
    //        {
    //            return Response.success(new String(response.data, "UTF-8"), HttpHeaderParser.parseCacheHeaders(response));
    //        }
    //        catch (UnsupportedEncodingException e)
    //        {
    //            return Response.error(new ParseError(e));
    //        }
    //    }
}

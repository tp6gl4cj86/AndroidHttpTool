package tw.com.tp6gl4cj86.android_http_tool.Listener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tp6gl4cj86 on 2016/12/5.
 */
public abstract class HttpListenerAdapter implements HttpListener
{

    @Override
    public void onSuccess(JSONObject data, String log) throws JSONException
    {

    }

    @Override
    public void onFailure(int statusCode, String errorStr)
    {

    }

}

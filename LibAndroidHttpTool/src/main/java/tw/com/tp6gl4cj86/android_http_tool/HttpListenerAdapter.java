package tw.com.tp6gl4cj86.android_http_tool;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tp6gl4cj86 on 2016/12/5.
 */
public abstract class HttpListenerAdapter implements HttpListener
{

    @Override
    public void onSuccess(JSONObject data) throws JSONException
    {

    }

    @Override
    public void onFailure(String errorStr)
    {

    }

}

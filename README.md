AndroidHttpTool
=========================

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/tp6gl4cj86/maven/android_http_tool/images/download.svg) ](https://bintray.com/tp6gl4cj86/maven/android_http_tool/_latestVersion)

Http Request Tool use volley:1.0.0 + okhttp:3.4.2

# Usage

Add dependency.

```
dependencies {
    compile 'tw.com.tp6gl4cj86:android_http_tool:2016.12.16.1'
}
```

# Http Post func

```java
// Params
Map<String, String> params = new HashMap<>();
params.put("key", "value");
...

// Request Call Back
HttpListenerAdapter httpListener = new HttpListenerAdapter()
{
    @Override
    public void onSuccess(JSONObject data, String log) throws JSONException
    {
        super.onSuccess(data, log);
    }

    @Override
    public void onFailure(String errorStr)
    {
        super.onFailure(errorStr);
    }
};

1. HttpTool.post(activity, url);
2. HttpTool.post(activity, url, params);
3. HttpTool.post(activity, url, httpListener);
4. HttpTool.post(activity, url, params, httpListener);
```

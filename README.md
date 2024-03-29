AndroidHttpTool
=========================

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/tp6gl4cj86/maven/android_http_tool/images/download.svg) ](https://bintray.com/tp6gl4cj86/maven/android_http_tool/_latestVersion)

Http Request Tool use <a href="https://developer.android.com/training/volley/index.html">volley:1.1.1</a> + <a href="http://square.github.io/okhttp/">okhttp:4.9.0</a> + <a href="https://gist.github.com/anggadarkprince/a7c536da091f4b26bb4abf2f92926594">VolleyMultipartRequest</a>

# Usage

https://jitpack.io/#tp6gl4cj86/AndroidHttpTool

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
    public void onStart()
    {

    }

    @Override
    public void onSuccess(String data, String log)
    {
        super.onSuccess(data, log);
    }
    
    @Override
    public void onSuccess(JSONObject data, String log) throws JSONException
    {
        super.onSuccess(data, log);
    }

    @Override
    public void onFailure(int statusCode, String body, String log)
    {
        super.onFailure(errorStr);
    }
    
    @Override
    public void onFinished()
    {

    }
};

1. HttpTool.post(activity, url);
2. HttpTool.post(activity, url, params);
3. HttpTool.post(activity, url, httpListener);
4. HttpTool.post(activity, url, params, httpListener);
```

# Http Get func

```java
// Params
Map<String, String> params = new HashMap<>();
params.put("key", "value");
...

// Request Call Back
HttpListenerAdapter httpListener = new HttpListenerAdapter()
{
   ...
};

1. HttpTool.get(activity, url);
2. HttpTool.get(activity, url, params);
3. HttpTool.get(activity, url, httpListener);
4. HttpTool.get(activity, url, params, httpListener);
5. HttpTool.getWithParmas(activity, url, httpListener);
```

# Http Post func for upload file
```java
// Params、httpListener same as Http Post func

// FileParams for upload file
Map<String, DataPart> fileParams = new HashMap<>();
fileParams.put("key", HttpTool.getDataPart(new File("..."), "mimeType"));
...

HttpTool.postWithFile(activity, url, params, fileParams, httpListener);
```

# DataPart Module
```java
TYPE_IMAGE = "image/jpeg"
TYPE_THREEGPP = "video/3gpp"
```

# Custom Http Request
```java
// method
// Request.Method.GET
// Request.Method.POST 
// Request.Method.PUT
// Request.Method.PATCH
// Request.Method.DELETE

HttpTool.requestJSON(method, activity, url, params, httpListener)
```

# RetryPolicy
```java
/// default 
/// initialTimeoutMs = 10000
/// maxNumRetries = 0
/// backoffMultiplier = 1.0f
setRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier)
```

# Cancel
```java
HttpTool.cancel(context, tag)
by getTagFromUrl(url)
```

# Content-Type application/json
```java
just put params by JSONObject
```

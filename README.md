AndroidHttpTool
=========================

[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[ ![Download](https://api.bintray.com/packages/tp6gl4cj86/maven/android_http_tool/images/download.svg) ](https://bintray.com/tp6gl4cj86/maven/android_http_tool/_latestVersion)

Http Request Tool use volley:1.0.0 + <a href="http://square.github.io/okhttp/">okhttp:3.4.2</a> + <a href="https://gist.github.com/anggadarkprince/a7c536da091f4b26bb4abf2f92926594">VolleyMultipartRequest</a>

# Usage

Add dependency.

```
dependencies {
    compile 'tw.com.tp6gl4cj86:android_http_tool:2017.01.08.1'
}
```

# Http Post func

```java
// Params
Map<String, String> params = new HashMap<>();
params.put("key", "value");
...

// Request Call Back
// default response JSONObject
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

# Http Get func

```java
// Params
Map<String, String> params = new HashMap<>();
params.put("key", "value");
...

// Request Call Back
// default response String
HttpListenerAdapter httpListener = new HttpListenerAdapter()
{
    @Override
    public void onSuccess(String data, String log)
    {
        super.onSuccess(data, log);
    }

    @Override
    public void onFailure(String errorStr)
    {
        super.onFailure(errorStr);
    }
};

1. HttpTool.get(activity, url);
2. HttpTool.get(activity, url, params);
3. HttpTool.get(activity, url, httpListener);
4. HttpTool.get(activity, url, params, httpListener);
```

# Http Post func for upload file
```java
// Params、httpListener same as Http Post func

// FileParams for upload file
Map<String, DataPart> fileParams = new HashMap<>();
fileParams.put("key", HttpTool.getDataPart(new File("..."), "mimeType"));
...

HttpTool.post(activity, url, params, fileParams, httpListener);
```

# DataPart Module
```java
TYPE_IMAGE = "image/jpeg"
TYPE_THREEGPP = "video/3gpp"
```

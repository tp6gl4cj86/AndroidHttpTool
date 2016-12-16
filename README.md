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

# Http Post

Map<String, String> params<br/>
HttpListener httpListener

```java
HttpTool.post(activity, Url)
HttpTool.post(activity, Url, params)
HttpTool.post(activity, Url, httpListener)
HttpTool.post(activity, Url, params, httpListener)
```

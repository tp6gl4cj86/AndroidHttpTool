Change Log
==========

Version 3.1.0
----------------------------
* Fix - Android 9.0 Crash

Version 3.0.5
----------------------------
* Add - custom header

Version 3.0.4
----------------------------
* Tune - Set tag to all http request (`getTagFromUrl()`)
* Add - Add `cancel` func to cancel http request by tag (`getTagFromUrl()`)

Version 3.0.3
----------------------------
* Add - Listener onStart func

Version 3.0.2
----------------------------
* Tune - WeakReference Context avoid Memory Leak

Version 3.0.1
----------------------------
* Fix - Default Retry

Version 3.0.0
----------------------------
* Update Gradle、Lib Version

Version 1.0.14
----------------------------
* Fix - Default initialTimeoutMs Value

Version 1.0.13
----------------------------
* Fix - Default RetryPolicy Value

Version 1.0.12
----------------------------
* Add - Listener onSuccess(String data, String log);
* Add - setRetryPolicy for custom timeout

Version 1.0.11
----------------------------
* Fix - Change Activity to Context

Version 1.0.10
----------------------------
* Add - HttpListener.onFinished()

Version 1.0.9
----------------------------
* Add - `GET` with params (without auto add ?Olis=Android)

Version 1.0.8
----------------------------
* Fix - some issue

Version 1.0.6
----------------------------
* Fix - `Post` Params issue

Version 1.0.5
----------------------------
* Fix - StaticHttpListenerAdapter onSuccess

Version 1.0.4
----------------------------
* New - Error Message return `Response Body`

Version 1.0.3
----------------------------
* New - Error Message return `Status Code`

Version 1.0.2
----------------------------
* Tune - Request without activity(null)

Version 1.0.1
----------------------------
* Fix - `Get` Params issue
* Tune - Error Message more detail
* Remove - Request for String func

Version 1.0.0
----------------------------
* Fix - `Post` Params issue
* Update - `okhttp to 3.5.0`

Version 2017.01.10.1
----------------------------
* Fix - `Get` Params issue

Version 2017.01.09.1
----------------------------
 * Fix - Error Message null issue

Version 2017.01.08.1
----------------------------
 * Fix - Request params by `UTF-8`

Version 2016.12.19.1
----------------------------
 * New - Post func for upload file
 * New - `HttpTool.getDataPart(...)` for easy to create fileParams
 * New - `DataPart.TYPE_IMAGE、TYPE_THREEGPP`
 * module some func
 
Version 2016.12.16.3
----------------------------

 * Http Post、Get func

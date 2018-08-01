# facebook_cleartext
Sample repo to demonstrate a problem with Facebook SDK

This very simple app exists to demonstrate a problem with the Facebook SDK and Android's StrictMode. It ends up meaning that an app with StrictMode enabled can't work with this SDK. This *only* seems to occur if you don't configure the app ID in the manifest, which makes sense - if you *do* provide an ID directly, the initial API call is made when the content provider installs, which is before Application.onCreate runs, and therefore before StrictMode is enabled. But for some apps, like ours, we need the ability to use a dynamic ID, since we have multiple backends our app can speak to.

To reproduce the problem, it's easiest to see this on Android P (API 28). I can't produce this 100% without a debugger, but the triggering condition is for the app events to flush. The easiest way I've found to produce the error 100% is the following:
* Place a breakpoint in MainApplication at line 40
* Place another breakpoint in AppEventQueue at line 182 (the inner `request.executeAndWait()` call)

When the app runs, the first breakpoint in MainApplication will be hit. When the second breakpoint is hit, StrictMode will be enabled - and the call will crash.

In real practice, I believe this happens if flushes are triggered at key moments. I suspect this happens for us because we have a splash screen that invokes after a delay. But getting the timing exactly right without a debugger is tricky.


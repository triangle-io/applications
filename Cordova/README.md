Cordova Sample Application
==========================

This sample application uses the Triangle Cordova Plugin found at https://github.com/triangle-io/plugins/tree/master/Cordova

The only supported platform is currently Android. When the application is loaded, the main screen will display a "Scanner is Ready" sign similar to the HelloCordova example provided by the core Cordova framework.

Once the "Scanner is Ready" item is shown, any contactless card can be scanned by tapping it to the device's NFC sensor. Once the information is read, an alert is displayed with some basic card information.

The bulk of the code resides in (index.js)[www/js/index.js].

To run the application, execute ```cordova run``` from the command prompt.

In order to use this plugin in your own application, you need to define your own application keys at http://www.triangle.io and use your access code, application ID, and secret code in index.js.

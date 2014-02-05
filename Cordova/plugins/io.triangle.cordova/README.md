Triangle Cordova Plugin
=======================

This plugin exposes functionality of the Triangle APIs (http://www.triangle.io) through a Cordova plugin. The only supported platform at this point is Android.

Prerequisites
-------------

- Cordova CLI (http://cordova.apache.org/docs/en/3.0.0/guide_cli_index.md.html)
- Android SDK (https://developer.android.com/sdk/installing/index.html)
- Ant
- Java

Steps to Use Plug-in
--------------------

1. ```cordova create testapp```
1. ```cd testapp```
1. ```cordova platform add android```
1. ```cordova plugins add https://github.com/triangle-io/plugins.git#:/Cordova```

Now you have added the plugin to a newly created project. If you already have your own project defined, you can of course only execute the last step above.

To learn about specific classes in the plugin, see [doc/index](doc/index.md)

This plugin exposes its functionality through navigator.triangle.

To use the API, first create your application keys at http://triangle.io. Once you obtain your keys, use the initialize method passing the application ID, access key and secret keys that you obtain:

```javascript
    /**
     * Initializes the Triangle session and exchanges private cryptographic keys with the Triangle servers.
     * @param applicationId ID of the application as defined in your account dashboard at http://www.triangle.io
     * @param accessKey Your account's access key as defined in your account dashboard at http://www.triangle.io
     * @param secretKey Your account's secret key as defined in your account dashboard at http://www.triangle.io
     * @param successCallback A success function will be called if initialization succeeds.
     * @param errorCallback An error callback which will be called if an issue arises during initialization.
     */
    initialize: function(applicationId, accessKey, secretKey, successCallback, errorCallback)
    { 
        /*...*/
    }
```

After initialization, 3 events will be raised on the document object:

- ontapsuccess: When a new card is tapped and processed by the API
- ontapdetect: When a new card is being detected
- ontaperror: When an error occurs during processing of a contactless card

To subscribe to any of the events, use document.addEventListener. Be sure to call addEventListener after you call the initialize function.

To see the API used in a complete sample, check https://github.com/triangle-io/applications

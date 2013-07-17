This repository contains sample applications that leverage the Triangle API to interact with payment cards. 
## Setup instructions for your own applications ##
Triangle uses latest security standards to encrypt cardholder information and also allows the APIs only to be used within registered applications. To use the APIs in your own applications, follow these steps:

1. Sign up for your account at [http://www.triangle.io](http://www.triangle.io/Home/Register)
1. Register your application by visiting the [Keys page](http://www.triangle.io/Keys)
1. Add the .jar files to your project

For more information on setup instructions, check out [the blog post](http://www.triangle.io/Blog/Index/2).
## Initializing the Triangle APIs
Before you can utilize the classes defined in the APIs, you need to initialize the APIs. This steps is done to exchange your private keys between the Triangle server and your application so that information scanned by your application is provided in an encrypted format.

You can defer the initialization to the first time you make use of the functions, or you can do so at the application's startup as shown in TODO

To initialize the API, you need to provide your Access Key, Secret Key and the Application ID in which you are using the APIs. This information can be obtained from the [keys page](http://www.triangle.io/Keys).
## Requirements
The Triangle API runs on NFC enabled Android devices running OS version 2.3 and above. The API can of course be used in the same application running on both NFC enabled and non-NFC enabled devices. For exact set of permissions required by the APIs, please see [the blog post](http://www.triangle.io/Blog/Index/2).
## Questions?
There are a variety of ways to get a hold of us:

- E: contact@triangle.io
- P: 1-866-533-5122
- StackOverflow (please tag your questions as triangle)

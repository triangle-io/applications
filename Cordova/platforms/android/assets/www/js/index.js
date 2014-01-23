/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
var app = {
    // Application Constructor
    initialize: function() {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function() {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicity call 'app.receivedEvent(...);'
    onDeviceReady: function() {

        // When the device is ready, hide the listening element
        app.hideElement('.listening');

        // Display that the device is ready
        app.showElement('.received');

        // Initialize Triangle
        // TODO: change these values to values you obtain for your own application from triangle.io
        navigator.triangle.initialize(
            "vZbpthAY7lCMEQF", // application ID
            "JyA9Qbil4E", // access key
            "O7ZiSeoLFzUs0M7zoJl5IsKrNtNTDJaMUw6AXMCiV6NYIgxN2gMzZZVmnxvpqv7W", // secret key
            function () // success callback
            {
                // Hide that the device is ready, it's given now
                app.hideElement('.received');

                // Display the Triangle ready element
                app.showElement('.triangle-ready');

                // Subscribe to events that the Triangle APIs raise
                document.addEventListener('ontaperror', app.onTapError, false);
                document.addEventListener('ontapdetect', app.onTapDetect, false);
                document.addEventListener('ontapsuccess', app.onNewCard, false);
            },
            function (message) // error callback
            {
                console.log("there was an error initializing the Triangle APIs");
                console.error(message);
            }
        );
    },
    showElement: function (css)
    {
        var parentElement = document.getElementById('notifications');
        var element = parentElement.querySelector(css);

        element.setAttribute('style', 'display:block');
    },
    hideElement: function (css)
    {
        var parentElement = document.getElementById('notifications');
        var element = parentElement.querySelector(css);

        element.setAttribute('style', 'display:none');
    },
    onNewCard: function (card)
    {
        console.log("Scanned card successfully.");

        // Display basic card information to the user
        // various other properties such as cardholderName,
        // activationDate, expiryDate, cardPreferredName, and encryptedAccountNumber
        // may be available.
        var dataToShow = card.cardBrand;
        if (card.cardholderName != undefined)
        {
            dataToShow += "\n" + card.cardholderName;
        }
        dataToShow += "\n**** **** **** " + card.lastFourDigits;

        alert(dataToShow);
    },
    onTapDetect: function ()
    {
        console.log("Detected new tap.");
    },
    onTapError: function(error)
    {
        console.log("Error processing contactless card.");
        console.error(error);
    }
};

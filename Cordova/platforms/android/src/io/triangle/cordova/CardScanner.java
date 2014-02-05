package io.triangle.cordova;

import android.os.Parcel;
import android.content.Intent;
import android.util.Log;
import io.triangle.Session;
import io.triangle.TriangleException;
import io.triangle.reader.PaymentCard;
import io.triangle.reader.TapListener;
import io.triangle.reader.TapProcessor;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Exposes methods that enable extraction of credit card details through the Triangle APIs.
 */
public class CardScanner extends CordovaPlugin implements TapListener
{
    /**
     * Main class handling the tapping of credit card to the device.
     */
    private TapProcessor tapProcessor;

    /**
     * Identifier of JavaScript events.
     */
    private static String EVENT_TAP_ERROR = "ontaperror";
    private static String EVENT_TAP_DETECT = "ontapdetect";
    private static String EVENT_TAP_SUCCESS = "ontapsuccess";

    private static String LOG_TAG = "TrianglePlugin";

    @Override
    public void onResume(boolean multitasking)
    {
        super.onResume(multitasking);

        // Resume acceptance of taps
        if (Session.getInstance().isInitialized() && this.tapProcessor != null)
        {
            this.tapProcessor.resume();
        }
    }

    @Override
    public void onPause(boolean multitasking)
    {
        super.onPause(multitasking);

        // Stop accepting taps
        if (Session.getInstance().isInitialized() && this.tapProcessor != null)
        {
            this.tapProcessor.pause();
        }
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        if (Session.getInstance().isInitialized() && this.tapProcessor != null)
        {
            this.tapProcessor.processIntent(intent);
        }
    }

    /**
     * Initializes the underlying Triangle session.
     * @param applicationId ID of the application as defined in triangle.io
     * @param accessKey Access key of the account using the API
     * @param secretKey Secret key of the account using the API
     */
    private void initializeTriangleSession(String applicationId, String accessKey, String secretKey, CallbackContext callbackContext)
    {
        try
        {
            Session.getInstance().initialize(applicationId, accessKey, secretKey, this.cordova.getActivity().getApplication());

            // Initialize the TapProcessor class now that the Session has been initialized
            this.tapProcessor = new TapProcessor(this.cordova.getActivity());

            // Subscribe to events raised during processing of credit card taps
            this.tapProcessor.setTapListener(this);
        }
        catch (TriangleException e)
        {
            // If there were any errors initializing the Session, let the js side know via the callback
            callbackContext.error(e.getMessage());

            return;
        }

        // Start listening to taps right away
        this.tapProcessor.resume();

        // Indicate success to the .js side so that further actions can be invoked
        callbackContext.success();
    }

    /**
     * Raises an event on the JavaScript side.
     * @param eventName a String identifying the event name.
     * @param messageData the data associated with the message. May be null.
     */
    private void raiseJavaScriptEvent(String eventName, JSONObject messageData)
    {
        String statement = null;

        if (messageData != null)
        {
            statement = String.format("cordova.fireDocumentEvent('%s', %s);",
                    eventName,
                    messageData.toString());
        }
        else
        {
            statement = String.format("cordova.fireDocumentEvent('%s');", eventName);
        }

        // Surround statement with try/catch
        statement = "try {" + statement + "} catch (err) { console.log('error sending javascript from Android'); }";

        this.webView.sendJavascript(statement);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException
    {
        if (action.equalsIgnoreCase("initialize"))
        {
            final String applicationId = args.getString(0);
            final String accessKey = args.getString(1);
            final String secretKey = args.getString(2);

            // Capture call back context for delegate
            final CallbackContext callback = callbackContext;

            // Execute initialization on a thread other than the core WebView thread
            this.cordova.getThreadPool().execute(new Runnable()
            {
                @Override
                public void run()
                {
                    CardScanner.this.initializeTriangleSession(applicationId, accessKey, secretKey, callback);
                }
            });

            return true;
        }

        return super.execute(action, args, callbackContext);
    }

    @Override
    public void onTapDetect()
    {
        Log.d(LOG_TAG, "Tap detected.");

        this.raiseJavaScriptEvent(EVENT_TAP_DETECT, null);
    }

    @Override
    public void onTapError(Exception e)
    {
        Log.d(LOG_TAG, "Tap error detected.");

        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put("error", e.getMessage());
        }
        catch (JSONException e1)
        {
            // Should never occur
        }

        this.raiseJavaScriptEvent(EVENT_TAP_ERROR, jsonObject);
    }

    @Override
    public void onTapSuccess(PaymentCard paymentCard)
    {
        Log.d(LOG_TAG, "Tap successfully processed.");

        // Dump the payment card class into a JSONObject
        JSONObject jsonObject = new JSONObject();

        try
        {
            jsonObject.put("lastFourDigits", paymentCard.getLastFourDigits());
            jsonObject.put("cardholderName", paymentCard.getCardholderName());
            jsonObject.put("cardBrand", paymentCard.getCardBrand());
            jsonObject.put("activationDate", paymentCard.getActivationDate());
            jsonObject.put("expiryDate", paymentCard.getExpiryDate());
            jsonObject.put("cardPreferredName", paymentCard.getCardPreferredName());
            jsonObject.put("encryptedAccountNumber", JSONObject.quote(paymentCard.getEncryptedAccountNumber()));
        }
        catch (JSONException e)
        {
            // Should never occur as we are putting string and date data only
        }

        // Finally send the message across
        this.raiseJavaScriptEvent(EVENT_TAP_SUCCESS, jsonObject);
    }
}

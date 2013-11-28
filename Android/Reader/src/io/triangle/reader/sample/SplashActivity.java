package io.triangle.reader.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import io.triangle.Session;

/**
 * The splash screen of the application that is shown while the Triangle APIs are being initialized. Note that usage of
 * this class is fully optional. You may want to allow your user to interact with the application irrespective of the
 * Session's initialization state and check for the Session's state at the time of scan in your applications.
 */
public class SplashActivity extends Activity implements Session.OnInitializeListener
{
    private boolean isResumed;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.splash);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        this.isResumed = true;

        if (Session.getInstance().isInitialized())
        {
            this.onSessionInitialized();
        }
        else
        {
            // Subscribe to be notified when the Session is initialized
            Session.getInstance().setOnInitializeListener(this);
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        this.isResumed = false;
    }

    private void onSessionInitialized()
    {
        // If for whatever reason the activity is no longer in the foreground, don't do anything
        if (!this.isResumed)
        {
            return;
        }

        // Since the Session is initialized now, load the Scan Result activity (which will in turn start a scan
        // if no cards have been scanned yet)
        this.startActivity(new Intent(this, ScanResultActivity.class));

        // Finish the splash activity (we don't want it in the back stack)
        this.finish();
    }

    @Override
    public void onInitialized()
    {
        // This call may come from a background thread, so we perform the real work on the UI thread
        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                SplashActivity.this.onSessionInitialized();
            }
        });
    }
}
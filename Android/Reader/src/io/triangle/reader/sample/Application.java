package io.triangle.reader.sample;

import android.os.AsyncTask;
import io.triangle.Session;

public class Application extends android.app.Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();

        // Initialize the Triangle API if it has not been initialized yet
        final Session triangleSession = Session.getInstance();

        if (!triangleSession.isInitialized())
        {
            // Since the initialization performs network IO, we should execute it in a background thread
            AsyncTask<Void, Void, Void> triangleInitializationTask = new AsyncTask<Void, Void, Void>()
            {
                Exception exception;

                @Override
                protected Void doInBackground(Void... voids)
                {
                    // Please note that the keys below will only work in the sample application. Before deploying
                    // to your own environment, please use keys associated with your own developer account
                    try
                    {
                        //triangleSession.initialize(
                        //        "ypcu3F6s3UZByVm", // Application ID
                        //        "JyA9Qbil4E",      // Access Key
                        //        "O7ZiSeoLFzUs0M7zoJl5IsKrNtNTDJaMUw6AXMCiV6NYIgxN2gMzZZVmnxvpqv7W", // Secret Key
                        //        Application.this);
                        triangleSession.initialize(
                                "fynCpLrUALG5HWS", // Application ID
                                "BDCmscCLGU",      // Access Key
                                "OuAWeW2HAL58ghsOm43g0uQCElEtswyYOH7V2P589dvS0tmkUDPxIpTkDYiSiye3", // Secret Key
                                Application.this);
                    }
                    catch (Exception exception)
                    {
                        this.exception = exception;
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid)
                {
                    super.onPostExecute(aVoid);

                    if (this.exception != null)
                    {
                        // Do error handling if initialization was not successful
                    }
                }
            };

            // Finally execute the task
            triangleInitializationTask.execute();
        }
    }
}

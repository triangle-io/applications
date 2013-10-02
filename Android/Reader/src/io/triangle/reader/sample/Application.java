package io.triangle.reader.sample;

import android.os.AsyncTask;
import android.widget.Toast;
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
            // TODO: You need to obtain keys from http://www.triangle.io to be able to run the application
            final String applicationId = "TODO";
            final String accessKey = "TODO";
            final String secretKey = "TODO" ;

            // Let the developer know that they need keys in case they have not added them here
            if (applicationId.equals("TODO"))
            {
                Toast.makeText(this, "You need to obtain keys from triangle.io before running the sample application", Toast.LENGTH_LONG).show();
            }

            // Since the initialization performs network IO, we should execute it in a background thread
            AsyncTask<Void, Void, Void> triangleInitializationTask = new AsyncTask<Void, Void, Void>()
            {
                Exception exception;

                @Override
                protected Void doInBackground(Void... voids)
                {
                    try
                    {
                        triangleSession.initialize(
                                applicationId, // Application ID
                                accessKey,      // Access Key
                                secretKey, // Secret Key
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
                        // TODO: Do error handling if initialization was not successful
                        Toast.makeText(Application.this, this.exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            };

            // Finally execute the task
            triangleInitializationTask.execute();
        }
    }
}

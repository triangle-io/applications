package io.triangle.reader;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Bundle;
import android.widget.TextView;
import triangle.io.emv.EMVCardSession;
import triangle.io.emv.EMVCardTerminal;

public class MainActivity extends Activity {
    TextView textView;
    NfcAdapter nfcAdapter;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textView = (TextView)this.findViewById(R.id.textView);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.

        if (nfcAdapter != null && nfcAdapter.isEnabled())
        {
            nfcAdapter.enableForegroundDispatch(this,
                    PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0),
                    new IntentFilter[] { new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED) },
                    new String[][] { new String[] { IsoDep.class.getName() }});
        }
    }

    @Override
    protected void onPause() {
        super.onPause();    //To change body of overridden methods use File | Settings | File Templates.

        if (nfcAdapter != null)
        {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null && intent.getAction() == NfcAdapter.ACTION_TECH_DISCOVERED)
        {
            Tag tag = (Tag)intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            IsoDep isoDep = IsoDep.get(tag);

            EMVCardSession cardSession = new EMVCardSession();
            boolean success = true;
            try
            {
                isoDep.connect();
                isoDep.setTimeout(7000);
                EMVCardTerminal.getInstance().TryProcessCard(isoDep, cardSession);
                isoDep.close();
            }
            catch (Exception e)
            {
                success = false;
            }

            if (success)
            {
                byte[] track2 = cardSession.getProperty((byte)0x57);

                if (track2 != null)
                {
                    textView.setText("Succeeded! " + bytesToHex(track2));
                }
                else
                {
                    textView.setText("Success but no Track2");
                }
            }
            else
            {
                textView.setText("Failed");
            }
        }
    }

    final protected static char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}

package io.triangle.reader.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import io.triangle.reader.PaymentCard;
import io.triangle.reader.ScanActivity;

import java.util.List;

/**
 * This class demonstrates how scanning payment cards via NFC is done via the Triangle APIs.
 */
public class ScanResultActivity extends Activity implements View.OnClickListener
{
    private LinearLayout root;
    private TextView caption;

    // Buttons
    private ImageButton facebookButton;
    private ImageButton twitterButton;
    private ImageButton shareButton;
    private ImageButton linkedInButton;
    private ImageButton googlePlusButton;

    private static final int SCAN_REQUEST_CODE = 100;

    /**
     * Tracks whether this activity has already requested a card scan.
     */
    private boolean hasRequestedScan;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.scan_result);

        this.root = (LinearLayout)this.findViewById(R.id.main_LinearLayout_root);
        this.caption = (TextView)this.findViewById(R.id.header_textView_caption);

        this.facebookButton = (ImageButton)this.findViewById(R.id.main_imageButton_facebook);
        this.twitterButton = (ImageButton)this.findViewById(R.id.main_imageButton_twitter);
        this.shareButton = (ImageButton)this.findViewById(R.id.main_imageButton_share);
        this.linkedInButton = (ImageButton)this.findViewById(R.id.main_imageButton_linkedin);
        this.googlePlusButton = (ImageButton)this.findViewById(R.id.main_imageButton_googleplus);

        // Handle clicks
        this.facebookButton.setOnClickListener(this);
        this.twitterButton.setOnClickListener(this);
        this.shareButton.setOnClickListener(this);
        this.linkedInButton.setOnClickListener(this);
        this.googlePlusButton.setOnClickListener(this);

        // Link the text to our website in the caption
        Linkify.addLinks(this.caption, Linkify.WEB_URLS);
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // Ensure that the device's NFC sensor is on
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        boolean askingToEnableNfc = false;

        if (nfcAdapter != null && !nfcAdapter.isEnabled())
        {
            askingToEnableNfc = true;

            // Alert the user that NFC is off
            new AlertDialog.Builder(this)
                    .setTitle("NFC Sensor Turned Off")
                    .setMessage("In order to use this application, the NFC sensor must be turned on. Do you wish to turn it on?")
                    .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            // Send the user to the settings page and hope they turn it on
                            if (android.os.Build.VERSION.SDK_INT >= 16)
                            {
                                startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                            }
                            else
                            {
                                startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }
                    })
                    .setNegativeButton("Do Nothing", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i)
                        {
                            // Do nothing
                        }
                    })
                    .show();
        }

        if (!askingToEnableNfc && !this.hasRequestedScan)
        {
            // If no cards have been scanned so far, then automatically kick off a scan
            this.scanCard();
        }
    }

    private void onScanResult(PaymentCard cardInformation, List<String> errors)
    {
        // NOTE: The errors list would contain any errors the scanning may have yielded

        if (cardInformation != null)
        {
            // Remove any previous cards
            this.root.removeAllViews();

            // Card was successfully read, create a new card view and add it to the layout so that the user can see the
            // card information
            CardView cardView = new CardView(this.root, cardInformation, this);
            LinearLayout.LayoutParams cardViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardViewLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;
            this.root.addView(cardView, 0, cardViewLayoutParams);
        }
    }

    /**
     * Allows the user to recommend this application to a friend.
     */
    private void recommend()
    {
        Intent recommendIntent = new Intent(Intent.ACTION_SEND);

        recommendIntent.setType("text/plain");

        recommendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this cool app, it scans your credit card when you tap it to your phone.\r\nhttp://play.google.com/store/apps/details?id=io.triangle.reader.sample");
        recommendIntent.putExtra(Intent.EXTRA_SUBJECT, "App that scans your credit card");

        this.startActivity(Intent.createChooser(recommendIntent, "Share App"));
    }

    public void onScanButtonClick(final View view)
    {
        this.scanCard();
    }

    private void scanCard()
    {
        Intent scanIntent = new Intent(this, io.triangle.reader.ScanActivity.class);

        // We want the scanning to continue until a successful scan occurs or
        // the user explicitly cancels
        scanIntent.putExtra(ScanActivity.INTENT_EXTRA_RETRY_ON_ERROR, true);

        // Kick off the scan activity
        this.startActivityForResult(scanIntent, SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == SCAN_REQUEST_CODE)
        {
            // Track that this activity has already asked for a scan
            this.hasRequestedScan = true;

            if (resultCode == RESULT_OK)
            {
                PaymentCard scannedCard = data.getParcelableExtra(ScanActivity.INTENT_EXTRA_PAYMENT_CARD);
                List<String> errors = data.getStringArrayListExtra(ScanActivity.INTENT_EXTRA_SCAN_ERRORS);

                // Handle the scan result
                this.onScanResult(scannedCard, errors);
            }
            else if (resultCode == ScanActivity.RESULT_NO_NFC)
            {
                // This device does not have an NFC sensor
                new AlertDialog.Builder(this)
                        .setTitle("Device has no NFC Sensor")
                        .setMessage("In order to scan a payment card, you must have a device with an NFC sensor.")
                        .setPositiveButton("OK", null)
                        .create()
                        .show();
            }
            else if (resultCode == ScanActivity.RESULT_CANCELED)
            {
                // The scanning was cancelled by the user
            }
        }
        else
        {
            // Let the parent handle this, we don't know what it is
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View view)
    {
        // The only buttons that call this method are the share buttons
        // Perform the actual recommendation
        this.recommend();
    }
}

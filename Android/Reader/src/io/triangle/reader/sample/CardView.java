package io.triangle.reader.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import io.triangle.reader.PaymentCard;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A compound UI component which presents information stored in a {@link PaymentCard} object. {@link PaymentCard} is
 * obtained via the Triangle API and stores information of a payment card. This class simply presents the information
 * to the user in a user interface.
 */
public class CardView extends RelativeLayout
{
    private TextView cardholder;
    private TextView accountNumber;
    private TextView expiryFrom;
    private TextView expiryTo;
    private TextView brandName;
    private TextView preferredName;
    private ImageButton closeButton;

    private LinearLayout parent;

    public CardView(final LinearLayout parent, PaymentCard scannedCard, Context context, int index)
    {
        super(context);

        this.parent = parent;

        // Inflate the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview, this, true);

        // Set the background of the view based on the index of the card. We have 3 colors and
        // will choose the color to use based on % 3
        RelativeLayout root = (RelativeLayout)view.findViewById(R.id.cardview_relativeLayout_root);

        int backgroundIndex = index % 3;
        switch (backgroundIndex)
        {
            case 0:
                root.setBackgroundResource(R.drawable.border_blue);
                break;
            case 1:
                root.setBackgroundResource(R.drawable.border_orange);
                break;
            case 2:
                root.setBackgroundResource(R.drawable.border_purple);
                break;
        }

        this.accountNumber = (TextView)view.findViewById(R.id.cardview_textView_account_number);
        this.cardholder = (TextView)view.findViewById(R.id.cardview_textView_cardholder);
        this.expiryFrom = (TextView)view.findViewById(R.id.cardview_textView_expiry_from);
        this.expiryTo = (TextView)view.findViewById(R.id.cardview_textView_expiry_to);
        this.brandName = (TextView)view.findViewById(R.id.cardview_textView_brand);
        this.preferredName = (TextView)view.findViewById(R.id.cardview_textView_preferred_name);
        this.closeButton = (ImageButton)view.findViewById(R.id.cardview_imageButton_close);

        this.setValuesFromCard(scannedCard);

        // Remove this view from its parent when the close button is pressed
        this.closeButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                parent.removeView(CardView.this);
            }
        });
    }

    /**
     * Reads the information from the payment card and sets various UI components so that the end user can see
     * the values.
     * @param scannedCard object representing the data scanned from a credit card via NFC.
     */
    private void setValuesFromCard(PaymentCard scannedCard)
    {
        SimpleDateFormat shortFormat = new SimpleDateFormat("dd/MM/yy");
        Date activationDate = scannedCard.getActivationDate();
        Date expiryDate = scannedCard.getExpiryDate();
        String accountNumber = scannedCard.getLastFourDigits();
        String brandName = scannedCard.getCardBrand();
        String preferredName = scannedCard.getCardPreferredName();
        String cardholderName = scannedCard.getCardholderName();

        this.accountNumber.setText("****    ****    ****    " + accountNumber);

        // Since various card manufacturers may choose not to include some information on the payment card, we check
        // the obtained values from the card against null.
        this.expiryTo.setText(expiryDate == null ? "" : shortFormat.format(expiryDate));
        this.expiryFrom.setText(activationDate == null ? "" : shortFormat.format(activationDate));
        this.brandName.setText(brandName == null ? "" : brandName);
        this.preferredName.setText(preferredName == null ? "" : preferredName);
        this.cardholder.setText(cardholderName == null ? "" : cardholderName);
    }
}

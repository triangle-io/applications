package io.triangle.reader.sample;

import android.content.Context;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A header control which contains the Triangle logo and some general information about the product.
 */
public class Header extends RelativeLayout
{
    ImageView logo;
    ImageView device;
    TextView instructions;
    TextView caption;
    ImageButton toggle;

    /**
     * Whether the control is in maximized state.
     */
    private boolean isMaximized;

    public Header(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        // Inflate the custom layout for the Header control
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.header, this, true);

        this.logo = (ImageView)layout.findViewById(R.id.header_imageView_logo);
        this.device = (ImageView)layout.findViewById(R.id.header_imageView_device);
        this.instructions = (TextView)layout.findViewById(R.id.header_textView_instructions);
        this.caption = (TextView)layout.findViewById(R.id.header_textView_caption);
        this.toggle = (ImageButton)layout.findViewById(R.id.header_imageButton_toggle);

        // The layout starts in maximized mode
        this.isMaximized = true;

        // Link the text to our website in the caption
        Linkify.addLinks(this.caption, Linkify.WEB_URLS);

        // Toggle expansion state on button click
        this.toggle.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Header.this.toggle();
            }
        });
    }

    public void minimize()
    {
        if (this.isMaximized)
        {
            this.device.setVisibility(GONE);
            this.instructions.setVisibility(GONE);

            this.isMaximized = false;
            this.toggle.setImageResource(R.drawable.ic_navigation_expand);
        }
    }

    public void maximize()
    {
        if (!this.isMaximized)
        {
            this.device.setVisibility(VISIBLE);
            this.instructions.setVisibility(VISIBLE);

            this.isMaximized = true;
            this.toggle.setImageResource(R.drawable.ic_navigation_collapse);
        }
    }

    public void toggle()
    {
        if (this.isMaximized)
        {
            this.minimize();
        }
        else
        {
            this.maximize();
        }
    }

    public void startFading()
    {
        Animation fadeAnimation = new AlphaAnimation(1.0f, 0.0f);

        // Rotate with linear speed
        fadeAnimation.setInterpolator(new LinearInterpolator());

        // Repeat indefinitely
        fadeAnimation.setRepeatCount(Animation.INFINITE);

        // One fade should take 700ms
        fadeAnimation.setDuration(700);

        this.logo.startAnimation(fadeAnimation);
    }

    public void stopFading()
    {
        this.logo.clearAnimation();
    }
}

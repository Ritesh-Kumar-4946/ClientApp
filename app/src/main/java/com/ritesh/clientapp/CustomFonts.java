package com.ritesh.clientapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by vatsana on 1/9/17.
 */

public class CustomFonts extends TextView {

    public CustomFonts(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomFonts(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomFonts(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/longdon_decorative.ttf");
            setTypeface(tf);
        }
    }
    
    
}

package de.eickemeyer.wake.on.lan.entities;

import android.graphics.drawable.Drawable;


public class DialogContextMenuItem {
    private final String mText;
    private Drawable mDrawable;

    public DialogContextMenuItem(String text, Drawable drawable) {
        mText = text;
        mDrawable = drawable;
    }

    public String getText() {
        return mText;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

}

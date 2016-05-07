package de.eickemeyer.wake.on.lan.ui;

import android.content.Context;
import android.content.res.TypedArray;

import de.eickemeyer.wake.on.lan.R;

public class UiUtils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
}

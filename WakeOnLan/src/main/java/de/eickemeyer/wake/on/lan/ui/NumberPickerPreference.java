package de.eickemeyer.wake.on.lan.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.preference.DialogPreference;
import android.util.AttributeSet;

import de.eickemeyer.wake.on.lan.R;

public class NumberPickerPreference extends DialogPreference {

    private int mValue;
    private int mEndValue;
    private int mStartValue;

    public NumberPickerPreference(Context context) {
        super(context);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference);

        final int N = ta.getIndexCount();
        for (int i = 0; i < N; ++i) {
            switch (ta.getIndex(i)) {
                case R.styleable.NumberPickerPreference_startValue:
                    mStartValue = ta.getInt(i, 0);
                    break;
                case R.styleable.NumberPickerPreference_endValue:
                    mEndValue = ta.getInt(i, 0);
                    break;
            }
        }
        ta.recycle();
    }

    public void setValue(int value) {
        mValue = value;
        final boolean wasBlocking = shouldDisableDependents();
        persistInt(value);
        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        setValue(restoreValue ? getPersistedInt(mValue) : (int) defaultValue);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());
        setValue(myState.mValue);
    }

    public int getValue() {
        return mValue;
    }

    public int getEndValue() {
        return mEndValue;
    }

    public int getStartValue() {
        return mStartValue;
    }

    private static class SavedState extends BaseSavedState {

        int mValue;

        public SavedState(Parcel source) {
            super(source);
            mValue = source.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mValue);
        }

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }

}

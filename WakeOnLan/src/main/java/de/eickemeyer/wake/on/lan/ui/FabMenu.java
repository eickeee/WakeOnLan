package de.eickemeyer.wake.on.lan.ui;

import android.animation.Animator;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.fragments.FavoritesFragment;

public class FabMenu {

    private AccelerateInterpolator mAccelerateInterpolator;
    private FloatingActionButton mFabAdd;
    private FloatingActionButton mFabClear;
    private boolean mIsMenuOpen = false;
    private FabMenuListener mFabMenuListener;

    public void setFavoritesFragment(FabMenuListener fabMenuListener) {
        this.mFabMenuListener = fabMenuListener;
    }

    private final Activity mActivity;
    private final FloatingActionButton mFabMenu;

    public static FabMenu createFabMenu(FavoritesFragment favoritesFragment) {
        return new FabMenu(favoritesFragment);
    }

    private FabMenu(FavoritesFragment favoritesFragment) {
        mFabMenuListener = favoritesFragment;
        mActivity = favoritesFragment.getActivity();

        mFabMenu = (FloatingActionButton) mActivity.findViewById(R.id.fab);

        mFabMenu.setVisibility(View.VISIBLE);
        mFabMenu.setImageResource(R.drawable.fab_add_grey);
        mFabMenu.setRippleColor(ContextCompat.getColor(mActivity, R.color.white_pressed));
        mFabMenu.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(mActivity, R.color.white)));

        mFabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAccelerateInterpolator = new AccelerateInterpolator(2f);
                if (mFabClear == null || mFabAdd == null)
                    setupFabMenu();

                if (!mIsMenuOpen)
                    openMenu();
                else
                    closeMenu();
            }

        });
    }

    private void setupFabMenu() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) mActivity.findViewById(R.id.coordinatorLayout);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.equals(mFabAdd)) {
                    closeMenu();
                    mFabMenuListener.addFavorite();
                } else if (v.equals(mFabClear)) {
                    closeMenu();
                    mFabMenuListener.clearFavorites();
                }
            }
        };

        mActivity.getLayoutInflater().inflate(R.layout.fab_add, coordinatorLayout);
        mFabAdd = (FloatingActionButton) mActivity.findViewById(R.id.fabAdd);
        mFabAdd.setAlpha(0f);
        mFabAdd.setOnClickListener(onClickListener);

        mActivity.getLayoutInflater().inflate(R.layout.fab_clear, coordinatorLayout);
        mFabClear = (FloatingActionButton) mActivity.findViewById(R.id.fabClear);
        mFabClear.setAlpha(0f);
        mFabClear.setOnClickListener(onClickListener);
    }

    private void openMenu() {
        final int fabSize = (int) mActivity.getResources().getDimension(R.dimen.fab_size_normal);
        final float fabClearTranslationY = (float) (-(fabSize * 1.2));
        final float fabAddTranslationY = (float) (-(fabSize * 2 * 1.2));
        final float alpha = 1.0f;
        final float rotation = 90f + 45f;

        setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= 14) {
            mFabMenu.animate().rotation(rotation).start();
            mFabClear.animate().setInterpolator(mAccelerateInterpolator).translationY(fabClearTranslationY).alpha(alpha).setListener(null).start();
            mFabAdd.animate().setInterpolator(mAccelerateInterpolator).translationY(fabAddTranslationY).alpha(alpha).setListener(null).start();
        } else {
            mFabClear.setAlpha(alpha);
            mFabAdd.setAlpha(alpha);
            mFabClear.setTranslationY(fabClearTranslationY);
            mFabAdd.setTranslationY(fabAddTranslationY);
            mFabMenu.setRotation(rotation);
        }

        mIsMenuOpen = true;
    }

    public void closeMenu() {
        final float fabClearTranslationY = 0f;
        final float fabAddTranslationY = 0f;
        final float alpha = 0.0f;
        final float rotation = 0f;

        //in case closeMenu gets invoked but the menu has not been initialized yet
        if (mFabClear != null && mFabAdd != null) {
            Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mFabClear.setVisibility(View.GONE);
                    mFabAdd.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            };
            if (Build.VERSION.SDK_INT >= 14) {
                mFabMenu.animate().rotation(0).start();
                mFabClear.animate().setInterpolator(mAccelerateInterpolator).translationY(0).alpha(0.0f).setListener(animatorListener).start();
                mFabAdd.animate().setInterpolator(mAccelerateInterpolator).translationY(0).alpha(0.0f).setListener(animatorListener).start();
                mIsMenuOpen = false;
            } else {
                mFabClear.setAlpha(alpha);
                mFabAdd.setAlpha(alpha);
                mFabClear.setTranslationY(fabClearTranslationY);
                mFabAdd.setTranslationY(fabAddTranslationY);
                mFabMenu.setRotation(rotation);
            }
        }
    }

    public void setOnClickListener(View.OnClickListener listener) {
        mFabMenu.setOnClickListener(listener);
    }

    public void setVisibility(int visibility) {
        if (mFabMenu != null) mFabMenu.setVisibility(visibility);
        if (mFabAdd != null) mFabAdd.setVisibility(visibility);
        if (mFabClear != null) mFabClear.setVisibility(visibility);
    }
}

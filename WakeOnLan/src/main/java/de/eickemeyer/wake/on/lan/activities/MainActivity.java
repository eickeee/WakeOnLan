package de.eickemeyer.wake.on.lan.activities;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.eickemeyer.wake.on.lan.R;
import de.eickemeyer.wake.on.lan.database.DatabaseManager;
import de.eickemeyer.wake.on.lan.fragments.AboutFragment;
import de.eickemeyer.wake.on.lan.fragments.FavoritesFragment;
import de.eickemeyer.wake.on.lan.fragments.PreferenceFragment;
import de.eickemeyer.wake.on.lan.fragments.ScanFragment;
import de.eickemeyer.wake.on.lan.fragments.WakeFragment;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "WakeOnLan";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.appBarLayout)
    AppBarLayout mAppBarLayout;

    DrawerLayout mDrawerLayout;
    private String mToolbarTitle;
    private boolean mIsDrawerUnlocked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);


        //differentiate between normal drawer layout and "sw600-land-drawer-mode" where a RelativeLayout
        //is used to have the navigation view always open and next to the fragment container
        mIsDrawerUnlocked = !getResources().getBoolean(R.bool.drawer_layout_locked);
        if (mIsDrawerUnlocked) {
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

            if (mDrawerLayout != null) mDrawerLayout.addDrawerListener(toggle);
            toggle.syncState();
        } else {
            //change status bar color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(ContextCompat.getColor(this, R.color.lockedDrawerStatusBar));
            }
        }
        mNavigationView.setNavigationItemSelectedListener(this);

        //select default fragment on first start
        if (savedInstanceState == null) {
            final MenuItem firstMenuItem = mNavigationView.getMenu().findItem(R.id.nav_wakeUp);
            onNavigationItemSelected(firstMenuItem);
            firstMenuItem.setChecked(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (mIsDrawerUnlocked) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseManager.getInstance().closeDatabase();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        final FragmentManager supportFragmentManager = getSupportFragmentManager();
        final Resources resources = getResources();
        Fragment selectedFragment = null;
        String selectedFragmentTag = "";

        switch (menuItem.getItemId()) {
            case R.id.nav_wakeUp:
                selectedFragment = supportFragmentManager.findFragmentByTag(WakeFragment.TAG);

                if (selectedFragment == null)
                    selectedFragment = new WakeFragment();

                selectedFragmentTag = WakeFragment.TAG;
                mToolbarTitle = resources.getString(R.string.app_name) + " - Wake";
                break;
            case R.id.nav_scan:
                selectedFragment = supportFragmentManager.findFragmentByTag(ScanFragment.TAG);

                if (selectedFragment == null)
                    selectedFragment = new ScanFragment();

                selectedFragmentTag = ScanFragment.TAG;
                mToolbarTitle = resources.getString(R.string.app_name) + " - Scan";
                break;
            case R.id.nav_favorites:
                selectedFragment = supportFragmentManager.findFragmentByTag(FavoritesFragment.TAG);

                if (selectedFragment == null)
                    selectedFragment = new FavoritesFragment();

                selectedFragmentTag = FavoritesFragment.TAG;
                mToolbarTitle = resources.getString(R.string.app_name) + " - " + resources.getString(R.string.menu_favorites);
                break;
            case R.id.nav_settings:
                selectedFragment = supportFragmentManager.findFragmentByTag(PreferenceFragment.TAG);

                if (selectedFragment == null)
                    selectedFragment = new PreferenceFragment();

                selectedFragmentTag = PreferenceFragment.TAG;
                mToolbarTitle = resources.getString(R.string.app_name) + " - " + resources.getString(R.string.menu_settings);
                break;
            case R.id.nav_about:
                selectedFragment = supportFragmentManager.findFragmentByTag(AboutFragment.TAG);

                if (selectedFragment == null)
                    selectedFragment = new AboutFragment();

                selectedFragmentTag = AboutFragment.TAG;
                mToolbarTitle = resources.getString(R.string.app_name) + " - " + resources.getString(R.string.menu_about);
                break;
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, selectedFragment, selectedFragmentTag)
                .commit();

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(mToolbarTitle);

        if (mIsDrawerUnlocked)
            mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public AppBarLayout getAppBarLayout() {
        return mAppBarLayout;
    }

}

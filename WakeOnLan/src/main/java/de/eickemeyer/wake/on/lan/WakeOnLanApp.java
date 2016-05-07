package de.eickemeyer.wake.on.lan;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static android.os.Build.VERSION.SDK_INT;
import static android.os.Build.VERSION_CODES.GINGERBREAD;

public class WakeOnLanApp extends Application {

    private static WakeOnLanApp wakeOnLanApplication;
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        WakeOnLanApp application = (WakeOnLanApp) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wakeOnLanApplication = this;
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        enabledStrictMode();
        refWatcher = LeakCanary.install(this);
    }


    private void enabledStrictMode() {
        if (SDK_INT >= GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build());
        }
    }

    public static WakeOnLanApp getWakeOnLanApp() {
        return wakeOnLanApplication;
    }
}

package de.eickemeyer.wake.on.lan.database;

import java.util.ArrayList;

import de.eickemeyer.wake.on.lan.entities.ScanResult;
import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseManager {


    private static Realm mRealm;

    private static DatabaseManager instance;

    private DatabaseManager() {
    }

    public static DatabaseManager getInstance() {
        if (instance == null)
            instance = new DatabaseManager();
        configureRealmIfNotReady();
        return instance;
    }

    private static void configureRealmIfNotReady() {
        if (mRealm == null)
            mRealm = Realm.getDefaultInstance();
        if (mRealm.isClosed())
            mRealm = Realm.getDefaultInstance();
    }

    public void closeDatabase() {
        if (mRealm != null)
            mRealm.close();
    }

    public ArrayList<ScanResult> getAllFavorites() {
        RealmResults<ScanResult> all = mRealm.where(ScanResult.class).findAll();
        return new ArrayList<>(all);
    }

    public void storeFavorite(ScanResult scanResult) {
        mRealm.beginTransaction();
        mRealm.copyToRealm(scanResult);
        mRealm.commitTransaction();
    }

    public void deleteFavorite(ScanResult scanResult) {
        mRealm.beginTransaction();
        mRealm.where(ScanResult.class)
                .equalTo("favName", scanResult.favName)
                .equalTo("mac", scanResult.mac)
                .equalTo("hostname", scanResult.hostname)
                .equalTo("broadcast", scanResult.broadcast)
                .equalTo("icon", scanResult.icon)
                .findAll().deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    public void deleteAllFavorites() {
        mRealm.beginTransaction();
        mRealm.where(ScanResult.class).findAll().deleteAllFromRealm();
        mRealm.commitTransaction();
    }

    public boolean isFavoriteNameAlreadyInUse(String favoriteName) {
        return mRealm.where(ScanResult.class).equalTo("favName", favoriteName).findAll().size() > 0;
    }
}

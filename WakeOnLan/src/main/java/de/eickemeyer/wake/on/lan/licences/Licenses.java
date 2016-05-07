package de.eickemeyer.wake.on.lan.licences;

import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;

public class Licenses extends Notices {

    public Licenses() {
        addNotice(new Notice("LicensesDialog", "https://github.com/PSDev/LicensesDialog", "Philip Schiffer", new ApacheSoftwareLicense20()));
        addNotice(new Notice("ButterKnife", "https://github.com/JakeWharton/butterknife", "Jake Wharton", new ApacheSoftwareLicense20()));
        addNotice(new Notice("LeakCanary", "https://github.com/square/leakcanary", "Square", new ApacheSoftwareLicense20()));

        addNotice(new Notice("Android Support v4", "https://source.android.com/", "Android Open Source Project", new ApacheSoftwareLicense20()));
        addNotice(new Notice("Android Support v7", "https://source.android.com/", "Android Open Source Project", new ApacheSoftwareLicense20()));
        addNotice(new Notice("Android Support RecyclerView v7", "https://source.android.com/", "Android Open Source Project", new ApacheSoftwareLicense20()));
        addNotice(new Notice("Android Support CardView v7", "https://source.android.com/", "Android Open Source Project", new ApacheSoftwareLicense20()));
        addNotice(new Notice("Android Support Preferences v7", "https://source.android.com/", "Android Open Source Project", new ApacheSoftwareLicense20()));
        addNotice(new Notice("Android Support Design", "https://source.android.com/", "Android Open Source Project", new ApacheSoftwareLicense20()));
        //todo always check if up to date
    }
}

package de.eickemeyer.wake.on.lan.entities;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import io.realm.RealmObject;
import io.realm.annotations.Index;

public class ScanResult extends RealmObject implements Parcelable {

    public String ip;
    @Index
    public String favName;
    public String mac;
    public String hostname;
    public String broadcast;
    public int icon;

    //required by realm
    public ScanResult() {
    }

    private ScanResult(ScanResultBuilder builder) {
        this.ip = builder.ip;
        this.favName = builder.favName;
        this.mac = builder.mac;
        this.hostname = builder.hostname;
        this.broadcast = builder.broadcast;
        this.icon = builder.icon;
    }

    @BindingAdapter("app:imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    //Parcelable Constructor
    public ScanResult(Parcel pc) {
        ip = pc.readString();
        favName = pc.readString();
        mac = pc.readString();
        hostname = pc.readString();
        broadcast = pc.readString();
        icon = pc.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(ip);
        parcel.writeString(favName);
        parcel.writeString(mac);
        parcel.writeString(hostname);
        parcel.writeString(broadcast);
        parcel.writeInt(icon);
    }

    public static final Parcelable.Creator<ScanResult> CREATOR = new Parcelable.Creator<ScanResult>() {
        public ScanResult createFromParcel(Parcel pc) {
            return new ScanResult(pc);
        }

        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };

    public static class ScanResultBuilder {
        private String ip;
        private String favName;
        private String mac;
        private String hostname;
        private String broadcast;
        private int icon;

        public ScanResultBuilder setIp(String ip) {
            this.ip = ip;
            return this;
        }

        public ScanResultBuilder setFavName(String favName) {
            this.favName = favName;
            return this;
        }

        public ScanResultBuilder setMac(String mac) {
            this.mac = mac;
            return this;
        }

        public ScanResultBuilder setHostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public ScanResultBuilder setBroadcast(String broadcast) {
            this.broadcast = broadcast;
            return this;
        }

        public ScanResultBuilder setIcon(int icon) {
            this.icon = icon;
            return this;
        }

        public ScanResult build() {
            return new ScanResult(this);
        }
    }
}

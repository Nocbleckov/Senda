package desarrollo.sip.senda.objetos;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

import desarrollo.sip.senda.abstractClass.WithImage;

/**
 * Created by DESARROLLO on 28/01/16.
 */
public class Aristas implements Serializable,Parcelable {

    private LatLng aristaSI;
    private LatLng aristaSD;
    private LatLng aristaII;
    private LatLng aristaID;

    public Aristas(LatLng aristaSI,LatLng aristaSD,LatLng aristaII, LatLng aristaID){
        this.aristaSI = aristaSI;
        this.aristaSD = aristaSD;
        this.aristaII = aristaII;
        this.aristaID = aristaID;
    }

    public Aristas(Parcel in){
        aristaSI =(LatLng) in.readValue(LatLng.class.getClassLoader());
        aristaSD =(LatLng) in.readValue(LatLng.class.getClassLoader());
        aristaII =(LatLng) in.readValue(LatLng.class.getClassLoader());
        aristaID =(LatLng) in.readValue(LatLng.class.getClassLoader());
    }

    public static final Creator<Aristas> CREATOR = new Creator<Aristas>() {
        @Override
        public Aristas createFromParcel(Parcel source) {
            return new Aristas(source);
        }

        @Override
        public Aristas[] newArray(int size) {
            return new Aristas[size];
        }
    };

    public LatLng getAristaSI() {
        return aristaSI;
    }

    public LatLng getAristaSD() {
        return aristaSD;
    }

    public LatLng getAristaII() {
        return aristaII;
    }

    public LatLng getAristaID() {
        return aristaID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(aristaSI);
        dest.writeValue(aristaSD);
        dest.writeValue(aristaII);
        dest.writeValue(aristaID);

    }
}

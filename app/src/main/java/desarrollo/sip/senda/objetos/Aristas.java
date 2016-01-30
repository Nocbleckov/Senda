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

    //private LatLng aristaSI;

    private double latAristaSI;
    private double lngAristaSI;

    //private LatLng aristaSD;

    private double latAristaSD;
    private double lngAristaSD;

    //private LatLng aristaII;

    private double latAristaII;
    private double lngAristaII;


    //private LatLng aristaID;

    private double latAristaID;
    private double lngAristaID;

    public Aristas(LatLng aristaSI,LatLng aristaSD,LatLng aristaII, LatLng aristaID){

        this.latAristaSI = aristaSI.latitude;
        this.lngAristaSI = aristaSI.longitude;

        this.latAristaSD = aristaSD.latitude;
        this.lngAristaSD = aristaSD.longitude;

        this.latAristaII = aristaII.latitude;
        this.lngAristaII = aristaII.longitude;
        
        this.latAristaID = aristaII.latitude;
        this.lngAristaID = aristaII.longitude;
    }

    public Aristas(Parcel in){

        latAristaSI = in.readDouble();
        lngAristaSI = in.readDouble();

        latAristaSD = in.readDouble();
        lngAristaSD = in.readDouble();

        latAristaII = in.readDouble();
        lngAristaII = in.readDouble();

        latAristaID = in.readDouble();
        lngAristaID = in.readDouble();

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
        return new LatLng(latAristaSI,lngAristaSI);
    }

    public LatLng getAristaSD() {
        return new LatLng(latAristaSD,lngAristaSD);
    }

    public LatLng getAristaII() {
        return new LatLng(latAristaII,lngAristaII);
    }

    public LatLng getAristaID() {
        return new LatLng(latAristaID,lngAristaID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeDouble(latAristaSI);
        dest.writeDouble(lngAristaSI);
        dest.writeDouble(latAristaSD);
        dest.writeDouble(lngAristaSD);
        dest.writeDouble(latAristaII);
        dest.writeDouble(lngAristaII);
        dest.writeDouble(latAristaID);
        dest.writeDouble(lngAristaID);

    }
}

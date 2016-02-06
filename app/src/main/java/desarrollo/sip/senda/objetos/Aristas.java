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
    private double latAristaSI;
    private double lngAristaSI;

    private double latAristaSD;
    private double lngAristaSD;

    private double latAristaII;
    private double lngAristaII;

    private double latAristaID;
    private double lngAristaID;

    /*
    * Guarda las aristas resultantes de la cuadrangulacion de la ruta
    * */

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

    /*
    * Este construccion es usado cuando se Parcea el objeto
    * */
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

    /*
    * Devuelve un LatLng tomando los valores de latAristaSI(Superior Izquierda) y lngAristaSI(Superior Izquierda)
    * */

    public LatLng getAristaSI() {
        return new LatLng(latAristaSI,lngAristaSI);
    }



    /*
    * Devuelve un LatLng tomando los valores de latAristaSD(Superior Derecha) y lngAristaSD(Superior Derecha)
    * */
    public LatLng getAristaSD() {
        return new LatLng(latAristaSD,lngAristaSD);
    }

    /*
    * Devuelve un LatLng tomando los valores de latAristaII(Inferiror Izquierda) y lngAristaII(Inferior Izquierda)
    * */
    public LatLng getAristaII() {
        return new LatLng(latAristaII,lngAristaII);
    }

    /*
    * Devuelve un LatLng tomando los valores de latAristaID(Inferiror Derecha) y lngAristaID(Inferiror Derecha)
    * */
    public LatLng getAristaID() {
        return new LatLng(latAristaID,lngAristaID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /*
    * estos datos son los que se escriben durente el parceo
    * */
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

package desarrollo.sip.senda.objetos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import desarrollo.sip.senda.abstractClass.WithImage;
import desarrollo.sip.senda.activities.MisRutas;

/**
 * Created by DESARROLLO on 16/01/16.
 */
public class MiRuta extends WithImage implements Parcelable,Serializable {


    private String identificador,siglas,municipio,estado,cadenaRuta,tiempoManual,original,version;

    private double centroLat;
    private double centroLng;


    private Aristas aristas;
    private ArrayList<Punto>destinos;

    /*
    *
    * Esta clase es la que se carga cuando se devuelven los datos de las rutas, de el web service
    *
    * recive Strings siglas,municipio,estado,idRuta,cadenaRuta,rutaImagen,tiempoManual,original y version
    *
    * recive un ArreyList de punto, un Latlng centro y aristas
    *
    * */

    public MiRuta(String siglas,String municipio,String estado,String idRuta,String cadenaRuta,String rutaImagen,ArrayList<Punto> destinos,LatLng puntoCentro,Aristas aristas,String tiempoManual,String original,String version){
        this.cadenaRuta = cadenaRuta;
        this.id = idRuta;
        this.siglas = siglas;
        this.municipio = municipio;
        this.estado = estado;
        this.identificador = idRuta+"_"+siglas+"_"+estado;
        this.rutaImagen = rutaImagen;
        this.destinos = destinos;
        this.tiempoManual = tiempoManual;
        this.original = original;
        this.version = version;

        this.centroLat = puntoCentro.latitude;
        this.centroLng = puntoCentro.longitude;

        this.latitud  =  ""+puntoCentro.latitude;
        this.longitud = ""+puntoCentro.longitude;
        this.aristas = aristas;
    }


    /*
    *
    * Este constructor es usado cuando se parcea el objeto
    *
    * */
    public MiRuta(Parcel in){

        identificador = in.readString();
        siglas = in.readString();
        municipio = in.readString();
        estado = in.readString();
        id = in.readString();
        cadenaRuta = in.readString();

        centroLat = (double)in.readDouble();
        centroLng = (double)in.readDouble();


        destinos = (ArrayList<Punto>)in.readValue(MiRuta.class.getClassLoader());
        latitud = in.readString();
        longitud = in.readString();

        tiempoManual = in.readString();
        original = in.readString();
        version = in.readString();

        aristas =(Aristas) in.readValue(MiRuta.class.getClassLoader());
        imagenCode = (byte[])in.readValue(MiRuta.class.getClassLoader());

    }

    public static final Creator<MiRuta> CREATOR = new Creator<MiRuta>() {
        @Override
        public MiRuta createFromParcel(Parcel source) {
            return new MiRuta(source);
        }

        @Override
        public MiRuta[] newArray(int size) {
            return new MiRuta[size];
        }
    };


    public void setCadenaRuta(String cadenaRuta) {
        this.cadenaRuta = cadenaRuta;
    }

    public String getCadenaRuta() {
        return cadenaRuta;
    }


    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public void setDestinos(ArrayList<Punto> destinos) {
        this.destinos = destinos;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getSiglas() {
        return siglas;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getEstado() {
        return estado;
    }

    public List<LatLng> getPuntos() {
        return  PolyUtil.decode(cadenaRuta);
    }

    public Aristas getAristas() {
        return aristas;
    }

    public String getTiempoManual() {
        return tiempoManual;
    }

    public String getOriginal() {
        return original;
    }

    public String getVersion() {
        return version;
    }

    public LatLng getPuntoCentro() {
        return new LatLng(centroLat,centroLng) ;
    }

    public ArrayList<Punto> getDestinos() {
        return destinos;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    /*
    *
    * Este metodo es usado antes del parceo para escribir los datos
    *
    * */

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identificador);
        dest.writeString(siglas);
        dest.writeString(municipio);
        dest.writeString(estado);
        dest.writeString(id);
        dest.writeString(cadenaRuta);

        dest.writeDouble(centroLat);
        dest.writeDouble(centroLng);

        dest.writeValue(destinos);
        dest.writeString(latitud);
        dest.writeString(longitud);
        dest.writeString(tiempoManual);
        dest.writeString(original);
        dest.writeString(version);
        dest.writeValue(aristas);
        dest.writeValue(imagenCode);
    }


}

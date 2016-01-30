package desarrollo.sip.senda.objetos;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import desarrollo.sip.senda.abstractClass.WithImage;


/**
 * Created by DESARROLLO on 03/12/15.
 */
public class Punto extends WithImage implements Parcelable,Serializable {

    private String referencias, pais, numero, municipio, localidad, idAccion, estatus, estado, direccion, colonia, codigoPostal, calle,cadenaRuta;
    //private LatLng coordenada;

    public Punto() {

    }

    public Punto(String referencias, String pais, String numero, String municipio, String longitud, String localidad, String latitud, String idPunto, String estatus, String estado, String direccion, String colonia, String codigoPostal, String calle, String cadenaRuta,String rutaImagen) {
        this.referencias = referencias;
        this.pais = pais;
        this.numero = numero;
        this.municipio = municipio;
        this.longitud = longitud;
        this.localidad = localidad;
        this.latitud = latitud;
        this.id = idPunto;
        //this.idAccion = idAccion;
        this.estatus = estatus;
        this.estado = estado;
        this.direccion = direccion;
        this.colonia = colonia;
        this.codigoPostal = codigoPostal;
        this.calle = calle;
        this.cadenaRuta = cadenaRuta;
        this.rutaImagen = rutaImagen;
        //latLng(latitud, longitud);
    }

    protected Punto(Parcel in) {
        referencias = in.readString();
        pais = in.readString();
        numero = in.readString();
        municipio = in.readString();
        longitud = in.readString();
        localidad = in.readString();
        latitud = in.readString();
        id = in.readString();
        idAccion = in.readString();
        estatus = in.readString();
        estado = in.readString();
        direccion = in.readString();
        colonia = in.readString();
        codigoPostal = in.readString();
        calle = in.readString();
        cadenaRuta = in.readString();
        rutaImagen = in.readString();
        //coordenada = in.readParcelable(LatLng.class.getClassLoader());
        //coordenada =(LatLng) in.readValue(LatLng.class.getClassLoader());
        imagenCode = (byte[]) in.readValue(Punto.class.getClassLoader());
    }

    public static final Creator<Punto> CREATOR = new Creator<Punto>() {
        @Override
        public Punto createFromParcel(Parcel in) {
            return new Punto(in);
        }

        @Override
        public Punto[] newArray(int size) {
            return new Punto[size];
        }

        /*public ArrayList<Punto> newArrayList(int size){
            return new ArrayList<Punto>(size);
        }*/

    };

    /*public void latLng(String lat, String lng) {
        try {
            this.coordenada = new LatLng(Float.parseFloat(lat), Float.parseFloat(lng));
        } catch (Exception e) {
            e.printStackTrace();
            this.coordenada = new LatLng(0, 0);
        }
    }*/

    /*public void setCoordenada(LatLng coordenada){
        this.coordenada = coordenada;
    }*/

    /*private void writeObject(java.io.ObjectOutputStream stream)throws IOException{
        stream.writeObject(referencias);
        stream.writeObject(pais);
        stream.writeObject(numero);
        stream.writeObject(municipio);
        stream.writeObject(localidad);
        stream.writeObject(idAccion);
        stream.writeObject(estatus);
        stream.writeObject(estado);
        stream.writeObject(direccion);
        stream.writeObject(colonia);
        stream.writeObject(codigoPostal);
        stream.writeObject(calle);
        stream.writeObject(cadenaRuta);
    }*/


    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getReferencias() {
        return referencias;
    }

    public String getPais() {
        return pais;
    }

    public String getNumero() {
        return numero;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getLocalidad() {
        return localidad;
    }

    public String getIdPunto() {
        return id;
    }

    public String getIdAccion() {
        return idAccion;
    }

    public String getEstatus() {
        return estatus;
    }

    public String getEstado() {
        return estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getColonia() {
        return colonia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public String getCalle() {
        return calle;
    }

    public LatLng getCoordenada() {
        return new LatLng(Double.parseDouble(latitud),Double.parseDouble(longitud));
    }

    public String getCadenaRuta(){
        return cadenaRuta;
    }

    public double LatDouble(){
        double lat = Double.parseDouble(latitud);
        return  lat;
    }

    public double LngDouble(){
        double lng = Double.parseDouble(longitud);
        return lng;
    }

    @Override
    public String toString() {
        return "Punto{" +
                "coordenada=" + getCoordenada() + "lat= " + latitud + "lng= " + longitud +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(referencias);
        dest.writeString(pais);
        dest.writeString(numero);
        dest.writeString(municipio);
        dest.writeString(longitud);
        dest.writeString(localidad);
        dest.writeString(latitud);
        dest.writeString(id);
        dest.writeString(idAccion);
        dest.writeString(estatus);
        dest.writeString(estado);
        dest.writeString(direccion);
        dest.writeString(colonia);
        dest.writeString(codigoPostal);
        dest.writeString(calle);
        dest.writeString(cadenaRuta);
        dest.writeString(rutaImagen);
        //dest.writeValue(coordenada);
        dest.writeByteArray(imagenCode);
    }

}

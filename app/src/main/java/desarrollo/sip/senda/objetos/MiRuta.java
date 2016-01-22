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
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import desarrollo.sip.senda.activities.MisRutas;

/**
 * Created by DESARROLLO on 16/01/16.
 */
public class MiRuta implements Parcelable,Serializable {
    private String identificador,siglas,municipio,estado,idRuta,cadenaRuta,rutaImagen;
    private List<LatLng> puntos;
    private LatLng puntoCentro;
    private byte[] imagenCode;
    private ArrayList<Punto>destinos;
    private Bitmap foto;


    public MiRuta(String siglas,String municipio,String estado,String idRuta,String cadenaRuta,String rutaImagen,ArrayList<Punto> destinos,LatLng puntoCentro){
        this.cadenaRuta = cadenaRuta;
        this.idRuta = idRuta;
        this.siglas = siglas;
        this.municipio = municipio;
        this.estado = estado;
        this.identificador = idRuta+"_"+siglas+"_"+estado;
        this.rutaImagen = rutaImagen;
        this.destinos = destinos;
        this.puntoCentro = puntoCentro;
    }

    public MiRuta(Parcel in){
        identificador = in.readString();
        siglas = in.readString();
        municipio = in.readString();
        estado = in.readString();
        idRuta = in.readString();
        cadenaRuta = in.readString();
        puntos = (List<LatLng>)in.readValue(MiRuta.class.getClassLoader());
        imagenCode = (byte[])in.readValue(MiRuta.class.getClassLoader());
        puntoCentro = (LatLng)in.readValue(MiRuta.class.getClassLoader());
        destinos = (ArrayList<Punto>)in.readValue(MiRuta.class.getClassLoader());
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

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public String getRutaImagen() {
        return rutaImagen;
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
        this.puntos = PolyUtil.decode(cadenaRuta);
        return puntos;
    }

    /*public void  getfoto(ImageView imageView){
        //new ColocarFoto(imageView).execute();
    }*/

    public LatLng getPuntoCentro() {
        return puntoCentro ;
    }

    public ArrayList<Punto> getDestinos() {
        return destinos;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(identificador);
        dest.writeString(siglas);
        dest.writeString(municipio);
        dest.writeString(estado);
        dest.writeString(idRuta);
        dest.writeString(cadenaRuta);
        dest.writeValue(puntos);
        dest.writeValue(imagenCode);
        dest.writeValue(puntoCentro);
        dest.writeValue(destinos);
    }

    protected byte[] obtenerImagen(){
        String url = rutaImagen;
        Bitmap bm = null;

        try{

            InputStream iS = new java.net.URL(url).openStream();
            bm = BitmapFactory.decodeStream(iS);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG,100,stream);
            imagenCode = stream.toByteArray();
            stream.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return imagenCode;
    }


    private class ColocarFoto extends AsyncTask<String,Void,Bitmap> implements Serializable{
        ImageView imageView;

        public ColocarFoto(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            imagenCode = obtenerImagen();
            int tamaño = imagenCode.length;
            Bitmap foto = BitmapFactory.decodeByteArray(imagenCode,0,tamaño);
            return foto;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

    }

}

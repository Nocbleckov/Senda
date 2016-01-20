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
    private String identificador,siglas,municipio,estado,idRuta;
    private List<LatLng> puntos;
    private LatLng puntoCentro;
    private byte[] imagenCode;
    private ArrayList<Punto>destinos;


    public MiRuta(String siglas,String municipio,String estado,String idRuta,String cadenaRuta){
        iniPuntos(cadenaRuta);
        this.idRuta = idRuta;
        new OnBackMisDestinos().execute();
        this.siglas = siglas;
        this.municipio = municipio;
        this.estado = estado;
        this.identificador = idRuta+"_"+siglas+"_"+estado;
        centro();
        new onBackImagen(cadenaRuta).execute();
    }

    public MiRuta(Parcel in){
        identificador = in.readString();
        siglas = in.readString();
        municipio = in.readString();
        estado = in.readString();
        idRuta = in.readString();
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

    public void iniPuntos(String cadena){
        this.puntos = PolyUtil.decode(cadena);
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
        return puntos;
    }

    public void  getfoto(ImageView imageView){
        new ColocarFoto(imageView).execute();
    }

    public LatLng getPuntoCentro() {
        return puntoCentro ;
    }

    public ArrayList<Punto> getDestinos() {
        return destinos;
    }

    private void centro(){
        ArrayList<Double> x=new ArrayList<>();
        ArrayList<Double> y=new ArrayList<>();
        for(int i = 0;i<puntos.size();i++){
            x.add(puntos.get(i).latitude);
            y.add(puntos.get(i).longitude);
        }
        double[] xa = Stuff.menorMayor(x);
        double[] ya = Stuff.menorMayor(y);
        MenorMayor xS = new MenorMayor(xa[0],xa[xa.length-1]);
        MenorMayor yS = new MenorMayor(ya[0],ya[ya.length-1]);

        LatLng centro = Stuff.puntoCentro(xS.menor,yS.menor,xS.mayor,yS.mayor);
        puntoCentro = centro;
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
        dest.writeValue(puntos);
        dest.writeValue(imagenCode);
        dest.writeValue(puntoCentro);
        dest.writeValue(destinos);
    }

    private class onBackImagen extends AsyncTask<String,Void,Bitmap>{
        String cadenaRuta;
        public  onBackImagen(String cadenaRuta){
            this.cadenaRuta = cadenaRuta;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            String centro = puntoCentro.latitude+","+ puntoCentro.longitude;

            String url = "https://maps.googleapis.com/maps/api/staticmap?center="+centro+"&zoom=15&size=800x480&path=weight:5%7Ccolor:blue%7Cenc:"+cadenaRuta+"&key=AIzaSyCN9dweEHH0yQXVVLyuCTxa_Es1Vk0gzJY";
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

            return bm;
        }
    }

    private class OnBackMisDestinos extends AsyncTask<String,String,ArrayList<Punto>>{

        @Override
        protected ArrayList<Punto> doInBackground(String... params) {

            try{
                HashMap<String,String> parametros = new HashMap<>();
                parametros.put("numPeticion", "3");
                parametros.put("idRuta", idRuta);
                Conexion conexion = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                conexion.setParametros(parametros);
                conexion.executar(Conexion.metodoPeticion.POST);
                String respuesta = conexion.getRespuesta();
                destinos = Stuff.obtenerPuntos(respuesta);
            }catch(Exception e){
                e.printStackTrace();
            }

            return null;
        }
    }

    private class ColocarFoto extends AsyncTask<String,Void,Bitmap> implements Serializable{
        ImageView imageView;

        public ColocarFoto(ImageView imageView){
            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
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

    private class MenorMayor implements Serializable{
        Double menor;
        Double mayor;

        public MenorMayor(Double menor,Double mayor){
            this.mayor = mayor;
            this.menor = menor;
        }

        public double getMayor() {
            return mayor;
        }

        public double getMenor() {
            return menor;
        }

        @Override
        public String toString() {
            return "MenorMayor{" +
                    "menor=" + menor +
                    ", mayor=" + mayor +
                    '}';
        }
    }

}

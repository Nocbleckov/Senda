package desarrollo.sip.senda.objetos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DESARROLLO on 16/01/16.
 */
public class MiRuta {
    private String identificador,siglas,municipio,estado,idRuta;
    private List<LatLng> puntos;
    private byte[] imagenCode;

    public MiRuta(String siglas,String municipio,String estado,String idRuta,String cadenaRuta){
        iniPuntos(cadenaRuta);
        this.idRuta = idRuta;
        this.siglas = siglas;
        this.municipio = municipio;
        this.estado = estado;
        this.identificador = idRuta+"_"+siglas+"_"+estado;
        new onBackImagen(cadenaRuta).execute();
    }

    public void iniPuntos(String cadena){
        this.puntos = PolyUtil.decode(cadena);
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

    public void  getfoto(ImageView imageView){
        new ColocarFoto(imageView).execute();
    }

    private LatLng centro(){
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
        return centro;
    }

    private class onBackImagen extends AsyncTask<String,Void,Bitmap>{
        String cadenaRuta;
        public  onBackImagen(String cadenaRuta){
            this.cadenaRuta = cadenaRuta;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            LatLng c = centro();
            String centro = c.latitude+","+c.longitude;

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

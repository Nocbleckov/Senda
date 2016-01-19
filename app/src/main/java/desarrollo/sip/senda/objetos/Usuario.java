package desarrollo.sip.senda.objetos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DESARROLLO on 02/12/15.
 */
public class Usuario extends Stuff implements Parcelable,Serializable {

    private String idUsuario,nombre,idPerfil,estado,url,idBrigada;
    private byte[] bytesNick,bytesPass;
    public byte[] fotoarray;

    public Usuario(){

    }

    public Usuario(String data){
        if (!data.equalsIgnoreCase("")) {
            JSONObject json;
            try {
                json =  new JSONObject(data);
                JSONArray usuario = json.optJSONArray("usuario");

                for(int i = 0; i<usuario.length();i++){

                    JSONObject obj = usuario.getJSONObject(i);
                    String nombre = obj.optString("nombre");
                    String idUsuario = obj.optString("idUsuario");
                    String idPerfil = obj.optString("idPerfil");
                    String estado = obj.optString("estado");
                    String fotoUrl = obj.optString("fotoUrl");
                    String idBrigada = obj.optString("idBrigada");
                    String nick = obj.optString("nickname");
                    String pass = obj.optString("pass");

                    this.nombre = nombre;
                    this.idUsuario = idUsuario;
                    this.idPerfil = idPerfil;
                    this.estado = estado;
                    this.url = fotoUrl;
                    this.idBrigada = idBrigada;
                    this.bytesNick = Stuff.codificar(nick.getBytes(Charset.forName("UTF-8")));
                    this.bytesPass = Stuff.codificar(pass.getBytes(Charset.forName("UTF-8")));



                }
                new DescargaFoto(url).execute();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Usuario(String idUsuario, String nombre, String idPerfil, String estado, String url) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.idPerfil = idPerfil;
        this.estado = estado;
        this.url = url;
        new DescargaFoto(url).execute();
    }

    protected Usuario(Parcel in) {
        idUsuario = in.readString();
        nombre = in.readString();
        idPerfil = in.readString();
        estado = in.readString();
        url = in.readString();
        idBrigada = in.readString();
        //nick = in.readString();
        //pass = in.readString();
        bytesNick =(byte[])in.readValue(Usuario.class.getClassLoader());
        bytesPass =(byte[])in.readValue(Usuario.class.getClassLoader());
        fotoarray =(byte[])in.readValue(Usuario.class.getClassLoader());
    }

    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdPerfil() {
        return idPerfil;
    }

    public String getEstado() {
        return estado;
    }

    public String getUrl() {
        return url;
    }

    public String getIdBrigada() {
        return idBrigada;
    }

    public String getNick() {
        try{
            String nick = new String(bytesNick,"UTF-8");
            return nick;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getPass() {
        try{
            String pass = new String(bytesPass,"UTF-8");
            return pass;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public void  getfoto(ImageView imageView){
        new ColocarFoto(imageView).execute();
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                '}'+getClass().getDeclaringClass();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUsuario);
        dest.writeString(nombre);
        dest.writeString(idPerfil);
        dest.writeString(estado);
        dest.writeString(url);
        dest.writeString(idBrigada);
        //dest.writeString(nick);
        dest.writeValue(bytesNick);
        //dest.writeString(pass);
        dest.writeValue(bytesPass);
        dest.writeValue(fotoarray);
    }

    public Class clase(){
        return DescargaFoto.class;
    }

    private class DescargaFoto extends AsyncTask<String,Void,Bitmap> implements Serializable{

        String  url;

        public  DescargaFoto(String url){
            this.url = url;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap foto;
            String urlDisplay = url;
            Bitmap bm = null;
            try{

                InputStream iS = new java.net.URL(urlDisplay).openStream();
                bm = BitmapFactory.decodeStream(iS);
                foto = bm;
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                foto.compress(Bitmap.CompressFormat.PNG,100,stream);
                fotoarray  =stream.toByteArray();
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
            int tamaño = fotoarray.length;
            Bitmap foto = BitmapFactory.decodeByteArray(fotoarray,0,tamaño);
            return foto;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

    }
}

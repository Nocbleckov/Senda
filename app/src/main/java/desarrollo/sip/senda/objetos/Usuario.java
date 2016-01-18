package desarrollo.sip.senda.objetos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by DESARROLLO on 02/12/15.
 */
public class Usuario extends Stuff implements Parcelable,Serializable {

    private String idUsuario,nombre,idPerfil,estado,url,idBrigada,nick,pass;
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
                    this.nick = nick;
                    this.pass = pass;



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
        nick = in.readString();
        pass = in.readString();
        fotoarray =(byte[]) in.readValue(Usuario.class.getClassLoader());
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
        return nick;
    }

    public String getPass() {
        return pass;
    }

    public void  getfoto(CircleImageView circleImageView){
        new ColocarFoto(circleImageView).execute();
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
        dest.writeString(nick);
        dest.writeString(pass);
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
        CircleImageView circleImageView;

        public ColocarFoto(CircleImageView circleImageView){
            this.circleImageView = circleImageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            int tamaño = fotoarray.length;
            Bitmap foto = BitmapFactory.decodeByteArray(fotoarray,0,tamaño);
            return foto;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            circleImageView.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }

    }
}

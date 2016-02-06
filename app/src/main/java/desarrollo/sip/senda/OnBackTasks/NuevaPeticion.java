package desarrollo.sip.senda.OnBackTasks;

import android.os.AsyncTask;

import java.util.HashMap;

import desarrollo.sip.senda.objetos.Conexion;

/**
 * Created by DESARROLLO on 03/02/16.
 */
public class NuevaPeticion extends AsyncTask<String,String,String>{

    HashMap<String,String> parametros;
    String respuesta;


    /*
    *
    * clase que hereda de AsyncTask recibe un hashmap de String-String
    *
    * y devuevle la respueste mediante una interface
    *
    * realiza conexiones con el webservice
    *
    * */
    public interface  AsyncResponse {
        void ProccesFinish(String respuesta);
    }

    public  AsyncResponse asyncResponse = null;

    public NuevaPeticion(HashMap<String,String>parametros,AsyncResponse asyncResponse){
        this.parametros = parametros;
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected String doInBackground(String... params) {

        try{
            Conexion conexion = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
            conexion.setParametros(parametros);
            conexion.executar(Conexion.metodoPeticion.POST);
            respuesta = conexion.getRespuesta();
        }catch (Exception e){
            e.printStackTrace();
        }

        return respuesta;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        asyncResponse.ProccesFinish(respuesta);
    }
}

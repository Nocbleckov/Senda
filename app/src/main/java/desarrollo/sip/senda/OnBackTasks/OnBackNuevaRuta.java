package desarrollo.sip.senda.OnBackTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import desarrollo.sip.senda.activities.EditarRutasActivity;
import desarrollo.sip.senda.listener.ChangeDataMap;
import desarrollo.sip.senda.objetos.Conexion;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;
import desarrollo.sip.senda.objetos.RequestDireccion;
import desarrollo.sip.senda.objetos.Ruta;
import desarrollo.sip.senda.objetos.Stuff;

/**
 * Created by DESARROLLO on 25/01/16.
 */
public class OnBackNuevaRuta extends AsyncTask<Boolean, Ruta, Ruta> {

    private ArrayList<Punto> direcciones;
    private RequestDireccion peticion;
    private Conexion con;
    private MiRuta miRuta;
    private EditarRutasActivity editarRutasActivity;
    private String idUsuario ;

    public OnBackNuevaRuta(ArrayList<Punto> direcciones, MiRuta miRuta, EditarRutasActivity editarRutasActivity,String idUsuario) {
        this.direcciones = direcciones;
        this.miRuta = miRuta;
        this.editarRutasActivity = editarRutasActivity;
        this.idUsuario = idUsuario;
        buildRequest();
    }

    public void buildRequest() {
        peticion = new RequestDireccion();
        peticion.setKey("AIzaSyCN9dweEHH0yQXVVLyuCTxa_Es1Vk0gzJY");
        peticion.setMode("walking");
        for (int i = 0; i < direcciones.size(); i++) {

            LatLng latLngTmp = new LatLng(Double.parseDouble(direcciones.get(i).getLatitud()), Double.parseDouble(direcciones.get(i).getLongitud()));

            if (i == 0) {
                peticion.setOrigin(latLngTmp);
            } else if (i == direcciones.size() - 1) {
                peticion.setDestination(latLngTmp);
            } else {
                peticion.addWaypoint(latLngTmp);
            }
        }
    }

    public List<idRuta> idPuntos(ArrayList<Punto> puntos){
        List<idRuta> temp = new ArrayList<>();
        for(int i = 0;i<puntos.size();i++){
            temp.add(new idRuta(Integer.parseInt(puntos.get(i).getId())));
        }
        return temp;
    }

    @Override
    protected Ruta doInBackground(Boolean... params) {
        try {
            con = new Conexion("https://maps.googleapis.com/maps/api/directions/json?");
            con.setParametros(peticion.getParametros());
            con.executar(Conexion.metodoPeticion.GET);
            String respuesta = con.getRespuesta();
            Ruta ruta = Stuff.obtenerRuta(respuesta);
            return ruta;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Ruta ruta) {
        super.onPostExecute(ruta);
        if (ruta != null) {

            this.miRuta.setCadenaRuta(ruta.getPolyLine());
            ChangeDataMap.setRuta(this.miRuta);
            ChangeDataMap.iniChangeDataMap();

            Gson gson = new Gson();

            String jsonArray = gson.toJson(idPuntos(miRuta.getDestinos()));
            HashMap<String,String> parametros = new HashMap<>();
            parametros.put("numPeticion","10");
            parametros.put("cadenaRuta",ruta.getPolyLine());
            parametros.put("siglas",miRuta.getSiglas());
            parametros.put("tiempoManual",miRuta.getTiempoManual());
            parametros.put("idAccionCambio",miRuta.getOriginal());
            parametros.put("idUsuario", idUsuario);
            parametros.put("idRutaActual",miRuta.getId());
            parametros.put("arregloPuntos",jsonArray);


            NuevaPeticion nuevaPeticion = (NuevaPeticion) new NuevaPeticion(parametros,new NuevaPeticion.AsyncResponse() {
                @Override
                public void ProccesFinish(String respuesta) {
                    Log.wtf("respuesta",respuesta);
                }
            }).execute();

            editarRutasActivity.finish();
        } else {
            editarRutasActivity.finish();
        }
    }

    private class idRuta{
        int idRuta;
        public idRuta(int idRuta){
            this.idRuta = idRuta;
        }
    }
}

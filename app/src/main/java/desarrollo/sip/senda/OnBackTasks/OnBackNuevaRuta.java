package desarrollo.sip.senda.OnBackTasks;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import desarrollo.sip.senda.activities.EditarRutasActivity;
import desarrollo.sip.senda.activities.MapaActivity;
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
public class OnBackNuevaRuta extends AsyncTask<Boolean,Ruta,Ruta> {

    private ArrayList<Punto> direcciones;
    private RequestDireccion peticion;
    private Conexion con;
    private MiRuta ruta;
    private EditarRutasActivity editarRutasActivity;

    public OnBackNuevaRuta(ArrayList<Punto> direcciones,MiRuta ruta,EditarRutasActivity editarRutasActivity){
        this.direcciones = direcciones;
        this.ruta = ruta;
        this.editarRutasActivity = editarRutasActivity;
        buildRequest();
    }
    public void buildRequest(){
        peticion = new RequestDireccion();
        peticion.setKey("AIzaSyCN9dweEHH0yQXVVLyuCTxa_Es1Vk0gzJY");
        peticion.setMode("walking");
        for(int i = 0;i<direcciones.size();i++){

            LatLng latLngTmp = new LatLng(Double.parseDouble(direcciones.get(i).getLatitud()),Double.parseDouble(direcciones.get(i).getLongitud()));

            if(i == 0){
                peticion.setOrigin(latLngTmp);
            }else if(i == direcciones.size()-1){
                peticion.setDestination(latLngTmp);
            }else{
                peticion.addWaypoint(latLngTmp);
            }
        }
    }

    @Override
    protected Ruta doInBackground(Boolean... params) {
        try{
            con = new Conexion("https://maps.googleapis.com/maps/api/directions/json?");
            con.setParametros(peticion.getParametros());
            con.executar(Conexion.metodoPeticion.GET);
            String respuesta = con.getRespuesta();
            Ruta ruta = Stuff.obtenerRuta(respuesta);
            return  ruta;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    @Override
    protected void onPostExecute(Ruta ruta) {
        super.onPostExecute(ruta);
        this.ruta.setCadenaRuta(ruta.getPolyLine());
        ChangeDataMap.setRuta(this.ruta);
        ChangeDataMap.iniChangeDataMap();
        editarRutasActivity.finish();
    }
}

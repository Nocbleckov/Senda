package desarrollo.sip.senda.listener;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.activities.DetallesPuntoActivity;
import desarrollo.sip.senda.objetos.Conexion;
import desarrollo.sip.senda.objetos.Punto;
import desarrollo.sip.senda.objetos.Stuff;

/**
 * Created by DESARROLLO on 04/12/15.
 */
public class ListenerLocation implements LocationListener {

    Button iniciar;
    DetallesPuntoActivity view;
    View viewM;
    Punto puntoDest;
    Context context;
    boolean primera = true;
    ProgressDialog pD;
    OnBackContador contador = new OnBackContador();
    private String idUsuario;
    Dialog dialog;
    LocationManager manager;

    /*
    * Esta clase es un d@$!"dre puede ser bastante mas optimizada
    *
    * hereda de LocationListener
    * calcula la distancia del punto obtenido por el gps con respecto al punto seleccionado
    *
    * Para ser insatnciada debe recibir una activiadad DetallesPuntosActivity, Punto, Context, View, String idUsuario y LocationManager
    *
    * */

    public ListenerLocation(DetallesPuntoActivity view, Punto puntoDest, Context context, View viewM, String idUsuario, LocationManager manager) {
        this.view = view;
        this.puntoDest = puntoDest;
        this.context = context;
        this.viewM = viewM;
        this.idUsuario = idUsuario;
        this.manager = manager;

        iniciar = (Button) view.findViewById(R.id.botonIniciarEncuesta_DetallesPunto);
        iniciar.setEnabled(false);
        pD = ProgressDialog.show(context, "Calculando su Distancia", "Espere porfavor ", true);
        dialog = Stuff.dialogCsm(this.context, "Enciada Su GPS");
        contador.execute();
    }

    /*
    * Metodo sobreescrito de la interface LocatinoListener
    *
    * es llamado cada vez que la posicion cambia
    *
    * */
    @Override
    public void onLocationChanged(Location location) {
        LatLng puntoOrigen = new LatLng(location.getLatitude(), location.getLongitude());
        if (primera) {
            iniciar.setEnabled(estaCerca(puntoDest, puntoOrigen));
            pD.dismiss();
            contador.cancel(true);
            new OnBackRegistroPos(location.getLatitude(), location.getLongitude()).execute();
            if (estaCerca(puntoDest, puntoOrigen)) {
                Stuff.toastCsmCntx(context, "Ahora puede iniciar la encuesta").show();
                manager.removeUpdates(this);
            } else {
                Stuff.toastCsmCntx(context, "No se encuntra lo suficientemente cerca para iniciar la encuesta").show();
                manager.removeUpdates(this);
            }
        }
        primera = false;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.wtf("ESTADO: ", provider);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.wtf("ESTADO", "ON");
        pD.dismiss();
        dialog.dismiss();
    }


    /*
    *
    * Metodo sobreescrito por la interface
    *
    * Es llamado cada vez que cambia el estado del gps
    *
    * */
    @Override
    public void onProviderDisabled(String provider) {
        if (primera != false) {
            contador.cancel(true);
            pD.dismiss();
        }
        Log.wtf("ESTADO", "OFF " + provider);
        Log.wtf("Contex", contador.toString());
        dialog.show();

    }


    /*
    *
    * realiza el calculo para saber si estas cerca del punto
    *
    * */

    public boolean estaCerca(Punto origen, LatLng destino) {
        double latO = origen.LatDouble();
        double lngO = origen.LngDouble();
        double latD = destino.latitude;
        double lngD = destino.longitude;

        if (distancia(latO, lngO, latD, lngD) <= 0.00040) {
            //Stuff.toastCsm(viewM, "Ahora puede iniciar la encuesta").show();
            return true;
        } else {
            return false;
        }
    }


    /*
    *
    * Calcula la distancia
    * */
    public double distancia(double x1, double y1, double x2, double y2) {
        double r = 0;
        double x = x2 - x1;
        double y = y2 - y1;
        double xC = Math.pow(x, 2);
        double yC = Math.pow(y, 2);

        r = Math.sqrt(xC + yC);

        Log.wtf("Distancia", "" + r);
        return r;
    }


    /*
    * Clase interna que hereda de AsyncTask
     *
     * si no se puede determinar la posisicon detiene el hilo
     * si se obtiene es creada una tubla en acciones de la base de datos
     *
    * */

    private class OnBackContador extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                Thread.sleep(17000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            pD.dismiss();
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Stuff.toastCsmCntx(context, "No se pudo determinar su posicion").show();
            iniciar.setEnabled(true);
        }
    }

    private class OnBackRegistroPos extends AsyncTask<String, String, String> {


        private Double latitud, longitud;

        public OnBackRegistroPos(Double latitud, Double longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Conexion con = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("numPeticion", "6");
                parametros.put("idUsuario", idUsuario);
                parametros.put("lat", "" + latitud);
                parametros.put("lng", "" + longitud);


                con.setParametros(parametros);
                con.executar(Conexion.metodoPeticion.POST);
                String respuesta = con.getRespuesta();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

}

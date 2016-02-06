package desarrollo.sip.senda.listener;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;

/**
 * Created by DESARROLLO on 25/01/16.
 */
public final class ChangeDataMap {

    /*
    * Esta es una clase final que recibe un mapa y una ruta
    *
    * su funcion es cambiar el mapa con los datos que reciba en la ruta
    *
    *
    * */

    private static GoogleMap mMap;
    private static MiRuta ruta;

    private ChangeDataMap() {

    }

    /*
    * se le asigna el Mapa
    * */
    public static void setmMap(GoogleMap mMap) {
        ChangeDataMap.mMap = mMap;
    }

    /*
    * se le asigna la Ruta
    * */
    public static void setRuta(MiRuta ruta) {
        ChangeDataMap.ruta = ruta;
    }


    /*
    * devuelve la ruta
    * */
    public static MiRuta getRuta() {
        return ruta;
    }


    /*
    * invoca los metodos colocarPuntos y moverCamara
    *
    * agrega una polyline al mapa y la eleva 1300
    *
    * */
    public static void iniChangeDataMap() {
        mMap.clear();
        colocarPuntos(ruta.getDestinos(), mMap);
        moverCamara(ruta.getPuntoCentro(), mMap);
        Polyline pol = mMap.addPolyline(colocarRuta(ruta.getPuntos()));
        pol.setZIndex(1300);
    }

    /*
    * cambia la camara del mapa, al punto latlng que se le de
    * */
    public static void moverCamara(LatLng latLng, GoogleMap mMap) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /*
    * centra la camara y cambia el angulo
    * */
    public static void centrarCamara(LatLng latLng, int zoom, int angulo, GoogleMap mMap) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoom).tilt(angulo).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    /*
    *
    * coloca marcas en el mapa es necesario pasarle un arreglo de puntos
    *
    * */
    public static void colocarPuntos(ArrayList<Punto> destinos, GoogleMap mMap) {
        for (int i = 0; i < destinos.size(); i++) {
            LatLng temp = destinos.get(i).getCoordenada();
            Marker marcaTemp = mMap.addMarker(new MarkerOptions().position(temp).title(destinos.get(i).getDireccion()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    /*
    *
    * crea un PolylineOption y lo devuelve es necesario para poder crear una Polyline
    *
    * */
    public static PolylineOptions colocarRuta(List<LatLng> puntos) {
        PolylineOptions polylineOptions = null;
        polylineOptions = new PolylineOptions();
        polylineOptions.addAll(puntos);
        polylineOptions.width(8);
        polylineOptions.color(Color.argb(150, 80, 190, 160));
        return polylineOptions;
    }

    public static String aString() {
        return "static map: " + mMap.toString() + " , " + "static ruta: " + ruta.toString();
    }
}

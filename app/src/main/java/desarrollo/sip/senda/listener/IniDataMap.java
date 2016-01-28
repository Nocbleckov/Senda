package desarrollo.sip.senda.listener;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;

/**
 * Created by DESARROLLO on 25/01/16.
 */
public  class IniDataMap {

    public static void initDataMap(MiRuta ruta,GoogleMap mMap){
        colocarPuntos(ruta.getDestinos(), mMap);
        moverCamara(ruta.getPuntoCentro(),mMap);
        Polyline pol =mMap.addPolyline(colocarRuta(ruta.getPuntos()));
        pol.setZIndex(1300);
    }

    public static void moverCamara(LatLng latLng,GoogleMap mMap) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static void centrarCamara(LatLng latLng, int zoom, int angulo,GoogleMap mMap) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoom).tilt(angulo).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public static void colocarPuntos(ArrayList<Punto> destinos,GoogleMap mMap) {
        for (int i = 0; i < destinos.size(); i++) {
            LatLng temp = destinos.get(i).getCoordenada();
            Marker marcaTemp = mMap.addMarker(new MarkerOptions().position(temp).title(destinos.get(i).getDireccion()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }
    public static PolylineOptions colocarRuta(List<LatLng> puntos) {
        PolylineOptions polylineOptions = null;
        polylineOptions = new PolylineOptions();
        polylineOptions.addAll(puntos);
        polylineOptions.width(8);
        polylineOptions.color(Color.argb(150, 80, 190, 160));
        return polylineOptions;
    }
}

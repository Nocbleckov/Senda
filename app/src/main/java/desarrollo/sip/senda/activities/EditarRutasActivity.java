package desarrollo.sip.senda.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.adaptadores.AdaptadorRutaEditar;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;

public class EditarRutasActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ListView listaRutaEditable;
    private MiRuta ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_rutas);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEditar);
        mapFragment.getMapAsync(this);

        ruta = (MiRuta) getIntent().getExtras().get("miRuta");
        setTitle("Edición de Ruta");

        listaRutaEditable = (ListView)findViewById(R.id.listaRuta_EditarRuta);
        AdaptadorRutaEditar adaptador = new AdaptadorRutaEditar(this,ruta.getDestinos(),this);
        listaRutaEditable.setAdapter(adaptador);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.addPolyline(colocarRuta(ruta.getPuntos()));
        moverCamara(ruta.getPuntoCentro());
        colocarPuntos(ruta.getDestinos());
    }

    protected void moverCamara(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void centrarCamara(LatLng latLng,int zoom,int angulo){
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(zoom).tilt(angulo).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void colocarPuntos(ArrayList<Punto> destinos) {
        for (int i = 0; i < destinos.size(); i++) {
            LatLng temp = destinos.get(i).getCoordenada();
            Marker marcaTemp = mMap.addMarker(new MarkerOptions().position(temp).title(destinos.get(i).getDireccion()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }

    public PolylineOptions colocarRuta(List<LatLng> puntos) {
        PolylineOptions polylineOptions = null;
        polylineOptions = new PolylineOptions();
        polylineOptions.addAll(puntos);
        polylineOptions.width(8);
        polylineOptions.color(Color.argb(150, 80, 190, 160));
        return polylineOptions;
    }

}

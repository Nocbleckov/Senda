package desarrollo.sip.senda.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private MiRuta ruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        //ruta =(MiRuta)getIntent().getExtras().get("miRuta");
        mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap ;
        //mMap.addPolyline(colocarRuta(ruta.getPuntos()));
       // moverCamara(ruta.getPuntoCentro());
        //colocarPuntos(ruta.getDestinos());
    }

    public PolylineOptions colocarRuta(List<LatLng> puntos){
        PolylineOptions polylineOptions = null;
        polylineOptions = new PolylineOptions();
        polylineOptions.addAll(puntos);
        polylineOptions.width(8);
        polylineOptions.color(Color.argb(150, 80, 190, 160));
        return polylineOptions;
    }
    public void moverCamara(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    public void colocarPuntos(ArrayList<Punto> destinos){
        for(int i = 0 ; i <destinos.size();i++){
            LatLng temp = destinos.get(i).getCoordenada();
            Marker marcaTemp = mMap.addMarker(new MarkerOptions().position(temp).title(destinos.get(i).getDireccion()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        }
    }


}

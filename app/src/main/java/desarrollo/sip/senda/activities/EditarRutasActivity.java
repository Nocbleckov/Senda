package desarrollo.sip.senda.activities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import desarrollo.sip.senda.listener.ListerEditarRuta;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;

public class EditarRutasActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ListView listaRutaEditable;
    private MiRuta ruta;
    private ArrayList<Punto> destinosMostrar = new ArrayList<>();
    private ArrayList<Punto> destinosOrg = new ArrayList<>();
    private AdaptadorRutaEditar adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_rutas);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEditar);
        mapFragment.getMapAsync(this);

        ruta = (MiRuta) getIntent().getExtras().get("miRuta");
        destinosMostrar.addAll(ruta.getDestinos());
        destinosOrg = ruta.getDestinos();
        setTitle("Edición de Ruta");
        iniLista();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.addPolyline(colocarRuta(ruta.getPuntos()));
        iniListener(adaptador, destinosMostrar);
        moverCamara(ruta.getPuntoCentro());
        colocarPuntos(ruta.getDestinos());
    }

    public void iniLista(){
        listaRutaEditable = (ListView)findViewById(R.id.listaRuta_EditarRuta);
        adaptador = new AdaptadorRutaEditar(this,destinosMostrar,this);
        listaRutaEditable.setAdapter(adaptador);
        View mifooter = ((LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout_button,null,false);
        listaRutaEditable.addFooterView(mifooter);
        setOnClickButtons(mifooter);
    }

    public void setOnClickButtons(View view){
        Button botonRecargar = (Button)view.findViewById(R.id.botonRecargar_FooteList);
        Button botonAceptar = (Button)view.findViewById(R.id.botonAceptar_FooterList);

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        botonRecargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinosMostrar.addAll(ruta.getDestinos());
                adaptador.notifyDataSetChanged();
            }
        });

    }

    public void iniListener(AdaptadorRutaEditar adaptadorRutaEditar,List<Punto> puntos){
        ListerEditarRuta listerEditarRuta = new ListerEditarRuta(adaptadorRutaEditar,puntos,this,destinosOrg);
        mMap.setOnMarkerClickListener(listerEditarRuta);
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

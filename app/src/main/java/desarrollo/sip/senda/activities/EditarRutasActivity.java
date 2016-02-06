package desarrollo.sip.senda.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import desarrollo.sip.senda.OnBackTasks.OnBackNuevaRuta;
import desarrollo.sip.senda.R;
import desarrollo.sip.senda.adaptadores.AdaptadorRutaEditar;
import desarrollo.sip.senda.listener.ChangeDataMap;
import desarrollo.sip.senda.listener.IniDataMap;
import desarrollo.sip.senda.listener.ListerEditarRuta;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;
import desarrollo.sip.senda.objetos.Usuario;

public class EditarRutasActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private ListView listaRutaEditable;
    private MiRuta ruta;
    private ArrayList<Punto> destinosMostrar = new ArrayList<>();
    private ArrayList<Punto> destinosOrg = new ArrayList<>();
    private AdaptadorRutaEditar adaptador;
    private MapaActivity mapaActivity;
    private Usuario usuario;



    /*
    * Metodo sobreescrito heredado del padre AppComparActivity
    * se referencian los valores ,obtenidos del getSupporFragmetManager que busca el id mapEditar, a mapFragment
    *
    * se referencian el usuario obtenido del intent a el usuario de la clase
    *
    * se referencia la ruta obtenida del intent a la ruta de la clase
    *
    * se llena el Arraylist destinosMostrar con rutas.destinos
    *
    * ruta.destinos es referenciada por destinosOrg
    *
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_rutas);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapEditar);
        mapFragment.getMapAsync(this);

        ruta = (MiRuta) getIntent().getExtras().get("miRuta");
        usuario = (Usuario)getIntent().getExtras().get("usuario");

        destinosMostrar.addAll(ruta.getDestinos());
        destinosOrg = ruta.getDestinos();
        setTitle("Edición de Ruta");
    }


    /*
    * metodo sobreescrito de la interface OnMapReadyCallBack
    * es llamado cuando el mapa esta listo
    *
    * invoca los metodos iniLista() e iniListener()
    *
    * invoca el metodo estatico iniDataMap de la clase IniDataMap
    *
    * */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        iniLista();
        iniListener(adaptador, destinosMostrar);
        IniDataMap.initDataMap(ruta, mMap);
    }

    /*
    * refencia el ListView encontrado en el xml con el id listaRuta_EditarRuta, al listaRutaEditable de la clase
    * se inicializa el AdaptadorRutaEditar
    *
    * se le asigna como adapter ,al Listview de la clase, el AdaptadorRutaEditar
    *
    * */
    public void iniLista() {
        listaRutaEditable = (ListView) findViewById(R.id.listaRuta_EditarRuta);
        adaptador = new AdaptadorRutaEditar(this, destinosMostrar,mMap);
        listaRutaEditable.setAdapter(adaptador);
        View mifooter = ((LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout_button, null, false);
        listaRutaEditable.addFooterView(mifooter);
        setOnClickButtons(mifooter);
    }


    /*
    *
    * metodo  que agrega los OnClickListener a los botones aceptar y recargar
    *
    * botonAceptar al precionarse limpia los destinos y agrega los nuevos obtenidos al intaciar y executar la clase OnBackNuevaRuta
    *
    * botonRecargar al precionarse el arreglo destinoMostrar es llenado con ruta.destinos
    *
    * */
    public void setOnClickButtons(View view) {
        Button botonRecargar = (Button) view.findViewById(R.id.botonRecargar_FooteList);
        Button botonAceptar = (Button) view.findViewById(R.id.botonAceptar_FooterList);

        botonAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (destinosMostrar.size() == destinosOrg.size()) {
                    ruta.getDestinos().clear();
                    ruta.getDestinos().addAll(destinosMostrar);
                    new OnBackNuevaRuta(ruta.getDestinos(), ruta,EditarRutasActivity.this,usuario.getIdUsuario()).execute();
                }
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

    public void iniListener(AdaptadorRutaEditar adaptadorRutaEditar, List<Punto> puntos) {
        ListerEditarRuta listerEditarRuta = new ListerEditarRuta(adaptadorRutaEditar, puntos, this, destinosOrg);
        mMap.setOnMarkerClickListener(listerEditarRuta);
    }


}

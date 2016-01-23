package desarrollo.sip.senda.activities;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import desarrollo.sip.senda.R;
import desarrollo.sip.senda.adaptadores.AdaptadorRutaEditar;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private MiRuta ruta;
    private boolean estado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        ruta = (MiRuta) getIntent().getExtras().get("miRuta");
        LayoutInflater inflater = LayoutInflater.from(this);
        View cstmAction = inflater.inflate(R.layout.csmactionbar_layout, null);


        android.support.v7.app.ActionBar bar = getSupportActionBar();
        ImageButton hamButton =(ImageButton)cstmAction.findViewById(R.id.iconoMenu);

        startDrawer(this, this);

        hamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        if(bar != null){
            bar.setTitle("");
            bar.setCustomView(cstmAction);
            bar.setDisplayShowCustomEnabled(true);
        }


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(19.3910038, -99.2836967)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        mMap.addPolyline(colocarRuta(ruta.getPuntos()));

        colocarPuntos(ruta.getDestinos());
        moverCamara(ruta.getPuntoCentro());

    }

    public void startDrawer(final Context context, final MapaActivity mapaActivity) {
        navigationView = (NavigationView) findViewById(R.id.navigationView_Frag);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.miInfo:

                        break;
                    case R.id.editarRuta:
                        Intent i = new Intent(MapaActivity.this,EditarRutasActivity.class);
                        i.putExtra("miRuta", (Parcelable) ruta);
                        startActivity(i);
                        break;
                    case R.id.guardarMapas:

                        break;
                }

                return false;
            }
        });
        navigationView.getHeaderCount();
        View header = navigationView.getHeaderView(0);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, 0, 0) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                estado = true;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                estado = false;
            }
        };

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    public PolylineOptions colocarRuta(List<LatLng> puntos) {
        PolylineOptions polylineOptions = null;
        polylineOptions = new PolylineOptions();
        polylineOptions.addAll(puntos);
        polylineOptions.width(8);
        polylineOptions.color(Color.argb(150, 80, 190, 160));
        return polylineOptions;
    }

    public void moverCamara(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).tilt(45).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void colocarPuntos(ArrayList<Punto> destinos) {
        for (int i = 0; i < destinos.size(); i++) {
            LatLng temp = destinos.get(i).getCoordenada();
            Marker marcaTemp = mMap.addMarker(new MarkerOptions().position(temp).title(destinos.get(i).getDireccion()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
    }


}

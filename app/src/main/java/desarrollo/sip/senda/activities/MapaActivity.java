package desarrollo.sip.senda.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.listener.ChangeDataMap;
import desarrollo.sip.senda.listener.IniDataMap;
import desarrollo.sip.senda.listener.ListenerMarkers;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Punto;
import desarrollo.sip.senda.objetos.Stuff;
import desarrollo.sip.senda.objetos.Usuario;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback,Serializable {

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private MiRuta ruta;
    private boolean estado = false;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        ruta = (MiRuta) getIntent().getExtras().get("miRuta");
        usuario = (Usuario)getIntent().getExtras().get("usuario");
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

        ListenerMarkers listenerMarkers = new ListenerMarkers(ruta.getDestinos(),this,usuario);
        mMap.setOnMarkerClickListener(listenerMarkers);
        IniDataMap.initDataMap(ruta, mMap);
        ChangeDataMap.setmMap(mMap);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        drawerLayout.closeDrawer(Gravity.LEFT);
        if(ChangeDataMap.getRuta() != null){
            if (!ruta.getCadenaRuta().equals(ChangeDataMap.getRuta().getCadenaRuta())) {
                this.ruta = ChangeDataMap.getRuta();
                IniDataMap.initDataMap(this.ruta, mMap);
            }
        }
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


}

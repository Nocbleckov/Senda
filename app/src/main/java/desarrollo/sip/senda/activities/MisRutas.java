package desarrollo.sip.senda.activities;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.TileClasses.TileDwlManager;
import desarrollo.sip.senda.adaptadores.AdaptadorMisRutas;
import desarrollo.sip.senda.adaptadores.AdaptadorRecyclerVMisRutas;
import desarrollo.sip.senda.objetos.Conexion;
import desarrollo.sip.senda.objetos.Login;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Senda;
import desarrollo.sip.senda.objetos.Stuff;
import desarrollo.sip.senda.objetos.Usuario;


public class MisRutas extends AppCompatActivity {

    private Usuario usuario;
    private TextView textUsuario;
    private ImageView foto, errorConexion;
    private RecyclerView recyclerMisRutas;
    private ConnectivityManager cm;
    private NetworkInfo miWifi;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private boolean estado = false;

    /*
    * Metodo sobreescrito del padre AppCompatActivity es llamado cuando se inicia la actividad por primera vez
    *
    * Se referencia los objetos antes instanciados ConnectivityManager y NetworkInfo
    * Se referencia el objeto usuario obteniendo el valor del Intent
    *
    *
    *
    * */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_rutas);

        cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        miWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        this.usuario = (Usuario) getIntent().getExtras().get("usuario");

        errorConexion = (ImageView) findViewById(R.id.imagenFalloConexion_MiRutas);

        if (miWifi.getState() == NetworkInfo.State.CONNECTED) {
            iniciarWidgets();
            new OnBackMisRutas().execute();
        } else {
            errorConexion.setVisibility(View.VISIBLE);
            iniciarActionBar();
        }
    }


    /*
    * invoca al metodo iniciarActionBar
    * recyclerMisRutas es inicializado con el valor Obtenido del xml y el id R.id.recyclerVMiRuta_MisRutas
    *
    * */
    public void iniciarWidgets() {
        iniciarActionBar();
        recyclerMisRutas = (RecyclerView) findViewById(R.id.recyclerVMiRuta_MisRutas);
    }


    /*
    * Este metodo instancia y referencia un LayoutInflater que servira
    * para inflar un View que sera asignado como customView al ActionBar
    * */
    public void iniciarActionBar() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View cstmAction = inflater.inflate(R.layout.csmactionbar_layout, null);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        ImageButton hamButton = (ImageButton) cstmAction.findViewById(R.id.iconoMenu);
        TextView title = (TextView) cstmAction.findViewById(R.id.titleBar_CSMActionBar);

        title.setText("Mis Rutas: " + usuario.getNombre());

        startDrawer(R.id.navigationView_Frag, casos());

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

        if (bar != null) {
            bar.setTitle("");
            bar.setCustomView(cstmAction);
            bar.setDisplayShowCustomEnabled(true);
        }

    }


    /*
    * Se instancia la clase NavigationView y se llama su interface interna OnNavigationItemSelectedListener y lo de vuelve
    *
    * Dentro de este metodo ,OnNavigationItemSelectedListener, es donde va la logia del navigationView dentro del DrawerLayout
    * */

    public NavigationView.OnNavigationItemSelectedListener casos() {
        return new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.generarSENDA:
                        Senda ruta = cargarArchivo();
                        if (ruta != null) {
                            iniciarWidgets();
                            cargarRecyclerView(ruta.getMisRutas());
                        }
                        break;
                }

                return true;
            }
        };
    }


    /*
    *
    * Este metodo instancia e inicializa la clase LinearLayoutManager
    * se le asigna este linearlayoutManager a el recyclerView
    *
    * Se intancia e inicializa ka clase AdapatadorRecyclerVMisRutas y posteriosmente es
    * asignada al recyclerMisRutas
    *
    * */
    public void cargarRecyclerView(ArrayList<MiRuta> rutas) {
        errorConexion.setVisibility(View.INVISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerMisRutas.setLayoutManager(layoutManager);
        AdaptadorRecyclerVMisRutas adaptadorRecyclerVMisRutas = new AdaptadorRecyclerVMisRutas(rutas, this);
        recyclerMisRutas.setAdapter(adaptadorRecyclerVMisRutas);
    }

    /*
    * Inicia la activiada MapaActivity
    * */
    public void activiadMapa(MiRuta miRuta) {
        Intent i = new Intent(MisRutas.this, MapaActivity.class);
        Log.wtf("Intent", i.toString());
        i.putExtra("miRuta", (Parcelable) miRuta);
        i.putExtra("usuario", (Parcelable) usuario);
        startActivity(i);
    }


    /*
    * Genera un archivo en la ruta root/senda/data con el nombre de MisRutas.senda
    * */
    public void generarSENDA(Senda rutas, String nombre) {
        try {
            File archivo = new File(Stuff.crearRuta("/senda/data") + "/MisRutas.senda");
            archivo.createNewFile();
            FileOutputStream fOs = new FileOutputStream(archivo, false);
            ObjectOutputStream oOs = new ObjectOutputStream(fOs);
            oOs.writeObject(rutas);
            oOs.close();
            fOs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Toast.makeText(this, "Se a generado su SENDA", Toast.LENGTH_SHORT).show();
        }
    }


    /*
    * Obtiene el archivo binario en la ruta root/senda/data con el nombre de Misrutas.senda
    * lo castea a un objeto Senda y lo devuelve
    * */
    public Senda cargarArchivo() {
        Senda rutas = null;
        try {
            File archivo = new File(Environment.getExternalStorageDirectory() + "/senda/data" + "/Misrutas.senda");
            FileInputStream iS = new FileInputStream(archivo);
            ObjectInputStream oiS = new ObjectInputStream(iS);
            rutas = (Senda) oiS.readObject();
            oiS.close();
            iS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rutas;
    }



    /*
    * Inicia el NavigationView y el DrawerLayout necesita el id ,asignado al navigationView en el xml, y un OnNavigationItemSelectedListener
    * */
    public void startDrawer(int view, NavigationView.OnNavigationItemSelectedListener navigationListener) {
        navigationView = (NavigationView) findViewById(view);
        navigationView.setNavigationItemSelectedListener(navigationListener);
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


    /*
    * clase interna cuando es llamada y ejecutada obtiene las rutas del usuario y genera un archivo .senda
    * */
    private class OnBackMisRutas extends AsyncTask<Boolean, ArrayList<MiRuta>, ArrayList<MiRuta>> {


        /*
        * metodo sobreescrito heredado del padre AsyncTask
        * es llamado al ejecutar la clase
        * se hace la conexion a la base y se buscan las rutas relacionadas con el usuario
        * */
        @Override
        protected ArrayList<MiRuta> doInBackground(Boolean... params) {
            ArrayList<MiRuta> rutas = null;
            try {
                Conexion conexion = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("numPeticion", "7");
                parametros.put("idUsuario", usuario.getIdUsuario());
                conexion.setParametros(parametros);
                conexion.executar(Conexion.metodoPeticion.POST);
                String respuesta = conexion.getRespuesta();
                if (Stuff.existe(respuesta)) {
                    rutas = Stuff.misRutas(respuesta);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return rutas;
        }

        /*
        * metodo sobreescrito heredado del padre AsyncTask
        * es llamado al terminar el metodo doInBackground
        *
        *
        * si las rutas son diferentes de nulo se genera un archivo senda y se cargar el RecyclerView
        * */
        @Override
        protected void onPostExecute(ArrayList<MiRuta> rutas) {
            super.onPostExecute(rutas);
            if (rutas != null) {
                Senda senda = new Senda(rutas);
                generarSENDA(senda, "some");
                cargarRecyclerView(rutas);
            }
        }
    }


}

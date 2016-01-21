package desarrollo.sip.senda.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.adaptadores.AdaptadorMisRutas;
import desarrollo.sip.senda.objetos.Conexion;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Stuff;
import desarrollo.sip.senda.objetos.Usuario;



public class MisRutas extends AppCompatActivity {

    private ListView listaMisRutas;
    private Usuario usuario;
    private TextView textUsuario;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_rutas);

        setTitle("Mis Rutas");

        this.usuario = (Usuario) getIntent().getExtras().get("usuario");
        iniciarWidgets();
        textUsuario.setText(usuario.getNombre());
        usuario.getfoto(foto);

        new OnBackMisRutas().execute();

    }

    public void iniciarWidgets(){
        textUsuario = (TextView)findViewById(R.id.labelUsario_MisRutas);
        foto = (ImageView)findViewById(R.id.imagenUsuario_MisRutas);
        listaMisRutas = (ListView)findViewById(R.id.listaMisRutas_MisRutas);

    }
    public void cargarLista(final ArrayList<MiRuta>rutas){
        AdaptadorMisRutas adaptadorMisRutas = new AdaptadorMisRutas(getBaseContext(),rutas);
        listaMisRutas.setAdapter(adaptadorMisRutas);
        listaMisRutas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activiadMapa(rutas.get(position));
            }
        });
    }

    private void activiadMapa(MiRuta miRuta){
        Intent i = new Intent(MisRutas.this,MapaActivity.class);
        Log.wtf("Intent",i.toString());
        i.putExtra("miRuta",(Parcelable) miRuta);
        startActivity(i);
    }


    private class OnBackMisRutas extends AsyncTask<Boolean,ArrayList<MiRuta>,ArrayList<MiRuta>> {

        @Override
        protected ArrayList<MiRuta> doInBackground(Boolean... params) {
            ArrayList<MiRuta> rutas = null;
            try {
                Conexion conexion = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                HashMap<String, String> parametros = new HashMap<>();
                parametros.put("numPeticion","7");
                parametros.put("idUsuario", usuario.getIdUsuario());
                conexion.setParametros(parametros);
                conexion.executar(Conexion.metodoPeticion.POST);
                String respuesta = conexion.getRespuesta();
                if(Stuff.existe(respuesta)){
                    rutas = Stuff.misRutas(respuesta);
                }
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            return rutas;
        }

        @Override
        protected void onPostExecute(ArrayList<MiRuta> rutas) {
            super.onPostExecute(rutas);
            if(rutas != null){
                cargarLista(rutas);
            }
        }
    }


}

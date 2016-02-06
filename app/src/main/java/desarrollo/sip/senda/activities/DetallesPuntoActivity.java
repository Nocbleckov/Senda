package desarrollo.sip.senda.activities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import desarrollo.sip.senda.OnBackTasks.OnBackColocarImagen;
import desarrollo.sip.senda.R;
import desarrollo.sip.senda.listener.ListenerLocation;
import desarrollo.sip.senda.objetos.Punto;
import desarrollo.sip.senda.objetos.Usuario;

public class DetallesPuntoActivity extends Activity {

    CardView cardView;
    TextView direccionCompleta,numeroCalle,colonia,localidad,municipio,estado,codigoPostal,estatus;
    ImageView puntoImagen;

    /*
    * metodo sobreescrito del padre Activity
    * obtiene el punto del intent y se lo asigna el punto de la clase
    * obtiene el usuario del intent y se lo asigna el usuario de la clase
    *
    * convoca el metodo iniWidgets,colocarInfo y cercania
    *
    *
    **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_punto);
        Punto punto = (Punto)getIntent().getExtras().get("punto");
        Usuario usuario = (Usuario)getIntent().getExtras().get("usuario");
        iniWidgets();
        colocarInfo(punto);
        cercania(punto,usuario.getIdUsuario());
    }


    /*
    * otiene los elementos relacionados con su id y se los asigna a los elementos correspondientes
    *
    * cardView,direccionCompleta,numbreCalle,colonia,localidad,municipio,estado,codigoPostal,estatus,puntoImagen
    *
    * */
    public void iniWidgets(){
        cardView = (CardView)findViewById(R.id.carViewPunto_DetallesPunto);
        direccionCompleta = (TextView)cardView.findViewById(R.id.textDireccionCompleta_DetallesPunto);
        numeroCalle = (TextView)cardView.findViewById(R.id.textNumeroCalle_DetallesPunto);
        colonia = (TextView)cardView.findViewById(R.id.textColonia_DetallesPunto);
        localidad = (TextView)cardView.findViewById(R.id.textLocalidad_DetallesPunto);
        municipio = (TextView)cardView.findViewById(R.id.textMunicipio_DetallesPunto);
        estado = (TextView)cardView.findViewById(R.id.textEstado_DetallesPunto);
        codigoPostal = (TextView)cardView.findViewById(R.id.textCodigoPostal_DetallesPunto);
        estatus = (TextView)cardView.findViewById(R.id.textEstatus_DetallesPunto);
        puntoImagen = (ImageView)cardView.findViewById(R.id.imagenPunto_DetallesPunto);
    }


    /*
    *
    * coloca la informacion de un objeto punto en los textview correspondientes
    *
    * */

    public void colocarInfo(Punto punto){
        direccionCompleta.setText(punto.getDireccion());
        numeroCalle.setText(punto.getCalle()+" No."+punto.getNumero());
        colonia.setText(punto.getColonia());
        localidad.setText(punto.getLocalidad());
        municipio.setText(punto.getMunicipio());
        estado.setText(punto.getEstado());
        codigoPostal.setText(punto.getCodigoPostal());
        estatus.setText(punto.getEstatus());
        new OnBackColocarImagen(punto,puntoImagen).execute();
    }

    /*
    * inicia el location manager que se encarga de encontrar tu posicion
    * */
    public void cercania(Punto punto,String idUsuario){

        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = new ListenerLocation(this,punto,DetallesPuntoActivity.this,getCurrentFocus(),idUsuario,locationManager);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 50, 0, listener);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 50, 0, listener);
        }

    }

}

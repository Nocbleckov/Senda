package desarrollo.sip.senda.activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import desarrollo.sip.senda.OnBackTasks.OnBackColocarImagen;
import desarrollo.sip.senda.R;
import desarrollo.sip.senda.objetos.Punto;

public class DetallesPuntoActivity extends Activity {

    CardView cardView;
    TextView direccionCompleta,numeroCalle,colonia,localidad,municipio,estado,codigoPostal,estatus;
    ImageView puntoImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_punto);
        Punto punto = (Punto)getIntent().getExtras().get("punto");
        iniWidgets();
        colocarInfo(punto);
    }

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

    public void colocarInfo(Punto punto){
        direccionCompleta.setText(punto.getDireccion());
        numeroCalle.setText(punto.getCalle()+" No."+punto.getNumero());
        colonia.setText(punto.getColonia());
        localidad.setText(punto.getLocalidad());
        municipio.setText(punto.getMunicipio());
        estado.setText(punto.getEstado());
        codigoPostal.setText(punto.getCodigoPostal());
        estatus.setText(punto.getEstatus());
        //new OnBackColocarImagen(punto,puntoImagen).execute();
    }

}

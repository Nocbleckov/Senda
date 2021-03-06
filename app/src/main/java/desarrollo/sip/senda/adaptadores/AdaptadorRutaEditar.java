package desarrollo.sip.senda.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.activities.EditarRutasActivity;
import desarrollo.sip.senda.listener.IniDataMap;
import desarrollo.sip.senda.objetos.Punto;

/**
 * Created by DESARROLLO on 23/01/16.
 */
public class AdaptadorRutaEditar extends ArrayAdapter<Punto>{

    Context context;
    ArrayList<Punto> puntos;
    GoogleMap mMap;

    /*
    *
    * Adaptador para el Listview RutaEditar para ser instanciado necesita un Context, un ArrayList Punto y un GoogleMap
    *
    * */

    public AdaptadorRutaEditar(Context context,ArrayList<Punto> puntos,GoogleMap mMap){
        super(context, -1, puntos);
        this.context = context;
        this.puntos = puntos;
        this.mMap = mMap;
    }

    /*
    * Este metodo es llamado cada vez que se muestra la row de u listview
    *
    * */

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.adaptador_ruta_editable,parent,false);

        TextView labelIdentificador = (TextView)rowView.findViewById(R.id.labelPunto_AdaptadorRutaEdit);
        ImageButton botonBorrar = (ImageButton)rowView.findViewById(R.id.botonBorrar_AdaptadorRutaEdit);
        ImageButton botonPunto = (ImageButton)rowView.findViewById(R.id.botonPunto_AdaptadorRutaEdit);

        labelIdentificador.setText(puntos.get(position).getDireccion());

        botonPunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IniDataMap.centrarCamara(puntos.get(position).getCoordenada(),18,48,mMap);
            }
        });

        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               puntos.remove(position);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

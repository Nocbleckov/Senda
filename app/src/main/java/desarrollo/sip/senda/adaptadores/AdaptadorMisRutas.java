package desarrollo.sip.senda.adaptadores;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import desarrollo.sip.senda.R;
import desarrollo.sip.senda.objetos.MiRuta;



/**
 * Created by DESARROLLO on 16/01/16.
 */
public class AdaptadorMisRutas extends ArrayAdapter<MiRuta> {

    Context context;
    ArrayList<MiRuta> rutas;

    public AdaptadorMisRutas(Context context, ArrayList<MiRuta> rutas) {
        super(context, -1, rutas);
        this.context = context;
        this.rutas = rutas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView = null;
        LinearLayout linearLayout = null;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.adaptador_mis_rutas, parent, false);
            linearLayout = (LinearLayout)rowView.findViewById(R.id.linearLytHorizontal_AdaptadorMisRutas);
            rowView.setTag(linearLayout);
        }else{
            linearLayout = (LinearLayout)convertView.getTag();
        }



        TextView identificador = (TextView)rowView.findViewById(R.id.labelIdentificador_AdaptadorMisRutas);
        TextView siglas = (TextView)linearLayout.findViewById(R.id.labelSiglas_AdaptadorMisRutas);
        TextView municipio = (TextView)linearLayout.findViewById(R.id.labelMunicipio_AdaptadorMisRutas);
        TextView estado = (TextView)linearLayout.findViewById(R.id.labelEstado_AdaptadorMisRutas);
        ImageView imagen = (ImageView)rowView.findViewById(R.id.imagenRegionMapa_AdaptadorMisRutas);

        rutas.get(position).getfoto(imagen);
        identificador.setText(rutas.get(position).getIdentificador());
        linearLayout.removeAllViews();
        siglas.setText("Siglas: " + rutas.get(position).getSiglas());
        municipio.setText("Municipio: " + rutas.get(position).getMunicipio());
        estado.setText("Estado: " + rutas.get(position).getEstado());
        linearLayout.addView(siglas);
        linearLayout.addView(municipio);
        linearLayout.addView(estado);
        rowView.setTag(linearLayout);

        return rowView;
    }
}

package desarrollo.sip.senda.adaptadores;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import desarrollo.sip.senda.OnBackTasks.OnBackColocarImagen;
import desarrollo.sip.senda.R;
import desarrollo.sip.senda.activities.MisRutas;
import desarrollo.sip.senda.objetos.MiRuta;

/**
 * Created by DESARROLLO on 27/01/16.
 */
public class AdaptadorRecyclerVMisRutas extends RecyclerView.Adapter<AdaptadorRecyclerVMisRutas.RutaViewHolder> {

    ArrayList<MiRuta> misRutas ;
    MisRutas actividadMisRutas;

    public AdaptadorRecyclerVMisRutas(ArrayList<MiRuta> misRutas,MisRutas actividadMisRutas){
        this.misRutas = misRutas;
        this.actividadMisRutas = actividadMisRutas;
    }

    @Override
    public RutaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carview_miruta,parent,false);
        RutaViewHolder rutaViewHolder = new RutaViewHolder(view);

        return rutaViewHolder;
    }

    @Override
    public void onBindViewHolder(RutaViewHolder holder,int position) {

        final int a = position;
        new OnBackColocarImagen(misRutas.get(a),holder.imagenRuta).execute();
        holder.labelIdentificador.setText(misRutas.get(position).getIdentificador());
        holder.labelsiglas.setText(misRutas.get(position).getSiglas());
        holder.labelMunicipio.setText(misRutas.get(position).getMunicipio());
        holder.labelEstado.setText(misRutas.get(position).getEstado());
        holder.botonVisitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadMisRutas.activiadMapa(misRutas.get(a));
            }
        });

    }

    @Override
    public int getItemCount() {
        return misRutas.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class RutaViewHolder extends RecyclerView.ViewHolder {

        CardView cvMiRuta;
        TextView labelsiglas,labelMunicipio,labelEstado,labelIdentificador;
        ImageView imagenRuta;
        Button botonVisitar;

        public RutaViewHolder(View itemView) {
            super(itemView);
            cvMiRuta = (CardView)itemView.findViewById(R.id.cardViewRuta_CVMiRuta);
            labelIdentificador = (TextView)cvMiRuta.findViewById(R.id.labelIdentificador_CVMiRuta);
            labelsiglas = (TextView)cvMiRuta.findViewById(R.id.labelSiglas_CVMiRuta);
            labelMunicipio = (TextView)cvMiRuta.findViewById(R.id.labelMunicipio_CVMiRuta);
            labelEstado = (TextView)cvMiRuta.findViewById(R.id.labelEstado_CVMiRuta);
            imagenRuta = (ImageView)cvMiRuta.findViewById(R.id.imagenMisRutas_CVMiRuta);
            botonVisitar = (Button)cvMiRuta.findViewById(R.id.botonVisitar_CVMiRuta);
        }
    }

}

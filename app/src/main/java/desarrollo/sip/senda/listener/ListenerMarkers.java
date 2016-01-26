package desarrollo.sip.senda.listener;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;

import desarrollo.sip.senda.activities.DetallesPuntoActivity;
import desarrollo.sip.senda.activities.MapaActivity;
import desarrollo.sip.senda.objetos.Punto;
import desarrollo.sip.senda.objetos.Usuario;

/**
 * Created by DESARROLLO on 03/12/15.
 */
public class ListenerMarkers implements OnMarkerClickListener {

    ArrayList<Punto> puntos;
    Context context;
    String  idUsuario;
    MapaActivity mapaActivity;

    public ListenerMarkers(Context context, Usuario usuario, MapaActivity mapaActivity){
        puntos = new ArrayList<>();
        this.context = context;
        this.idUsuario = usuario.getIdUsuario();
        this.mapaActivity = mapaActivity;
    }

    public ListenerMarkers(ArrayList<Punto> puntos, Context context){
        this.puntos = puntos;
        this.context = context ;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Punto punto = buscar(marker.getTitle());
        Intent i = new Intent(context,DetallesPuntoActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        i.putExtra("punto",(Parcelable)punto);
        i.putExtra("idUsuario",idUsuario);
        context.startActivity(i);
        return false;
    }

    public Punto buscar(String titulo){
        Punto temp = null;

        for(int i = 0;i<puntos.size();i++){
            Punto objetivo =  puntos.get(i);
            if(objetivo.getDireccion().equals(titulo)){
                return temp = objetivo;
            }
        }
        return temp;
    }

    public void agregarPuntos(ArrayList<Punto> puntos){
        this.puntos.addAll(puntos);
       // mapaActivity.setPuntosMapa(this.puntos);
    }

    public void setPuntos(ArrayList<Punto> puntos) {
        this.puntos = puntos;
    }

}

package desarrollo.sip.senda.listener;

import android.content.Context;
import android.os.Vibrator;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

import desarrollo.sip.senda.adaptadores.AdaptadorRutaEditar;
import desarrollo.sip.senda.objetos.Punto;

/**
 * Created by DESARROLLO on 23/01/16.
 */
public class ListerEditarRuta implements GoogleMap.OnMarkerClickListener {

    AdaptadorRutaEditar adaptadorRutaEditar;
    List<Punto> destinos;
    List<Punto> destinosOrg;
    Context context;

    /*
    * clase que implementa la interface OnMarkerClickListener
    * agrega objetos Punto al listview de EditarRuta
    *
    *
    * */

    public ListerEditarRuta(AdaptadorRutaEditar adaptadorRutaEditar, List<Punto> destinos, Context context,List<Punto> destinosOrg) {
        this.adaptadorRutaEditar = adaptadorRutaEditar;
        this.destinos = destinos;
        this.context = context;
        this.destinosOrg = destinosOrg;
    }


    /*
    * Se llama cada vez que se presiona un marker del mapa
    * */
    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.hideInfoWindow();
        Punto temp = esEste(marker.getTitle());
        agregarItems(temp, marker);

        return true;
    }

    /*
    * Busca la relacion punto con la marca y lo devuelve
    * */
    public Punto esEste(String title) {
        Punto temp = null;
        for (int i = 0; i < destinosOrg.size(); i++) {
            if (destinosOrg.get(i).getDireccion().equals(title)) {
                return destinosOrg.get(i);
            }
        }
        return temp;
    }

    /*
    * regresa un true o un false dependiendo si el punto ya existe en el arreglo o no
    * */

    public boolean existe(String direccion) {
        for (int i = 0; i < destinos.size(); i++) {
            if (destinos.get(i).getDireccion().equals(direccion)) {
                return true;
            }
        }
        return false;
    }

    /*
    * agrega los Puntos al arreglo si no existen
    * */
    public void agregarItems(Punto direccion, Marker marker) {
        Vibrator v = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        v.vibrate(33);
        if (direccion != null) {
            if (!existe(direccion.getDireccion())) {
                destinos.add(direccion);
                marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                adaptadorRutaEditar.notifyDataSetChanged();
                //Stuff.toastCsmCntx(context, "El punto se agrego a la ruta").show();
            } else {
                //Stuff.toastCsmCntx(context,"El punto ya ha sido Seleccionado").show();
            }
        }else {
            destinos.add(direccion);
            adaptadorRutaEditar.notifyDataSetChanged();
        }
    }

}

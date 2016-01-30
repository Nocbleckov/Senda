package desarrollo.sip.senda.objetos;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import desarrollo.sip.senda.activities.MisRutas;

/**
 * Created by DESARROLLO on 29/01/16.
 */
public class Senda implements Serializable{

    ArrayList<MiRuta> misRutas;

    public Senda(ArrayList<MiRuta> misRutas){
        this.misRutas = misRutas;
    }

    public ArrayList<MiRuta> getMisRutas() {
        return misRutas;
    }
}

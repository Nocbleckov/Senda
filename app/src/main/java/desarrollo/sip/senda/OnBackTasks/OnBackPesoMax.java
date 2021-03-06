package desarrollo.sip.senda.OnBackTasks;

import android.app.ProgressDialog;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import desarrollo.sip.senda.TileClasses.TileDwlManager;
import desarrollo.sip.senda.objetos.Conexion;

/**
 * Created by DESARROLLO on 30/01/16.
 */
public class OnBackPesoMax implements Runnable {

    private List<TileDwlManager.Coordenada> coordenadas = new ArrayList<>();
    private TileDwlManager tileDwlManager;
    private double peso;
    private TextView progressLabel;
    private ProgressDialog pd;

    /*
    *
    * clase que heredad de Runnable
    *
    * debe recibir un arreglo de Coordenas(es una clase interna), TileDwlManager, textView, progressDialog
    *
    * */

    public OnBackPesoMax(List<TileDwlManager.Coordenada> coordenadas, TileDwlManager tileDwlManager,TextView progressLabel,ProgressDialog pd) {
        this.coordenadas = coordenadas;
        this.tileDwlManager = tileDwlManager;
        this.progressLabel = progressLabel;
        this.pd = pd;
    }


    /*
    *
    * metodo sobreescrito que pertenece a la clase padre Runnable
    *
    * enviar el arreglo de coordenas al webservice para obtener
    * el peso de las imagenes antes de descargarla
    *
    * el codigo dentro de este metodo se lleba algo de tiempo (2-4 min)
    *
    * */
    @Override
    public void run() {
        Gson gson = new Gson();

        String jsonArray = gson.toJson(coordenadas);

        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("numPeticion", "9");
        parametros.put("coord", jsonArray);

        try {
            Conexion conexion = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
            conexion.setParametros(parametros);
            conexion.executar(Conexion.metodoPeticion.POST);
            String respuesta = conexion.getRespuesta();
            System.out.println(respuesta);
            peso = Double.parseDouble(respuesta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tileDwlManager.aumentarAcumulador(peso);
        cambiarUI();
    }

    /*
    *
    * este metodo vuevle al hilo principal de la aplicacion
    * y cambia el progressLabel por el valor obtenerdo
    *
    * */
    public void cambiarUI(){
        tileDwlManager.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
                String actual = new DecimalFormat("#.####").format(tileDwlManager.getAcumulador());
                progressLabel.setText("0 KB /" + actual + " KB");
            }
        });
        tileDwlManager.descargaTiles();
    }

}

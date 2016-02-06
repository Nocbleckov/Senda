package desarrollo.sip.senda.OnBackTasks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.HandlerThread;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.TileClasses.TileDwlManager;
import desarrollo.sip.senda.activities.MapaActivity;

/**
 * Created by DESARROLLO on 28/01/16.
 */
public class OnBackDwlTiles extends AsyncTask<String,String,String>{

    private List<TileDwlManager.Coordenada> coordenadas;

    private TileDwlManager tileDwlManager;
    private Dialog dialog;

    private ProgressBar progressBar;
    private TextView progressLabel;
    private ProgressDialog pd;

    private MapaActivity activity;


    /*
    *
    * clase que hereda de AsyncTask
    *
    * recibe un TileDwlManager, una Actividad MapaActivity,List de Coodenadas(Esta es una clase interna)
    *
    * */
    public OnBackDwlTiles(TileDwlManager tileDwlManager,MapaActivity activity,List<TileDwlManager.Coordenada> coordenadas){
        this.tileDwlManager = tileDwlManager;
        dialog = this.tileDwlManager.getDialog();
        this.activity = activity;
        this.coordenadas = coordenadas;
    }


    /*
    *
    * inicia la Clase OnBackPesoMax
    *
    * */
    public void peso(List<TileDwlManager.Coordenada> coordenadas){
        Thread hilo= new Thread(new OnBackPesoMax(coordenadas,tileDwlManager,progressLabel,pd));
        HandlerThread thread = new HandlerThread("hilo");
        hilo.start();
    }

    /*
    *
    * metodo que hereda de AsyncTask
    * es llamada cuando se executa la clase y antes del doInBackground
    *
    *
    * muestra el dialog
    * referencia la ProgressBar, ProgressLabel
    *
    * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.show();
        progressBar = (ProgressBar)dialog.findViewById(R.id.progresbarr_CustomDialogPB);
        progressLabel = (TextView)dialog.findViewById(R.id.labelProgreso_CustomDialogPB);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressLabel.setText("0 KB / Calculando... ");
        pd = ProgressDialog.show(activity, "Calculando informacion ..", "Esto puede tardar un par de minutos...", true);
    }

    /*
    *
    * metodo heredado de AsyncTask
    * convoca el metodo inciarTile del tileDwlManager
    *
    * */
    @Override
    protected String doInBackground(String... params) {
        tileDwlManager.iniciarTiles(tileDwlManager.getZoomInicial(), progressBar, progressLabel);
        peso(coordenadas);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}

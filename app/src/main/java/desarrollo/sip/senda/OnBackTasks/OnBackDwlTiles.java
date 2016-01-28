package desarrollo.sip.senda.OnBackTasks;

import android.app.Dialog;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.TileClasses.TileDwlManager;
import desarrollo.sip.senda.objetos.Stuff;

/**
 * Created by DESARROLLO on 28/01/16.
 */
public class OnBackDwlTiles extends AsyncTask<String,String,String>{

    private TileDwlManager tileDwlManager;
    private Dialog dialog;

    private ProgressBar progressBar;
    private TextView progressLabel;

    public OnBackDwlTiles(TileDwlManager tileDwlManager){
        this.tileDwlManager = tileDwlManager;
        dialog = this.tileDwlManager.getDialog();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar = (ProgressBar)dialog.findViewById(R.id.progresbarr_CustomDialogPB);
        progressLabel = (TextView)dialog.findViewById(R.id.labelProgreso_CustomDialogPB);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressLabel.setText("0 KB / Calculando... ");
        this.dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        tileDwlManager.descargaTiles(tileDwlManager.getZoomInicial(),progressBar,progressLabel);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        String maxS = new DecimalFormat("#.####").format((tileDwlManager.getAcumulador()/ 1024));
        progressLabel.setText("0 KB /" + maxS + " KB");
    }
}

package desarrollo.sip.senda.TileClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Tile;

import java.text.DecimalFormat;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import desarrollo.sip.senda.OnBackTasks.OnBackDwlTiles;
import desarrollo.sip.senda.R;
import desarrollo.sip.senda.objetos.Aristas;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Stuff;

/**
 * Created by DESARROLLO on 28/01/16.
 */
public class TileDwlManager  {

    private int zoomInicial;
    private int zoomFinal;
    private int zoomIncremento;

    private double actual;
    private double acumulador;


    private MiRuta ruta;
    private Dialog dialog;
    private Context context;

    private Activity activity;


    public TileDwlManager(MiRuta ruta,int zoomInicial,int zoomFinal,Context context,Dialog dialog,Activity activity){
        this.ruta = ruta;
        this.zoomInicial = zoomInicial;
        this.zoomFinal = zoomFinal;
        this.context = context;
        this.dialog = dialog;
        this.activity = activity;
        //iniciar(this.zoomInicial, progressBar, progressLabel);
        new OnBackDwlTiles(this).execute();
    }


    public void descargaTiles(int zoom,ProgressBar progressBar,TextView textView){

        Point[] puntos = convertAritasTile(ruta.getAristas(),zoom,256);

        for(int i = puntos[2].x;i<=puntos[0].x;i++){
            for(int y = puntos[0].y;y<=puntos[2].y;y++){
                Log.wtf("TILECOORD","x: "+i+","+"y: "+y);
                CustomTileDw temp = new CustomTileDw(i,y,zoom,context,this,progressBar,textView,dialog);
            }
        }

        if(zoomIncremento<zoomInicial+3){
            zoomIncremento = zoomIncremento + 1;
            descargaTiles(zoomIncremento,progressBar,textView);
        }
    }

    public Point[] convertAritasTile(Aristas aristas,int zoom,int tileSize){

        Point[] puntos = new Point[4];

        puntos[0] = Stuff.cnvLtLnToTileCoord(aristas.getAristaSD(),zoom,tileSize);
        puntos[1] = Stuff.cnvLtLnToTileCoord(aristas.getAristaSI(),zoom,tileSize);
        puntos[2] = Stuff.cnvLtLnToTileCoord(aristas.getAristaII(),zoom,tileSize);
        puntos[3] = Stuff.cnvLtLnToTileCoord(aristas.getAristaID(),zoom,tileSize);

        return puntos;
    }

    public void aumentarAcumulador(double peso){
        acumulador = acumulador + peso;
    }

    public void setActual(double actual) {
        this.actual = actual;
    }

    public void setAcumulador(double acumulador) {
        this.acumulador = acumulador;
    }

    public double getActual(double cantidad) {
        double temp = actual + cantidad;
        this.actual = temp;
        return actual;
    }

    public double getAcumulador() {
        return acumulador;
    }

    public int getZoomInicial() {
        return zoomInicial;
    }

    public Dialog getDialog() {
        return dialog;
    }
}

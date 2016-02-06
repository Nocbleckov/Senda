package desarrollo.sip.senda.TileClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import desarrollo.sip.senda.OnBackTasks.OnBackDwlTiles;
import desarrollo.sip.senda.OnBackTasks.OnBackPesoMax;
import desarrollo.sip.senda.activities.MapaActivity;
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

    private List<Coordenada> coordenadas;

    private MiRuta ruta;
    private Dialog dialog;
    private Context context;

    private MapaActivity mapaActivity;


    private ArrayList<CustomTileDw> customTiles;
    private int contador = 1;

    public TileDwlManager(MiRuta ruta,int zoomInicial,int zoomFinal,Context context,Dialog dialog,MapaActivity mapaActivity){
        customTiles = new ArrayList<>();
        coordenadas = new ArrayList<>();
        this.ruta = ruta;
        this.zoomInicial = zoomInicial;
        this.zoomFinal = zoomInicial + 2 ;
        this.context = context;
        this.dialog = dialog;
        this.mapaActivity = mapaActivity;
        //iniciar(this.zoomInicial, progressBar, progressLabel);
        new OnBackDwlTiles(this,mapaActivity,coordenadas).execute();
    }


    public void iniciarTiles(int zoom, ProgressBar progressBar, TextView textView){

        Point[] puntos = convertAritasTile(ruta.getAristas(),zoom,256);

        for(int i = puntos[2].x;i<=puntos[0].x;i++){
            for(int y = puntos[0].y;y<=puntos[2].y;y++){
                Log.wtf("TILECOORD","x: "+i+","+"y: "+y);
                CustomTileDw temp = new CustomTileDw(i,y,zoom,context,this,progressBar,textView,dialog);
                temp.setPosition(contador);
                contador = contador +1;
                Coordenada coordTemp  = new Coordenada(i,y,zoom);
                coordenadas.add(coordTemp);
                customTiles.add(temp);
            }
        }
        if(zoomInicial<zoomFinal){
            zoomInicial = zoomInicial + 1;
            iniciarTiles(zoomInicial, progressBar, textView);
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

    public int getZoomFinal() {
        return zoomFinal;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void descargaTiles(){
        for (CustomTileDw tiles:customTiles){
            tiles.iniciarDescarga();
        }
    }

    public Activity getActivity(){
        return mapaActivity;
    }

    public void clearCustomTiles(){
        customTiles.clear();
    }

    public int getMax(){
        return customTiles.size();
    }

    public class Coordenada {
        private int x;
        private int y;
        private int zoom;

        public Coordenada(int x,int y,int zoom){
            this.x = x;
            this.y = y;
            this.zoom = zoom;
        }
    }
}

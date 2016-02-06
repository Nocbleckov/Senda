package desarrollo.sip.senda.TileClasses;

import android.content.res.AssetManager;
import android.os.Environment;

import com.google.android.gms.maps.model.Tile;
import com.google.android.gms.maps.model.TileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by DESARROLLO on 04/01/16.
 */
public class CustomMapTileProvider implements TileProvider {

    private static final int TILE_WIDTH = 256;
    private static final int TILE_HEIGHT = 256;
    private static final int BUFFER_SIZE = 16 * 1024;


    /*
    *
    * Clase que heredad de TileProvider
    *
    * su funcion es obtener imagenes del dispositivo, con estas
    * instaciar clases Tile, que recibe el tamaño de la imagen y la misma
    * para posteriormente mostrarlas en el mapa
    *
    *
    * */

    public CustomMapTileProvider(){
    }


    /*
    *
    * este metodo sobreescrito pertenece al padre
    *
    * invoca el metodo readTileImage
    *
    * y debuelve un Tile
    *
    * */
    @Override
    public Tile getTile(int x, int y, int zoom) {

        byte[] imagen = readTileImage(x,y,zoom);
        return imagen == null ? null : new Tile(TILE_WIDTH,TILE_HEIGHT,imagen);


    }


    /*
    *
    * obtiene los archivos de las direcciones map/zoom/x/y.jpg
    *
    * si existen las combierte un arreglo de bytes[] y la devuelve
    *
    * */

    public byte[] readTileImage(int x, int y,int zoom){

        InputStream in = null;
        ByteArrayOutputStream buffer = null;

        try{


            File file = new File(Environment.getExternalStorageDirectory()+"/"+getTileFilename(x,y,zoom));

            if(file.exists()){
                in = new FileInputStream(file.getAbsolutePath());
            }else{
                //in = assetManager.open("no_imagen/no_imagen.jpeg");
            }


            buffer = new ByteArrayOutputStream();

            int nRead;
            byte[] data = new byte[BUFFER_SIZE];

            while ((nRead = in.read(data,0,BUFFER_SIZE))!= -1){
                buffer.write(data,0,nRead);
            }
            buffer.flush();

            return  buffer.toByteArray();

        }catch (Exception e){
            return null;
        }finally {
            if(in != null)try{in.close();}catch(Exception e){}
            if(buffer != null)try{buffer.close();}catch(Exception e){}
        }

    }


    /*
    *
    * construye el nombre de la ruta con la posision x,y y el zoom
    *
    * */

    private String getTileFilename(int x, int y, int zoom) {
        return "map/" + zoom + '/' + x + '/' + y + ".jpeg";
    }

}

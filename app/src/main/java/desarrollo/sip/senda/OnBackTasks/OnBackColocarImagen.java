package desarrollo.sip.senda.OnBackTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.abstractClass.WithImage;
import desarrollo.sip.senda.objetos.Punto;
import desarrollo.sip.senda.objetos.Stuff;

/**
 * Created by DESARROLLO on 26/01/16.
 */
public class OnBackColocarImagen extends AsyncTask<String, Bitmap, Bitmap> {

    private WithImage withImage;
    private ImageView imageView;
    private static final int BUFFER_SIZE = 16 * 1024;

    /*
    *
    * Clase que heredad de AsyncTask
    *
    * recive una Clase que herede de WithImage y un ImageView
    *
    * su funcion es obtener la imagen de la url y colocarla en ImageView
    *
    * si existe la imagen la carga y la coloca en el ImageView
    *
    * */

    public OnBackColocarImagen(WithImage withImage,ImageView imageView) {
        this.withImage = withImage;
        this.imageView = imageView;
    }


    /*
    *
    * carga la imagen del objeto File (contiene la direccion
    * y la devuelve como un bitmap
    *
    * */
    private Bitmap cargarFoto(File archivo) {
        Bitmap bm = null;
        ByteArrayOutputStream buffer = null;
        try {
            InputStream fIs = new FileInputStream(archivo);
            buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[BUFFER_SIZE];
            while ((nRead = fIs.read(data, 0, BUFFER_SIZE)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] imagenCode = buffer.toByteArray();
            int tamaño = imagenCode.length;
            bm = BitmapFactory.decodeByteArray(imagenCode, 0, tamaño);
            fIs.close();
            buffer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    /*
    *
    * convierte el Bitmap en un arreglo de bytes
    *
    * */
    private byte[] bitmapToByteArray(Bitmap bm){
        byte[] temp = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        temp = outputStream.toByteArray();

        return  temp;
    }

    /*
    *
    * crea el archivo(Es una imagen) con el bitmap
    *
    * */
    private Bitmap crearFoto(File archivo) {
        Bitmap bm = null;
        try {
            archivo.createNewFile();

            String ruta = withImage.getRutaImagen();

            InputStream iS = new URL(ruta).openStream();
            bm = BitmapFactory.decodeStream(iS);
            FileOutputStream fOs = new FileOutputStream(archivo, false);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOs);
            ObjectOutputStream oOS = new ObjectOutputStream(fOs);

            oOS.close();
            fOs.close();
        } catch (Exception e) {
            e.printStackTrace();
            archivo.delete();
        }
        return bm;
    }

    /*
    *
    * metodo sobreescrito del padre AsyncTask
    *
    * si existe la imagen la carga
    * si no existe la descarga de la url y la guarda
    *
    * en ambos casos de obtiene un bitmap que es el que mada al OnPostExecute
    *
    * */
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bm = null;

        try {

            if (withImage.existeImagen()) {
                bm = cargarFoto(withImage.getRutaInternaImagen());
            } else {
                File a = withImage.getRutaInternaImagen();
                bm = crearFoto(a);
            }
            withImage.setImagenCode(bitmapToByteArray(bm));

        }catch (Exception e){
            e.printStackTrace();
        }
        return bm;
    }


    /*
    *
    * asigna el bitmap a el ImageView
    *
    * */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            imageView.setBackgroundResource(R.drawable.degradado);
        }else{
            Bitmap bm = BitmapFactory.decodeResource(imageView.getResources(),R.drawable.errorconexion);
            imageView.setImageBitmap(bm);
        }
    }
}

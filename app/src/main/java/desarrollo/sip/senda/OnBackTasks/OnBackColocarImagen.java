package desarrollo.sip.senda.OnBackTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public OnBackColocarImagen(WithImage withImage,ImageView imageView) {
        this.withImage = withImage;
        this.imageView = imageView;
    }

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
            fIs.close();
            buffer.close();
            int tamaño = imagenCode.length;
            bm = BitmapFactory.decodeByteArray(imagenCode, 0, tamaño);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    private byte[] bitmapToByteArray(Bitmap bm){
        byte[] temp = null;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        temp = outputStream.toByteArray();

        return  temp;
    }

    private Bitmap crearFoto(File archivo) {
        Bitmap bm = null;
        try {
            archivo.createNewFile();
            InputStream iS = new URL(withImage.getRutaImagen()).openStream();
            bm = BitmapFactory.decodeStream(iS);
            FileOutputStream fOs = new FileOutputStream(archivo, false);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOs);
            ObjectOutputStream oOS = new ObjectOutputStream(fOs);

            oOS.close();
            fOs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bm = null;
        if (withImage.getImagenCode() == null) {
            if (withImage.existeImagen()) {
                bm = cargarFoto(withImage.getRutaInternaImagen());
            } else {
                bm = crearFoto(withImage.getRutaInternaImagen());
            }
            withImage.setImagenCode(bitmapToByteArray(bm));
        }else{
            int tamaño = withImage.getImagenCode().length;
            bm = BitmapFactory.decodeByteArray(withImage.getImagenCode(),0,tamaño);
        }
        return bm;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
        }
    }
}

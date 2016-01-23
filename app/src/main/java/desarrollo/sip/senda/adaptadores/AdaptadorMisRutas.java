package desarrollo.sip.senda.adaptadores;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import desarrollo.sip.senda.R;
import desarrollo.sip.senda.objetos.MiRuta;
import desarrollo.sip.senda.objetos.Stuff;


/**
 * Created by DESARROLLO on 16/01/16.
 */
public class AdaptadorMisRutas extends ArrayAdapter<MiRuta> {

    Context context;
    ArrayList<MiRuta> rutas;

    public AdaptadorMisRutas(Context context, ArrayList<MiRuta> rutas) {
        super(context, -1, rutas);
        this.context = context;
        this.rutas = rutas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = null;
        LinearLayout linearLayout = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.adaptador_mis_rutas, parent, false);
        linearLayout = (LinearLayout) rowView.findViewById(R.id.linearLytHorizontal_AdaptadorMisRutas);
        rowView.setTag(linearLayout);


        TextView identificador = (TextView) rowView.findViewById(R.id.labelIdentificador_AdaptadorMisRutas);
        TextView siglas = (TextView) linearLayout.findViewById(R.id.labelSiglas_AdaptadorMisRutas);
        TextView municipio = (TextView) linearLayout.findViewById(R.id.labelMunicipio_AdaptadorMisRutas);
        TextView estado = (TextView) linearLayout.findViewById(R.id.labelEstado_AdaptadorMisRutas);
        ImageView imagen = (ImageView) rowView.findViewById(R.id.imagenRegionMapa_AdaptadorMisRutas);

        String id = rutas.get(position).getPuntoCentro().latitude+","+rutas.get(position).getPuntoCentro().longitude;
        new OnBackImagen(rutas.get(position).getRutaImagen(), imagen,id,rutas.get(position)).execute();
        identificador.setText(rutas.get(position).getIdentificador());
        linearLayout.removeAllViews();
        siglas.setText("Siglas: " + rutas.get(position).getSiglas());
        municipio.setText("Municipio: " + rutas.get(position).getMunicipio());
        estado.setText("Estado: " + rutas.get(position).getEstado());
        linearLayout.addView(siglas);
        linearLayout.addView(municipio);
        linearLayout.addView(estado);
        rowView.setTag(linearLayout);


        return rowView;
    }

    private class OnBackImagen extends AsyncTask<String, Void, Bitmap> {

        String url;
        ImageView imageView;
        String id;
        MiRuta miRuta;
        private static final int BUFFER_SIZE = 16 * 1024;

        public OnBackImagen(String url, ImageView imageView, String id,MiRuta miRuta) {
            this.url = url;
            this.imageView = imageView;
            this.id = id;
            this.miRuta = miRuta;
        }

        private Bitmap cargarFoto(File archivo) {
            Bitmap bm = null;
            ByteArrayOutputStream buffer= null;
            try {
                InputStream fIs = new FileInputStream(archivo);
                buffer = new ByteArrayOutputStream();
                int nRead;
                byte[] data = new byte[BUFFER_SIZE];
                while((nRead = fIs.read(data,0,BUFFER_SIZE))!=-1){
                    buffer.write(data,0,nRead);
                }
                buffer.flush();
                byte[] imagenCode = buffer.toByteArray();
                fIs.close();
                buffer.close();
                int tamaño = imagenCode.length;
                bm = BitmapFactory.decodeByteArray(imagenCode,0,tamaño);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bm = null;
            if(miRuta.getFoto() == null) {
                File archivo = new File(Stuff.crearRuta("/senda/data") + "/Imagen" + id + ".png");
                if (!archivo.exists()) {
                    try {
                        archivo.createNewFile();
                        InputStream iS = new URL(url).openStream();
                        bm = BitmapFactory.decodeStream(iS);
                        FileOutputStream fOs = new FileOutputStream(archivo, false);
                        bm.compress(Bitmap.CompressFormat.PNG, 100, fOs);
                        ObjectOutputStream oOS = new ObjectOutputStream(fOs);
                        oOS.close();
                        fOs.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    bm = cargarFoto(archivo);
                }
                miRuta.setFoto(bm);
            }else{
                bm = miRuta.getFoto();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}

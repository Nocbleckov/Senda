package desarrollo.sip.senda.abstractClass;

import java.io.File;
import java.io.Serializable;

import desarrollo.sip.senda.objetos.Stuff;

/**
 * Created by DESARROLLO on 26/01/16.
 */
public abstract class WithImage implements Serializable {

    protected String longitud,latitud, id,rutaImagen;
    protected byte[] imagenCode;
    private File archivo;

    public WithImage(){
        archivo = new File(Stuff.crearRuta("/senda/data")+"/"+id+latitud+","+longitud);
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public boolean existeImagen(){
        if(archivo.exists()){
            return true;
        }else{
            return false;
        }
    }
    public File getRutaInternaImagen(){
        if(archivo.exists()){
            return  archivo;
        }else{
            return null;
        }
    }

    public void setImagenCode(byte[] imagenCode) {
        this.imagenCode = imagenCode;
    }

    public String getId() {
        return id;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public byte[] getImagenCode() {
        return imagenCode;
    }
}

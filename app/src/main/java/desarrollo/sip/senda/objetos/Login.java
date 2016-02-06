package desarrollo.sip.senda.objetos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


/**
 * Created by DESARROLLO on 14/01/16.
 */
public class Login implements Serializable {

    private byte[] bytesUsario ;
    private byte[] bytesPass;
    private byte[] bytesSerial;
    private static final long serialVersionUID = 12L;
    private byte[] bytesUsuario;


    /*
    *
    * Esta clase es la que se guardara en el .login
    *
    * Para ser creada necesita un nombre de usuario, contraseña , serial y usuario
    *
    * cuando los recive los combirete en arreglo de bytes y los codifica
    *
    * */

    public Login(String usuario,String pass,Long serial,Usuario usuObj){
        bytesUsario = codificar(usuario.getBytes(Charset.forName("UTF-8")));
        bytesPass = codificar(pass.getBytes(Charset.forName("UTF-8")));
        bytesSerial = codificar(longToBytes(serial));
        bytesUsario = Stuff.toByteArray(usuObj);
    }


    /*
    *
    * codifica el arreglo de bytes
    *
    * */
    private byte[] codificar(byte[] arreglo){

        for(int i = 0;i<arreglo.length;i++){
            BigInteger temp = new BigInteger(Integer.toBinaryString(arreglo[i]+1),2);
            arreglo[i] = temp.byteValue();
        }

        return arreglo;
    }


    /*
    * Convierte un Long o long  en un arreglo de bytes y lo devuelve
    * */

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate((Long.SIZE / Byte.SIZE) + 10);
        buffer.putLong(x).array();
        return buffer.array();
    }


    /*
    *
    * recupera el Long o long de un arreglo de bytes y lo devuelve
    * */
    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate((Long.SIZE/Byte.SIZE) + 10);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }


    /*
    * devuelve el long recuperdado del arreglo de bytes
    * */
    public Long getID(){
        try{
            Long id = bytesToLong(bytesSerial);
            return id;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    /*
    *
    * recupera el string usuario del arreglo de bytes y lo devuelve
    *
    * */
    public String getUsario(){
        try {
            String usuario = new String(bytesUsario, "UTF-8");
            return usuario;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /*
    *
    *
    * recupera el String contraseña del arreglo de bytes y lo devuelve
    *
    * */
    public String getPass(){
        try{
            String pass = new String(bytesPass,"UTF-8");
            return pass;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /*
    *
    * recupera el usuario del arreglo de bytes y lo devuelve
    *
    * */
    public Usuario getUsuarioObject(){
        Usuario temp = null;
        ByteArrayInputStream bIs = null;
        ObjectInputStream oIs = null;
        try{
            bIs = new ByteArrayInputStream(bytesUsario);
            oIs = new ObjectInputStream(bIs);
            temp =(Usuario)oIs.readObject();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(bIs!=null)try{bIs.close();}catch(Exception e){}
            if(oIs!=null)try{oIs.close();}catch(Exception e){}
        }
        return temp;
    }

}

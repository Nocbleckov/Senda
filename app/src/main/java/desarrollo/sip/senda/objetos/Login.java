package desarrollo.sip.senda.objetos;

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
    private Usuario usuObj;


    public Login(String usuario,String pass,Long serial,Usuario usuObj){
        bytesUsario = codificar(usuario.getBytes(Charset.forName("UTF-8")));
        bytesPass = codificar(pass.getBytes(Charset.forName("UTF-8")));
        bytesSerial = codificar(longToBytes(serial));
        this.usuObj = usuObj;
    }
    private byte[] codificar(byte[] arreglo){

        for(int i = 0;i<arreglo.length;i++){
            BigInteger temp = new BigInteger(Integer.toBinaryString(arreglo[i]+1),2);
            arreglo[i] = temp.byteValue();
        }

        return arreglo;
    }

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate((Long.SIZE / Byte.SIZE) + 10);
        buffer.putLong(x).array();
        return buffer.array();
    }

    public long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate((Long.SIZE/Byte.SIZE) + 10);
        buffer.put(bytes);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public Long getID(){
        try{
            Long id = bytesToLong(bytesSerial);
            return id;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }

    public String getUsario(){
        try {
            String usuario = new String(bytesUsario, "UTF-8");
            return usuario;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String getPass(){
        try{
            String pass = new String(bytesPass,"UTF-8");
            return pass;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Usuario getUsuObj() {
        return usuObj;
    }
}

package desarrollo.sip.senda.objetos;

/**
 * Created by DESARROLLO on 16/01/16.
 */
public class MiRuta {
    private String identificador,siglas,municipio,estado,idRuta;
    //private byte[] imagenCode;

    public MiRuta(String siglas,String municipio,String estado,String idRuta){
        this.idRuta = idRuta;
        this.siglas = siglas;
        this.municipio = municipio;
        this.estado = estado;

        this.identificador = idRuta+siglas+estado;
    }

    public String getIdentificador() {
        return identificador;
    }

    public String getSiglas() {
        return siglas;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getEstado() {
        return estado;
    }
}

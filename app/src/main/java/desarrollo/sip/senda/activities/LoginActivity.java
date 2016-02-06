package desarrollo.sip.senda.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import desarrollo.sip.senda.R;
import desarrollo.sip.senda.objetos.Conexion;
import desarrollo.sip.senda.objetos.Login;
import desarrollo.sip.senda.objetos.Stuff;
import desarrollo.sip.senda.objetos.Usuario;
import desarrollo.sip.senda.serial.NumSerie;



public class LoginActivity extends AppCompatActivity {

    private EditText editUsuario,editPass;
    private Button botonIngresar,botonLimpiar;
    private CheckedTextView chekedLogueo;
    private Dialog dialog;
    private ConnectivityManager cm;
    private NetworkInfo miWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Bienvenido");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        iniciarWidgets();

        cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);


    }


    /*
    Inicia los Widgets Cuando se Llama
    */
    public void iniciarWidgets(){
        editUsuario = (EditText)findViewById(R.id.editNombre_Login);
        editPass = (EditText)findViewById(R.id.editPass_Login);
        botonIngresar =(Button)findViewById(R.id.botonIngresar_Login);
        botonLimpiar = (Button)findViewById(R.id.botonLimpiar_Login);
        chekedLogueo = (CheckedTextView)findViewById(R.id.checkLogueo_Login);
    }


    /*
    * Metodo OnClick se llama cuando es presionado el CheckBox y crea un cuado de dialogo
    * */
    public void onClickMarca(View view) {
        if(!chekedLogueo.isChecked()){
            chekedLogueo.setChecked(true);
            dialog = Stuff.crearAlrtDialogCst(this,"Confirmación","Usted necesita un archivo tipo '.login' ó '.senda' en la carpeta '/senda/data' para poder usar esta opción ",aceptar(),cancelar());
            dialog.show();

        }else{
            chekedLogueo.setChecked(false);
        }
    }


    /*
    * Metodo OnClick del boton Ingresar al ser presionado inicia la conexion a la base de datos para obtener el usuario
    * */

    public void onClickIngresar(View view){

        miWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if(miWifi.getState() == NetworkInfo.State.CONNECTED){

            String usuario = editUsuario.getText().toString().trim();
            String pass = editPass.getText().toString().trim();
            if (!usuario.equalsIgnoreCase("") || !pass.equalsIgnoreCase("")) {
                new onBackLogin(this).execute(usuario, pass, "123456789");
            } else {
                Toast.makeText(this, "Los campos de usuario y contraseña no pueden estar vacíos", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No esta conectado a una Red", Toast.LENGTH_SHORT).show();
        }

    }


    /*
    * Metodo onClick coloca los edit text en blanco
    * */
    public void onClickLimpiar(View view){
        editPass.setText("");
        editUsuario.setText("");
    }

    public View.OnClickListener aceptar(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Login login = cargarArchivo();
                if(login != null){
                    if(Stuff.decodeLong(login.getID()) == NumSerie.id){
                        Usuario usuario = login.getUsuarioObject();
                        iniciarSiguiente(usuario);
                    }
                }else{
                    Toast.makeText(getBaseContext(),"No se encontro un archivo valido",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }



    public View.OnClickListener cancelar(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getBaseContext(),"Cancelado",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        };
    }

    /*
    * Al ser llamado inicia la siguiente actividad que es la lista de Rutas
    * */
    public void iniciarSiguiente(Usuario usuario){
        Intent i = new Intent(LoginActivity.this,MisRutas.class);
        i.putExtra("usuario",(Parcelable)usuario);
        startActivity(i);
    }


    /*
    * Genera un archivo binario de la clase Login
    * */
    public void generarLogueo(Login login,String nombre){
        try {
            File archivo = new File(Stuff.crearRuta("/senda/data") + "/personal.login");
            archivo.createNewFile();
            FileOutputStream fOs = new FileOutputStream(archivo,false);
            ObjectOutputStream oOs = new ObjectOutputStream(fOs);
            oOs.writeObject(login);
            oOs.close();
            fOs.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            Toast.makeText(this,"Se a generado su Archivo de logueo offline",Toast.LENGTH_SHORT).show();
        }
    }

    /*
    * Carga el archivo de la ruta raiz/senda/data/persona.login ,lo castea en un Objeto Login y lo devuelve
    * */
    public Login cargarArchivo(){
        Login login = null;
        try{
            File archivo = new File(Environment.getExternalStorageDirectory()+"/senda/data"+"/personal.login");
            FileInputStream iS = new FileInputStream(archivo);
            ObjectInputStream oiS = new ObjectInputStream(iS);
            login = (Login)oiS.readObject();
            oiS.close();
            iS.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  login;
    }


    /*
    * Clase interna al ser llamada inicia una peticion al servidor para buscar y traer los datos del usuario
    * */

    private class onBackLogin extends AsyncTask<String,Boolean,Usuario>{

        private Context context;

        /*
        * Contructor de la clase interna necesita un Context
        * */

        public onBackLogin(Context context){
            this.context = context;
        }


        /*
        * Metodo sobreescrito, pertenece al padre AsyncTask
        * inicia una tarea en segundo plano
        * aqui se inicia la conexion al servido
        * */
        @Override
        protected Usuario doInBackground(String... params) {
            String nick = params[0];
            String pass = params[1];
            Usuario usuario = null ;

            HashMap<String,String> parametros = new HashMap<>();
            parametros.put("numPeticion","1");
            parametros.put("nick",nick);
            parametros.put("pass",pass);

            try {
                Conexion con = new Conexion("http://sysintpro.com.mx/PruebasApiGoogle/WSSApp/Peticiones.php");
                con.setParametros(parametros);
                con.executar(Conexion.metodoPeticion.POST);
                String respuesta = con.getRespuesta();
                if(Stuff.existe(respuesta)){
                    usuario = new Usuario(respuesta);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return usuario;
        }

        /*
        * Metodo sobreescrito, pertenece al padre AsyncTask
        * se executa cuando acaba el metodo doInBackground
        *
        * En caso de que el usuario obtenido en la conexion no sea nulo
        * instancia la clase login pasandole los parametros de usuario.nick, usuario.pass, NumSerie.id y el mismo objeto usuario
        * invoca el metodo generarLogue y le pasa el objeto Login y el nick del usuario
        *
        * En caso de que el usuario sea nulo muestra un mensaje Toast
        *
        * */

        @Override
        protected void onPostExecute(Usuario usuario) {
            super.onPostExecute(usuario);
            if(usuario != null){;
                Login login = new Login(usuario.getNick(),usuario.getPass(), NumSerie.id,usuario);
                generarLogueo(login,usuario.getNick());
                iniciarSiguiente(usuario);
            }else{
                Toast.makeText(context,"El usuario o la contraseña son incorrectas", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

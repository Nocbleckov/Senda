package desarrollo.sip.senda.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

    EditText editUsuario,editPass;
    Button botonIngresar,botonLimpiar;
    CheckedTextView chekedLogueo;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        iniciarWidgets();

    }

    public void iniciarWidgets(){
        editUsuario = (EditText)findViewById(R.id.editNombre_Login);
        editPass = (EditText)findViewById(R.id.editPass_Login);
        botonIngresar =(Button)findViewById(R.id.botonIngresar_Login);
        botonLimpiar = (Button)findViewById(R.id.botonLimpiar_Login);
        chekedLogueo = (CheckedTextView)findViewById(R.id.checkLogueo_Login);
    }

    public void onClickMarca(View view) {
        if(!chekedLogueo.isChecked()){
            chekedLogueo.setChecked(true);
            dialog = Stuff.crearAlrtDialogCst(this,"Confirmación","Usted necesita un archivo tipo '.login' ó '.senda' en la carpeta '/senda/data' para poder usar esta opción ",aceptar(),cancelar());
            dialog.show();

        }else{
            chekedLogueo.setChecked(false);
            Toast.makeText(this,"Se marca",Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickIngresar(View view){
        String usuario = editUsuario.getText().toString().trim();
        String pass = editPass.getText().toString().trim();
        if(!usuario.equalsIgnoreCase("") || !pass.equalsIgnoreCase("")) {
            new onBackLogin(this).execute(usuario, pass, "123456789");
        }else{
            Toast.makeText(this,"Los campos de usuario y contraseña no pueden estar vacíos",Toast.LENGTH_SHORT).show();
        }
    }

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
                        Usuario usuario = login.getUsuObj();
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

    public void iniciarSiguiente(Usuario usuario){
        Intent i = new Intent(LoginActivity.this,MisRutas.class);
        i.putExtra("usuario",(Parcelable)usuario);
        startActivity(i);
    }

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

    private class onBackLogin extends AsyncTask<String,Boolean,Usuario>{

        private Context context;

        public onBackLogin(Context context){
            this.context = context;
        }

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

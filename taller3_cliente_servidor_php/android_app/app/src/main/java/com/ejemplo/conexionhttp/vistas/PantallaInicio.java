package com.ejemplo.conexionhttp.vistas;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ejemplo.conexionhttp.R;
import com.ejemplo.conexionhttp.controladores.ConexionHttpPostServer;
import com.ejemplo.conexionhttp.controladores.Mensaje;
import com.ejemplo.conexionhttp.controladores.NameValuePair;
import com.ejemplo.conexionhttp.datos.Usuario;

import java.util.ArrayList;

public class PantallaInicio extends AppCompatActivity {

    private EditText campoEmail;
    private EditText campoPassword;
    private Button btnLogin;
    private Button botonCancelar;
    private ConexionHttpPostServer conexionServidor;
    public static Usuario fulanito;
    private ProgressDialog barraDeprogreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_inicio);

        campoEmail = findViewById(R.id.campoEmail);
        campoPassword = findViewById(R.id.campoClave);
        btnLogin = findViewById(R.id.btnIniciarSesion);
        botonCancelar = findViewById(R.id.btnCancelar);

        // Botón "Registrate": Abre la pantalla en MODO REGISTRO
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PantallaInicio.fulanito = null; // Asegurar que no hay sesión activa
                Intent intento = new Intent(PantallaInicio.this, PantallaCrudUsuario.class);
                intento.putExtra("modo", "registro");
                startActivity(intento);
            }
        });

        // Botón "Entrar": Inicia sesión en el servidor PHP
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verificarDatos()) {
                    String correo = campoEmail.getText().toString().trim();
                    String password = campoPassword.getText().toString().trim();

                    conexionServidor = new ConexionHttpPostServer();
                    TareaLoginEnSegunPlano tareaDeLogin = new TareaLoginEnSegunPlano();
                    tareaDeLogin.execute(correo, password);
                }
            }
        });
    }

    private void mostrarDatosDeUsuarioEnOtraActividad() {
        if (fulanito != null) {
            Intent intento = new Intent(PantallaInicio.this, PantallaListado.class);
            intento.putExtra("sesion", fulanito);
            startActivity(intento);
        }
    }

    private Object iniciarSesion(String correo, String password) {
        fulanito = null;
        ArrayList<NameValuePair> listaDeParametros = new ArrayList<>();
        listaDeParametros.add(new NameValuePair("accion", "login"));
        listaDeParametros.add(new NameValuePair("email", correo));
        listaDeParametros.add(new NameValuePair("psw", password));

        String respuestaDelServidorEnJSon = null;
        try {
            respuestaDelServidorEnJSon = conexionServidor.conexionConElServidor(
                    listaDeParametros,
                    ConexionHttpPostServer.direccionDelServidor
            );
            System.out.println("Respuesta Servidor: " + respuestaDelServidorEnJSon);
        } catch (Exception error) {
            error.printStackTrace();
            return null;
        }

        if (respuestaDelServidorEnJSon != null && !respuestaDelServidorEnJSon.trim().isEmpty()) {
            Gson formatoJson = new Gson();
            try {
                Usuario usuarioResp = formatoJson.fromJson(respuestaDelServidorEnJSon, Usuario.class);
                if (usuarioResp != null && usuarioResp.getEmail() != null) {
                    return usuarioResp;
                } else {
                    return formatoJson.fromJson(respuestaDelServidorEnJSon, Mensaje.class);
                }
            } catch (Exception e) {
                return formatoJson.fromJson(respuestaDelServidorEnJSon, Mensaje.class);
            }
        } else {
            return null;
        }
    }

    private boolean verificarDatos() {
        String correo = campoEmail.getText().toString().trim();
        String password = campoPassword.getText().toString().trim();

        if (correo.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su correo", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Debe ingresar su contraseña", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    class TareaLoginEnSegunPlano extends AsyncTask<String, String, String> {
        String correo;
        String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            barraDeprogreso = new ProgressDialog(PantallaInicio.this);
            barraDeprogreso.setMessage("Conectando con el servidor...");
            barraDeprogreso.setIndeterminate(false);
            barraDeprogreso.setCancelable(false);
            barraDeprogreso.show();
        }

        @Override
        protected String doInBackground(String... parametros) {
            correo = parametros[0];
            password = parametros[1];
            Object resp = iniciarSesion(correo, password);

            if (resp != null) {
                if (resp instanceof Usuario) {
                    fulanito = (Usuario) resp;
                    return "OK";
                } else if (resp instanceof Mensaje) {
                    return ((Mensaje) resp).getMensaje();
                } else {
                    return "Error desconocido";
                }
            } else {
                return "Acceso Denegado: credenciales incorrectas o falla de servidor";
            }
        }

        @Override
        protected void onPostExecute(String resp) {
            if (barraDeprogreso != null && barraDeprogreso.isShowing()) {
                barraDeprogreso.dismiss();
            }

            if ("OK".equals(resp)) {
                Toast.makeText(PantallaInicio.this, "¡Bienvenido " + fulanito.getNombre() + "!", Toast.LENGTH_SHORT).show();
                mostrarDatosDeUsuarioEnOtraActividad();
            } else {
                Toast.makeText(PantallaInicio.this, resp, Toast.LENGTH_LONG).show();
            }
        }
    }
}

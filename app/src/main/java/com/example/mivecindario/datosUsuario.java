package com.example.mivecindario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mivecindario.Modelos.Hogar;
import com.example.mivecindario.Modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class datosUsuario extends AppCompatActivity {

    EditText nomUsuario, apeUsuario, corUsuario, passUsuario, telUsuario, dirUsuario;

    String nmUsuario,apUsuario;

    Toolbar toolbar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_usuario);

        nomUsuario=findViewById(R.id.txt_nombreUsuarioM);
        apeUsuario=findViewById(R.id.txt_apellidoUsuarioM);
        corUsuario=findViewById(R.id.txt_correoUsuarioM);
        passUsuario=findViewById(R.id.txt_passUsuarioM);
        telUsuario=findViewById(R.id.txt_telefonoUsuarioM);
        dirUsuario=findViewById(R.id.txt_direccionUsuarioM);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        incializarFirebase();

        cargarPreferencias();
    }



    public void modificarDatosUsuario(View v) {

        String nombre = nomUsuario.getText().toString();
        String apellido = apeUsuario.getText().toString();
        String correo = corUsuario.getText().toString();
        String password = passUsuario.getText().toString();
        String telefono = telUsuario.getText().toString();
        String direccion = dirUsuario.getText().toString();

        if (nombre.equals("") || apellido.equals("") || correo.equals("") || password.equals("") || telefono.equals("") ||
                direccion.equals("")) {
            validacion();
        }
        else{
            databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
                Usuario u = new Usuario();
                String uidUsuario;
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()) {
                        Usuario usuario = objSnapshot.getValue(Usuario.class);
                        if (nmUsuario.equals(usuario.getNombre()) && apUsuario.equals(usuario.getApellido())) {
                            uidUsuario = usuario.getUid();
                            u.setUid(uidUsuario);
                            u.setNombre(nomUsuario.getText().toString());
                            u.setApellido(apeUsuario.getText().toString());
                            u.setCorreo(corUsuario.getText().toString());
                            u.setPassword(passUsuario.getText().toString());
                            u.setTelefono(telUsuario.getText().toString());
                            u.setDireccion(dirUsuario.getText().toString());
                            u.setTipo(usuario.getTipo());
                            u.setHogar(usuario.getHogar());
                        }
                    }
                    databaseReference.child("Usuario").child(uidUsuario).setValue(u);
                    limpiarCajas();
                    SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferecias.edit();
                    editor.putString("nombreUsuario", u.getNombre());
                    editor.putString("apellidoUsuario", u.getApellido());
                    editor.apply();
                    Intent intent = new Intent(datosUsuario.this,datosUsuario.class);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    private void incializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    } // Inicializar Firebase

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_usuario,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.verVecindario:{
                Intent intent = new Intent(this,verVecindario.class);
                startActivity(intent);
                break;
            }

            case R.id.editarHogar:{
                Intent intent = new Intent(this,editarHogar.class);
                startActivity(intent);
                break;
            }

            case R.id.datosUsuario:{
                Intent intent = new Intent(this,datosUsuario.class);
                startActivity(intent);
                break;
            }

            case R.id.cerrarSesion:{
                SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferecias.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this,IniciarSesion.class);
                startActivity(intent);
            }
            default:break;

        }
        return true;
    }

    private void limpiarCajas() {

        nomUsuario.setText("");
        apeUsuario.setText("");
        corUsuario.setText("");
        passUsuario.setText("");
        telUsuario.setText("");
        dirUsuario.setText("");
    }

    private void validacion() {

        String nombre = nomUsuario.getText().toString();
        String apellido = apeUsuario.getText().toString();
        String correo = corUsuario.getText().toString();
        String password = passUsuario.getText().toString();
        String telefono = telUsuario.getText().toString();
        String direccion = dirUsuario.getText().toString();


        if (nombre.equals("")){
            nomUsuario.setError("Requerido");
        }
        else if (apellido.equals("")){
            apeUsuario.setError("Requerido");
        }
        else if (correo.equals("")){
            corUsuario.setError("Requerido");
        }
        else if (password.equals("")){
            passUsuario.setError("Requerido");
        }
        else if (telefono.equals("")){
            telUsuario.setError("Requerido");
        }
        else if (direccion.equals("")){
            dirUsuario.setError("Requerido");
        }


    }

    private void cargarPreferencias() {
        SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        nmUsuario = preferecias.getString("nombreUsuario","NoSesion");
        apUsuario = preferecias.getString("apellidoUsuario","NoSesion");
        toolbar.setSubtitle(nmUsuario+" "+apUsuario);
    }

}

package com.example.mivecindario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mivecindario.Modelos.Administrador;
import com.example.mivecindario.Modelos.Usuario;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class IniciarSesion extends AppCompatActivity  {


    EditText editTextCorreo, editTextPass;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);

        editTextCorreo = findViewById(R.id.txt_correo);
        editTextPass = findViewById(R.id.txt_pass);


        //Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        cargarPreferencias();
        incializarFirebase();

        }

        private void cargarPreferencias() {

            SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
            if (preferencias.contains("nombreAdmin") && preferencias.contains("apellidoAdmin")){
                Intent intent = new Intent(this,Inicio.class);
                startActivity(intent);
            }else if(preferencias.contains("nombreUsuario") && preferencias.contains("apellidoUsuario")){
                Intent intent = new Intent(this,InicioUsuario.class);
                startActivity(intent);
            }
        }

        private void incializarFirebase() {
            FirebaseApp.initializeApp(this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }



        public void IniciarSesion ( View v) {

            databaseReference.child("Administrador").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){

                        Administrador admin = objSnapshot.getValue(Administrador.class);
                        String correoAdmin = editTextCorreo.getText().toString();
                        String passAdmin = editTextPass.getText().toString();

                        if (correoAdmin.equals(admin.getCorreo()) && passAdmin.equals(admin.getPassword())){
                            Intent intentAdmin = new Intent(IniciarSesion.this, Inicio.class);
                            SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencias.edit();
                            editor.putString("nombreAdmin", admin.getNombre());
                            editor.putString("apellidoAdmin",admin.getApellido());
                            editor.commit();
                            startActivity(intentAdmin);
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Usuario user = objSnapshot.getValue(Usuario.class);
                        String correo = editTextCorreo.getText().toString();
                        String pass = editTextPass.getText().toString();

                        if (correo.equals(user.getCorreo()) && pass.equals(user.getPassword())){
                            Intent intent = new Intent (IniciarSesion.this, InicioUsuario.class);
                            SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferencias.edit();
                            editor.putString("nombreUsuario", user.getNombre());
                            editor.putString("apellidoUsuario", user.getApellido());
                            editor.putString("tipoUsuario", user.getTipo());
                            editor.commit();
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }







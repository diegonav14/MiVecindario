package com.example.mivecindario;

import android.content.Intent;
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

        incializarFirebase();

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
                            intentAdmin.putExtra("nombreAdmin",admin.getNombre());
                            intentAdmin.putExtra("apellidoAdmin",admin.getApellido());
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
                            intent.putExtra("nombreUsuario",user.getNombre());
                            intent.putExtra("apellidoUsuario",user.getApellido());
                            intent.putExtra("tipoUsuario", user.getTipo());
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







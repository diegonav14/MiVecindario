package com.example.mivecindario.Administrador;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.mivecindario.IniciarSesion;
import com.example.mivecindario.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class InicioAdmin extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        cargarPreferencias();
        incializarFirebase();
    }



    private void cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String nmAdmin = preferencias.getString("nombreAdmin","NoSesion");
        String apAdmin = preferencias.getString("apellidoAdmin","NoSesion");
        toolbar.setSubtitle(nmAdmin+" "+apAdmin);
    }


    public void irUsuario (View v){
        Intent intent = new Intent(InicioAdmin.this, ingresarUsuarios.class);
        startActivity(intent);
    }

    public void irVecindario (View v){
        Intent intent = new Intent(InicioAdmin.this, ingresarVecindarios.class);
        startActivity(intent);
    }

    public void irHogares (View v){
        Intent intent = new Intent(InicioAdmin.this, agregarHogares.class);
        startActivity(intent);
    }

    public void irMapa (View v){
        Intent intent = new Intent(InicioAdmin.this, ingresarMapaVecindario.class);
        startActivity(intent);
    }

    private void incializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.cerrarSesionAdmin:{
                SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this, IniciarSesion.class);
                startActivity(intent);
                finish();
            }
            default:break;

        }
        return true;
    }

}

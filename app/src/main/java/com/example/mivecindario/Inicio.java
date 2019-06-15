package com.example.mivecindario;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Inicio extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

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
        Intent intent = new Intent(Inicio.this, Usuarios.class);
        startActivity(intent);
    }

    public void irVecindario (View v){
        Intent intent = new Intent(Inicio.this, Vecindarios.class);
        startActivity(intent);
    }

    public void irHogares (View v){
        Intent intent = new Intent(Inicio.this, Hogares.class);
        startActivity(intent);
    }

    public void irMapa (View v){
        Intent intent = new Intent(Inicio.this, MapaVecindario.class);
        startActivity(intent);
    }

    private void incializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.icon_add:{

                break;
            }

            case R.id.icon_save:{
                break;
            }

            case R.id.icon_delete:{
                break;
            }

            case R.id.icon_login:{
                SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferencias.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(this,IniciarSesion.class);
                startActivity(intent);
            }
            default:break;

        }
        return true;
    }

}

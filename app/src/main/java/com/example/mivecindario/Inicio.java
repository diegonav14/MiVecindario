package com.example.mivecindario;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


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

        recibirDatos();

        incializarFirebase();
    }

    private void recibirDatos() {
        Bundle datoExtra = getIntent().getExtras();
        String nmAdmin = datoExtra.getString("nombreAdmin");
        String apAdmin = datoExtra.getString("apellidoAdmin");
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

            }
            default:break;

        }
        return true;
    }

}

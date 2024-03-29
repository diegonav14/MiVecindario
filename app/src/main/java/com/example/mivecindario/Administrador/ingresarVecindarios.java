package com.example.mivecindario.Administrador;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.mivecindario.IniciarSesion;
import com.example.mivecindario.Modelos.Vecindario;
import com.example.mivecindario.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ingresarVecindarios extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    EditText nombreVec, direccionVec;

    Vecindario vecindarioSeleccionado;

    ListView listV_vecindario;

    private List<Vecindario> listaVecindario = new ArrayList<Vecindario>();
    ArrayAdapter<Vecindario> arrayAdapterVecindario;

    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_vecindario);



        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nombreVec = findViewById(R.id.txt_nombreVecindario);
        direccionVec = findViewById(R.id.txt_direccionVecindario);

        cargarPreferencias();
        inicializarFirebase();
        listarDatos();

        listV_vecindario=findViewById(R.id.lv_datosVecindario);

        listV_vecindario.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                vecindarioSeleccionado = (Vecindario) parent.getItemAtPosition(position);
                nombreVec.setText(vecindarioSeleccionado.getNombre());
                direccionVec.setText(vecindarioSeleccionado.getDireccion());
            }
        });


}

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatos() {
        databaseReference.child("Vecindario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaVecindario.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Vecindario v = objSnapshot.getValue(Vecindario.class);
                    listaVecindario.add(v);
                    arrayAdapterVecindario = new ArrayAdapter<Vecindario>(ingresarVecindarios.this, android.R.layout.simple_list_item_1, listaVecindario);
                    listV_vecindario.setAdapter(arrayAdapterVecindario);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Listar los los usuarios
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        getSupportActionBar().setTitle("MiVecindario");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String nombre= nombreVec.getText().toString();
        String direccion= direccionVec.getText().toString();



        switch (item.getItemId()){

            case R.id.icon_add:{

                if (nombre.equals("") || direccion.equals("")){
                    validacion();
                }
                else {
                    Vecindario v = new Vecindario();
                    v.setUid(UUID.randomUUID().toString());
                    v.setNombre(nombre);
                    v.setDireccion(direccion);
                    databaseReference.child("Vecindario").child(v.getUid()).setValue(v);
                    Toast.makeText(this, "Agregar", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
                break;
            }

            case R.id.icon_save:{
                Vecindario v = new Vecindario ();
                v.setUid(vecindarioSeleccionado.getUid());
                v.setNombre(nombreVec.getText().toString().trim());
                v.setDireccion(direccionVec.getText().toString().trim());
                databaseReference.child("Vecindario").child(v.getUid()).setValue(v);
                Toast.makeText(this,"Guardar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }

            case R.id.icon_delete:{
                Vecindario v = new Vecindario ();
                v.setUid(vecindarioSeleccionado.getUid());
                databaseReference.child("Vecindario").child(v.getUid()).removeValue();
                Toast.makeText(this,"Eliminar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }

            case R.id.icon_login:{
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


    private void limpiarCajas() {

        nombreVec.setText("");
        direccionVec.setText("");

    }

    private void validacion() {

        String nombre = nombreVec.getText().toString();
        String direccion = direccionVec.getText().toString();


        if (nombre.equals("")) {
            nombreVec.setError("Requerido");
        } else if (direccion.equals("")) {
            direccionVec.setError("Requerido");
        }
    }

    private void cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String nmAdmin = preferencias.getString("nombreAdmin","NoSesion");
        String apAdmin = preferencias.getString("apellidoAdmin","NoSesion");
        toolbar.setSubtitle(nmAdmin+" "+apAdmin);
    }

}

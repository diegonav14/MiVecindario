package com.example.mivecindario.Administrador;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mivecindario.IniciarSesion;
import com.example.mivecindario.Modelos.Hogar;
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

public class agregarHogares extends AppCompatActivity {

    Toolbar toolbar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Hogar hogarSeleccionado;

    ListView listV_hogar;

    Spinner spinnerVecindario;

    EditText dirHogar, nomHogar, comHogar;

    private List<Hogar> listaHogares = new ArrayList<Hogar>();
    ArrayAdapter<Hogar> arrayAdapterHogar;
    private List<String> listaVecindario = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_hogar);

        inicializarFirebase();

        dirHogar=findViewById(R.id.txt_direccionHogar);
        nomHogar=findViewById(R.id.txt_nombreHogar);
        comHogar=findViewById(R.id.txt_comentarioHogar);
        spinnerVecindario = findViewById(R.id.spVecindarioHogar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaVecindario.add("Vecindario");

        ArrayAdapter<CharSequence> adapterSpinnerV = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, listaVecindario);

        spinnerVecindario.setAdapter(adapterSpinnerV);

        listV_hogar=findViewById(R.id.lv_datosHogar);

        listarDatos();
        cargarPreferencias();
        spinnerVecindario();

        listV_hogar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                hogarSeleccionado = (Hogar) parent.getItemAtPosition(position);
                dirHogar.setText(hogarSeleccionado.getDireccion());
                nomHogar.setText(hogarSeleccionado.getNombre());
                comHogar.setText(hogarSeleccionado.getComentario());
                for (int i = 0; i <= spinnerVecindario.getCount(); i++){
                    if (spinnerVecindario.getItemAtPosition(i).toString().equals(hogarSeleccionado.getVecindario().getNombre())){
                        spinnerVecindario.setSelection(i);
                    }
                }
            }


            });


        }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDatos() {
        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaHogares.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Hogar h = objSnapshot.getValue(Hogar.class);
                    listaHogares.add(h);
                    arrayAdapterHogar = new ArrayAdapter<Hogar>(agregarHogares.this, android.R.layout.simple_list_item_1, listaHogares);
                    listV_hogar.setAdapter(arrayAdapterHogar);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Listar los los usuarios

    private void spinnerVecindario (){
        databaseReference.child("Vecindario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Vecindario v = objSnapshot.getValue(Vecindario.class);
                    listaVecindario.add(v.getNombre());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        getSupportActionBar().setTitle("MiVecindario");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case R.id.icon_add:{

                databaseReference.child("Vecindario").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String nombre= nomHogar.getText().toString();
                        String direccion= dirHogar.getText().toString();
                        String comentario= comHogar.getText().toString();
                        String vecindario = spinnerVecindario.getSelectedItem().toString();

                        if (nombre.equals("") || direccion.equals("") || comentario.equals("") || vecindario.equals("Vecindario")){
                            validacion();
                        }
                        else {
                            Hogar h = new Hogar();
                            h.setUid(UUID.randomUUID().toString());
                            h.setNombre(nombre);
                            h.setDireccion(direccion);
                            h.setComentario(comentario);
                            for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                                Vecindario v = objSnapshot.getValue(Vecindario.class);
                                if (vecindario.equals(v.getNombre())){
                                    h.setVecindario(v);
                                }
                            }
                            databaseReference.child("Hogar").child(h.getUid()).setValue(h);
                            Toast.makeText(agregarHogares.this, "Agregado", Toast.LENGTH_LONG).show();
                            limpiarCajas();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            }

            case R.id.icon_save:{

                databaseReference.child("Vecindario").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Hogar h = new Hogar();
                        h.setUid(hogarSeleccionado.getUid());
                        h.setNombre(nomHogar.getText().toString().trim());
                        h.setDireccion(dirHogar.getText().toString().trim());
                        h.setComentario(comHogar.getText().toString().trim());
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                            Vecindario v = objSnapshot.getValue(Vecindario.class);
                            if (spinnerVecindario.getSelectedItem().toString().equals(h.getNombre())){
                                h.setVecindario(v);
                            }
                        }
                        databaseReference.child("Hogar").child(h.getUid()).setValue(h);
                        Toast.makeText(agregarHogares.this,"Guardar", Toast.LENGTH_LONG).show();
                        limpiarCajas();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            }

            case R.id.icon_delete:{
                Hogar h = new Hogar();
                h.setUid(hogarSeleccionado.getUid());
                databaseReference.child("Hogar").child(h.getUid()).removeValue();
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

    private void validacion() {

        String nombre = nomHogar.getText().toString();
        String direccion = dirHogar.getText().toString();
        String comentario = comHogar.getText().toString();


        if (nombre.equals("")) {
            nomHogar.setError("Requerido");
        } else if (direccion.equals("")) {
            dirHogar.setError("Requerido");
        } else if (comentario.equals("")) {
        comHogar.setError("Requerido");
        }else if (spinnerVecindario.getSelectedItem().toString().equals("Vecindario")) {
            Toast.makeText(agregarHogares.this,"Debe seleccionar el vecindario", Toast.LENGTH_LONG).show();
        }

    }

    private void limpiarCajas() {

        nomHogar.setText("");
        dirHogar.setText("");
        comHogar.setText("");
        spinnerVecindario.setSelection(0);

    }

    private void cargarPreferencias() {
        SharedPreferences preferencias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String nmAdmin = preferencias.getString("nombreAdmin","NoSesion");
        String apAdmin = preferencias.getString("apellidoAdmin","NoSesion");
        toolbar.setSubtitle(nmAdmin+" "+apAdmin);
    }
}

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mivecindario.Modelos.Evento;
import com.example.mivecindario.Modelos.Hogar;
import com.example.mivecindario.Modelos.Usuario;
import com.example.mivecindario.Modelos.Vecindario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InicioUsuario extends AppCompatActivity {

    Toolbar toolbar;

    String tipoUsuario,nmUsuario,apUsuario;

    EditText comentarioEvento;

    Button btnEvento;

    ListView lv_Eventos;

    private List<Evento> listaEventos = new ArrayList<Evento>();
    ArrayAdapter<Evento> arrayAdapteEvento;

    Spinner tipoEvento;

    TextView tpUsuario;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_usuario);

        tpUsuario= findViewById(R.id.txt_tipoUsuario);
        comentarioEvento = findViewById(R.id.txt_comentarioEvento);
        tipoEvento = findViewById(R.id.spEvento);
        btnEvento = findViewById(R.id.btnEventos);
        lv_Eventos = findViewById(R.id.lv_eventos);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.TipoEvento, android.R.layout.simple_spinner_dropdown_item);

        tipoEvento.setAdapter(adapterSpinner);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicializarFirebase();
        cargarPreferencias();
        listarDatos();
        tiposUsuarios();

    }



    private void tiposUsuarios(){

        switch (tipoUsuario){

            case "Presidente junta vecinos":{
                tpUsuario.setText(tipoUsuario);
                btnEvento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingresarEvento();
                    }
                });
                break;
            }

            case "Jefe de hogar":{
                tpUsuario.setText(tipoUsuario);
                btnEvento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingresarEvento();
                    }
                });
                break;
            }

            case "Miembro de hogar":{
                tpUsuario.setText(tipoUsuario);
                btnEvento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingresarEvento();
                    }
                });
                break;


            }

        }
    }

    private void ingresarEvento() {
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String comentario = comentarioEvento.getText().toString();
                String tipo = tipoEvento.getSelectedItem().toString();

                if (comentario.equals("") || tipo.equals("Tipo")){
                    validacion();
                }
                else{
                    Evento e = new Evento();
                    e.setUid(UUID.randomUUID().toString());
                    e.setComentario(comentario);
                    e.setTipo(tipo);
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Usuario u = objSnapshot.getValue(Usuario.class);
                        if (nmUsuario.equals(u.getNombre()) && apUsuario.equals(u.getApellido())){
                            e.setUsuario(u);
                        }
                    }
                    databaseReference.child("Evento").child(e.getUid()).setValue(e);
                    Toast.makeText(InicioUsuario.this, "Evento agregado", Toast.LENGTH_LONG).show();
                    limpiarCajas();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void listarDatos() {
        databaseReference.child("Evento").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaEventos.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Evento e = objSnapshot.getValue(Evento.class);
                    listaEventos.add(e);
                    arrayAdapteEvento = new ArrayAdapter<Evento>(InicioUsuario.this, android.R.layout.simple_list_item_1, listaEventos);
                    lv_Eventos.setAdapter(arrayAdapteEvento);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Listar los los usuarios

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

    private void validacion() {

        String comentario = comentarioEvento.getText().toString();

        if (comentario.equals("")) {
            comentarioEvento.setError("Requerido");
        } else if (tipoEvento.getSelectedItem().toString().equals("Tipo")) {
            Toast.makeText(this,"Debe seleccionar el tipo", Toast.LENGTH_LONG).show();
        }

    }

    private void limpiarCajas() {

        comentarioEvento.setText("");
        tipoEvento.setSelection(0);

    }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void cargarPreferencias() {
        SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        nmUsuario = preferecias.getString("nombreUsuario","NoSesion");
        apUsuario = preferecias.getString("apellidoUsuario","NoSesion");
        tipoUsuario = preferecias.getString("tipoUsuario","NoSesion");
        toolbar.setSubtitle(nmUsuario+" "+apUsuario);
    }

}

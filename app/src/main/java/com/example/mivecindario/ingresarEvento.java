package com.example.mivecindario;

import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mivecindario.Modelos.Evento;
import com.example.mivecindario.Modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ingresarEvento extends AppCompatActivity {

    Toolbar toolbar;

    String tipoUsuario,nmUsuario,apUsuario;

    EditText comentarioEvento,fechaEvento;

    Button btnEvento,btnAsistir;

    ListView lv_Eventos,lv_asistentes;



    private List<Evento> listaEventos = new ArrayList<Evento>();
    private  List<Usuario> listaAsistentes = new ArrayList<Usuario>();
    ArrayAdapter<Evento> arrayAdapteEvento;


    ArrayAdapter<Usuario> arrayAdapteAsistente;

    Spinner tipoEvento;

    TextView txt_tipoEvento,txt_comentarioEvento,txt_usuarioEvento,txt_fechaEvento;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Evento eventoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_evento);

        comentarioEvento = findViewById(R.id.txt_comentarioEvento);
        fechaEvento = findViewById(R.id.txt_fechaEvento);
        tipoEvento = findViewById(R.id.spEvento);
        btnEvento = findViewById(R.id.btnEventos);
        btnAsistir = findViewById(R.id.btnAsistir);
        lv_Eventos = findViewById(R.id.lv_eventos);
        lv_asistentes = findViewById(R.id.lv_asistentes);
        txt_comentarioEvento = findViewById(R.id.txt_comentarioEventoSeleccionado);
        txt_tipoEvento = findViewById(R.id.txt_tipoEventoSeleccionado);
        txt_usuarioEvento = findViewById(R.id.txt_usuarioEventoSeleccionado);
        txt_fechaEvento = findViewById(R.id.txt_fechaEventoSeleccionado);



        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.TipoEvento, android.R.layout.simple_spinner_dropdown_item);

        tipoEvento.setAdapter(adapterSpinner);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inicializarFirebase();
        cargarPreferencias();
        listarDatos();
        tiposUsuarios();

        lv_Eventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                eventoSeleccionado = (Evento) parent.getItemAtPosition(position);
                txt_comentarioEvento.setText(eventoSeleccionado.getComentario());
                txt_tipoEvento.setText(eventoSeleccionado.getTipo());
                txt_fechaEvento.setText(eventoSeleccionado.getFecha());
                txt_usuarioEvento.setText(eventoSeleccionado.getUsuario().getNombre()+" "+eventoSeleccionado.getUsuario().getApellido());

                for (int i = 0 ; i < eventoSeleccionado.getListaAsistentes().size(); i++){
                    if (eventoSeleccionado.getListaAsistentes().get(i).getNombre().equals(nmUsuario) && eventoSeleccionado.getListaAsistentes().get(i).getApellido().equals(apUsuario)){
                        arrayAdapteAsistente = new ArrayAdapter<Usuario>(ingresarEvento.this, android.R.layout.simple_list_item_1,eventoSeleccionado.getListaAsistentes());
                        lv_asistentes.setAdapter(arrayAdapteAsistente);
                        btnAsistir.setVisibility(View.GONE);
                    }else{
                        btnAsistir.setVisibility(View.VISIBLE);
                        arrayAdapteAsistente = new ArrayAdapter<Usuario>(ingresarEvento.this, android.R.layout.simple_list_item_1,eventoSeleccionado.getListaAsistentes());
                        lv_asistentes.setAdapter(arrayAdapteAsistente);
                    }
                }
            }
        });
    }

    public void participar(View v){
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    if (nmUsuario.equals(u.getNombre()) && apUsuario.equals(u.getApellido())){
                        eventoSeleccionado.getListaAsistentes().add(u);
                    }
                    databaseReference.child("Evento").child(eventoSeleccionado.getUid()).setValue(eventoSeleccionado);
                    arrayAdapteAsistente = new ArrayAdapter<Usuario>(ingresarEvento.this, android.R.layout.simple_list_item_1,eventoSeleccionado.getListaAsistentes());
                    lv_asistentes.setAdapter(arrayAdapteAsistente);
                    btnAsistir.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void tiposUsuarios(){

        switch (tipoUsuario){

            case "Presidente junta vecinos":{
                btnEvento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingresarEvento();
                    }
                });
                break;
            }

            case "Jefe de hogar":{
                btnEvento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ingresarEvento();
                    }
                });
                break;
            }

            case "Miembro de hogar":{
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
                String fecha = fechaEvento.getText().toString();

                if (comentario.equals("") || tipo.equals("Tipo")){
                    validacion();
                }
                else{
                    Evento e = new Evento();
                    e.setUid(UUID.randomUUID().toString());
                    e.setComentario(comentario);
                    e.setTipo(tipo);
                    e.setFecha(fecha);
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Usuario u = objSnapshot.getValue(Usuario.class);
                        if (nmUsuario.equals(u.getNombre()) && apUsuario.equals(u.getApellido())){
                            listaAsistentes.add(u);
                            e.setListaAsistentes(listaAsistentes);
                            e.setUsuario(u);
                        }
                    }
                    databaseReference.child("Evento").child(e.getUid()).setValue(e);
                    Toast.makeText(ingresarEvento.this, "Evento agregado", Toast.LENGTH_LONG).show();
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
                    arrayAdapteEvento = new ArrayAdapter<Evento>(ingresarEvento.this, android.R.layout.simple_list_item_1, listaEventos);
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
               finish();
            }
            default:break;

        }
        return true;
    }

    private void validacion() {

        String comentario = comentarioEvento.getText().toString();
        String fecha = fechaEvento.getText().toString();

        if (comentario.equals("")) {
            comentarioEvento.setError("Requerido");
        } else if (tipoEvento.getSelectedItem().toString().equals("Tipo")) {
            Toast.makeText(this,"Debe seleccionar el tipo", Toast.LENGTH_LONG).show();
        }else if (fecha.equals("")) {
            fechaEvento.setError("Requerido");
        }
    }

    private void limpiarCajas() {

        comentarioEvento.setText("");
        fechaEvento.setText("");
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

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
import com.example.mivecindario.Modelos.Hogar;
import com.example.mivecindario.Modelos.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class editarHogar extends AppCompatActivity {

    Toolbar toolbar;

    EditText comentarioHogar, direccionHogar, nombreHogar;

    String nmUsuario,apUsuario;



    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_hogar);

        inicializarFirebase();


        comentarioHogar = findViewById(R.id.txt_comentarioUsuarioHogar);
        direccionHogar = findViewById(R.id.txt_direccionHogarUsuario);
        nombreHogar = findViewById(R.id.txt_nombreUsuarioHogar);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cargarPreferencias();

    }

    public void modificarHogar( View v){
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {

            String comentario = comentarioHogar.getText().toString();
            String direccion = direccionHogar.getText().toString();
            String nombre = nombreHogar.getText().toString();
            String idusuario;
            String hogarUsuario;


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Hogar h = new Hogar();
                if (comentario.equals("") || direccion.equals("") || nombre.equals("")){
                    validacion();
                }
                else{
                    for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                        Usuario u = objSnapshot.getValue(Usuario.class);
                        if (nmUsuario.equals(u.getNombre()) && apUsuario.equals(u.getApellido())){
                            hogarUsuario = u.getHogar().getUid();
                            idusuario = u.getUid();
                            h.setNombre(nombre);
                            h.setDireccion(direccion);
                            h.setComentario(comentario);
                            h.setUid(hogarUsuario);
                            h.setVecindario(u.getHogar().getVecindario());
                        }
                    }
                    databaseReference.child("Hogar").child(hogarUsuario).setValue(h);
                    databaseReference.child("Usuario").child(idusuario).child("hogar").setValue(h);
                    limpiarCajas();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


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

    private void cargarPreferencias() {
        SharedPreferences preferecias = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        nmUsuario = preferecias.getString("nombreUsuario","NoSesion");
        apUsuario = preferecias.getString("apellidoUsuario","NoSesion");
        toolbar.setSubtitle(nmUsuario+" "+apUsuario);
    }

    private void validacion() {

        String comentario = comentarioHogar.getText().toString();
        String nombre = nombreHogar.getText().toString();
        String direccion = direccionHogar.getText().toString();

        if (comentario.equals("")) {
            comentarioHogar.setError("Requerido");
        } else if (nombre.equals("")){
            nombreHogar.setError("Requerido");
        }else if (direccion.equals("")){
            direccionHogar.equals("Requerido");
        }

    }

    private void limpiarCajas() {

        comentarioHogar.setText("");
        nombreHogar.setText("");
        direccionHogar.setText("");

    }

    private void inicializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


}

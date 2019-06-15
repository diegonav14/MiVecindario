package com.example.mivecindario.Administrador;


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

import com.example.mivecindario.Modelos.Hogar;
import com.example.mivecindario.Modelos.Usuario;
import com.example.mivecindario.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ingresarUsuarios extends AppCompatActivity {

    private List<Usuario> listaUsuario = new ArrayList<Usuario>();
    private List<String> listaHogar = new ArrayList<String>();
    ArrayAdapter<Usuario> arrayAdapterUsuario;

    EditText nomUsuario, apeUsuario, corUsuario, passUsuario, telUsuario, dirUsuario;
    ListView listV_usuarios;

    Spinner spinnerTipo,spinnerHogar;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Usuario usuarioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);

            listaHogar.add("Hogar");

            nomUsuario=findViewById(R.id.txt_nombreUsuario);
            apeUsuario=findViewById(R.id.txt_apellidoUsuario);
            corUsuario=findViewById(R.id.txt_correoUsuario);
            passUsuario=findViewById(R.id.txt_passUsuario);
            telUsuario=findViewById(R.id.txt_telefonoUsuario);
            dirUsuario=findViewById(R.id.txt_direccionUsuario);
            spinnerTipo = findViewById(R.id.sptipo);
            spinnerHogar = findViewById(R.id.spHogar);


            ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,R.array.Tipo, android.R.layout.simple_spinner_dropdown_item);
            ArrayAdapter<CharSequence> adapterSpinnerHogar = new ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item, listaHogar);

            spinnerTipo.setAdapter(adapterSpinner);
            spinnerHogar.setAdapter(adapterSpinnerHogar);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            listV_usuarios=findViewById(R.id.lv_datosUsuarios);

            incializarFirebase();
            listarDatos();
            spinnerHogar();

            listV_usuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    usuarioSeleccionado = (Usuario) parent.getItemAtPosition(position);
                    nomUsuario.setText(usuarioSeleccionado.getNombre());
                    apeUsuario.setText(usuarioSeleccionado.getApellido());
                    corUsuario.setText(usuarioSeleccionado.getCorreo());
                    passUsuario.setText(usuarioSeleccionado.getPassword());
                    telUsuario.setText(usuarioSeleccionado.getTelefono());
                    dirUsuario.setText(usuarioSeleccionado.getDireccion());
                    for (int i = 0; i < spinnerTipo.getCount(); i++){
                        if (spinnerTipo.getItemAtPosition(i).toString().equals(usuarioSeleccionado.getTipo())){
                            spinnerTipo.setSelection(i);
                        }
                    }
                    for (int e = 0; e <= spinnerHogar.getCount()+1; e++){
                        if (spinnerHogar.getItemAtPosition(e).toString().equals(usuarioSeleccionado.getHogar().getNombre())){
                            spinnerHogar.setSelection(e);
                        }
                    }

                }
            });
    } // On create (Todos los atributos de las vistas)

    private void listarDatos() {
        databaseReference.child("Usuario").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaUsuario.clear();
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Usuario u = objSnapshot.getValue(Usuario.class);
                    listaUsuario.add(u);
                    arrayAdapterUsuario = new ArrayAdapter<Usuario>(ingresarUsuarios.this, android.R.layout.simple_list_item_1, listaUsuario);
                    listV_usuarios.setAdapter(arrayAdapterUsuario);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    } // Listar los los usuarios

    private void spinnerHogar (){
        databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                    Hogar h = objSnapshot.getValue(Hogar.class);
                    listaHogar.add(h.getNombre());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void incializarFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        //firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    } // Inicializar Firebase

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case R.id.icon_add:{

                    databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String hogar = spinnerHogar.getSelectedItem().toString();
                            String nombre= nomUsuario.getText().toString();
                            String apellido= apeUsuario.getText().toString();
                            String correo= corUsuario.getText().toString();
                            String password= passUsuario.getText().toString();
                            String telefono = telUsuario.getText().toString();
                            String direccion = dirUsuario.getText().toString();
                            String tipo = spinnerTipo.getSelectedItem().toString();

                            if (nombre.equals("") || apellido.equals("") || correo.equals("") || password.equals("") || telefono.equals("") ||
                                    direccion.equals("") || tipo.equals("Tipo") || hogar.equals("Hogar") ){
                                validacion();
                            }
                            else {
                                Usuario u = new Usuario();
                                u.setUid(UUID.randomUUID().toString());
                                u.setNombre(nombre);
                                u.setApellido(apellido);
                                u.setCorreo(correo);
                                u.setPassword(password);
                                u.setTelefono(telefono);
                                u.setDireccion(direccion);
                                u.setTipo(tipo);
                                for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                                    Hogar h = objSnapshot.getValue(Hogar.class);
                                    if (hogar.equals(h.getNombre())){
                                        u.setHogar(h);
                                        }
                                }
                                databaseReference.child("Usuario").child(u.getUid()).setValue(u);

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


                databaseReference.child("Hogar").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Usuario u = new Usuario ();
                        u.setUid(usuarioSeleccionado.getUid());
                        u.setNombre(nomUsuario.getText().toString().trim());
                        u.setApellido(apeUsuario.getText().toString().trim());
                        u.setCorreo(corUsuario.getText().toString().trim());
                        u.setPassword(passUsuario.getText().toString().trim());
                        u.setTelefono(telUsuario.getText().toString().trim());
                        u.setDireccion(dirUsuario.getText().toString().trim());
                        u.setTipo(spinnerTipo.getSelectedItem().toString().trim());
                        for (DataSnapshot objSnapshot : dataSnapshot.getChildren()){
                            Hogar h = objSnapshot.getValue(Hogar.class);
                            if (spinnerHogar.getSelectedItem().toString().equals(h.getNombre())){
                                u.setHogar(h);
                            }
                        }
                        databaseReference.child("Usuario").child(u.getUid()).setValue(u);
                        Toast.makeText(ingresarUsuarios.this,"Guardar", Toast.LENGTH_LONG).show();
                        limpiarCajas();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                break;
            }

            case R.id.icon_delete:{
                Usuario u = new Usuario();
                u.setUid(usuarioSeleccionado.getUid());
                databaseReference.child("Usuario").child(u.getUid()).removeValue();
                Toast.makeText(this,"Eliminar", Toast.LENGTH_LONG).show();
                limpiarCajas();
                break;
            }

            case R.id.icon_login:{

            }
            default:break;

        }
        return true;
    }

    private void limpiarCajas() {

        nomUsuario.setText("");
        apeUsuario.setText("");
        corUsuario.setText("");
        passUsuario.setText("");
        telUsuario.setText("");
        dirUsuario.setText("");
        spinnerHogar.setSelection(0);
        spinnerTipo.setSelection(0);
    }

    private void validacion() {

        String nombre = nomUsuario.getText().toString();
        String apellido = apeUsuario.getText().toString();
        String correo = corUsuario.getText().toString();
        String password = passUsuario.getText().toString();
        String telefono = telUsuario.getText().toString();
        String direccion = dirUsuario.getText().toString();


        if (nombre.equals("")){
            nomUsuario.setError("Requerido");
        }
        else if (apellido.equals("")){
            apeUsuario.setError("Requerido");
        }
        else if (correo.equals("")){
            corUsuario.setError("Requerido");
        }
        else if (password.equals("")){
            passUsuario.setError("Requerido");
        }
        else if (telefono.equals("")){
            telUsuario.setError("Requerido");
        }
        else if (direccion.equals("")){
            dirUsuario.setError("Requerido");
        }
        else if (spinnerTipo.getSelectedItem().equals("Tipo")){
            Toast.makeText(this,"Debe seleccionar el tipo de usuario", Toast.LENGTH_LONG).show();
        }
        else if (spinnerHogar.getSelectedItem().equals("Hogar")){
            Toast.makeText(this,"Debe seleccionar el Hogar", Toast.LENGTH_LONG).show();
        }

    }
}

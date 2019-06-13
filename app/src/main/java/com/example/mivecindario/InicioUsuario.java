package com.example.mivecindario;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class InicioUsuario extends AppCompatActivity {

    Toolbar toolbar;

    String tipoUsuario,nmUsuario,apUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_usuario);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recibirDatos();
        tiposUsuarios();

    }

    private void recibirDatos() {
        Bundle datoExtra = getIntent().getExtras();
        nmUsuario = datoExtra.getString("nombreUsuario");
        apUsuario = datoExtra.getString("apellidoUsuario");
        tipoUsuario = datoExtra.getString("tipoUsuario");
        toolbar.setSubtitle(nmUsuario+" "+apUsuario);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    private void tiposUsuarios(){

        switch (tipoUsuario){

            case "Presidente junta vecinos":{

                break;
            }

            case "Jefe de hogar":{

                break;
            }

            case "Miembro de hogar":{

                break;
            }

        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //@Override
    //public boolean onOptionsItemSelected(MenuItem item) {

        //switch (item.getItemId()){

            //case R.id.icon_add:{

                //break;
            //}

            //case R.id.icon_save:{
                //break;
            //}

            //case R.id.icon_delete:{
                //break;
            //}

           // case R.id.icon_login:{

            //}
            //default:break;

        //}
        //return true;
    //}



}

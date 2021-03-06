package com.breakapp.notificaciones_bomberos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.FileOutputStream;

/**
 * Created by valentinocerutti on 25/2/18.
 */

public class Logged_In extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);

        Button salir = (Button) findViewById(R.id.salir);
        TextView text = (TextView) findViewById(R.id.desc);


        Globals g = (Globals)getApplication();
        String localidad = g.getData();
        text.setText("Bienvenido, ya esta registrado para recibir las notificaciones de "+ localidad +", ya no" +
                " debe ingresar a la app a menos que desee cambiar el cuartel al que se sumó previamente." +
                " Las llamadas de alerta llegaran automáticamente a su dispositivo, muchas gracias por elegir " +
                "nuestro servicio.");
        localidad = localidad.replace(" ","-");

        FirebaseMessaging.getInstance().subscribeToTopic(localidad);
        Log.d("FIRED","Subscribed to " + localidad);


        final String finalLocalidad = localidad;
        salir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String filename = "localidad";
                String fileContents = null;
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(fileContents.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                Globals g = (Globals) getApplication();
                g.setData(null);

                FirebaseMessaging.getInstance().unsubscribeFromTopic(finalLocalidad);
                Log.d("FIRED","Unsubcribed from " + finalLocalidad);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);


            }
        });

    }
    @Override
    public void onBackPressed() {

        moveTaskToBack(true);

    }

}





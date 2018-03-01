package com.breakapp.notificaciones_bomberos;


import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;




public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyFirebaseMsgService";

    RequestQueue ExampleRequestQueue = null;
    RequestQueue MyRequestQueue = null;
    String selectedLocalidad = null;
    Spinner spinner = null;
    EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FileInputStream fin = openFileInput("localidad");

            int c;
            String temp="";
            while( (c = fin.read()) != -1){
                temp = temp + Character.toString((char)c);
            }

            fin.close();

            if (temp != ""){

                Globals g = (Globals) getApplication();
                g.setData(temp);

                Intent intent = new Intent(getApplicationContext(), Logged_In.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(intent);

            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        ExampleRequestQueue = Volley.newRequestQueue(this);
        MyRequestQueue = Volley.newRequestQueue(this);
        spinner = (Spinner) findViewById(R.id.localidades);
        password = (EditText) findViewById(R.id.password);
        getList();

        Button button = (Button) findViewById(R.id.submit);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                autenticateUser();
            }
        });
    }

    public void autenticateUser() {

        String url = "http://breakappgames.com/bomberos/validate.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject obj = new JSONObject(response);


                    Log.d("ERROR",response);

                    int error = obj.getInt("error");

                    if (error == 0){

                        String filename = "localidad";
                        String fileContents = selectedLocalidad;
                        FileOutputStream outputStream;

                        try {
                            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                            outputStream.write(fileContents.getBytes());
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        Globals g = (Globals) getApplication();
                        g.setData(selectedLocalidad);

                        Intent intent = new Intent(getApplicationContext(), Logged_In.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivity(intent);
                    } else if(error == 1){
                        Toast.makeText(getApplicationContext(),"Contraseña incorrecta",
                                Toast.LENGTH_LONG).show();
                    } else if(error == 2){
                        Toast.makeText(getApplicationContext(),"Verifique la contraseña con el cuartelero",
                                Toast.LENGTH_LONG).show();
                    } else if(error == 3){
                        Toast.makeText(getApplicationContext(),"Ha ocurrido un error, intente más tarde",
                                Toast.LENGTH_LONG).show();
                    } else if(error == 4){
                        Toast.makeText(getApplicationContext(),"Por favor, corrobore su contraseña",
                                Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                selectedLocalidad =  spinner.getSelectedItem().toString();
                String selectedPassword = String.valueOf(password.getText());
                MyData.put("localidad", selectedLocalidad); //Add the data you'd like to send to the server.
                MyData.put("password", selectedPassword);


                Log.d("INFO",selectedPassword);
                Log.d("INFO",selectedLocalidad);
                Log.d("INFO","DATA: " + MyData);
                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);

    }





    public void getList() {

        String url = "http://breakappgames.com/bomberos/get_list.php";
        JsonArrayRequest get_localidades = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {


                    String[] strArr = new String[response.length() + 1];
                    for (int i = 0; i < response.length(); i++) {
                            strArr[i+1] = response.getString(i);
                    }

                    strArr[0] = "Elija Ciudad";

                    ArrayAdapter<String > spinnerArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, strArr);
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                    spinner.setAdapter(spinnerArrayAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("Field", "Value"); //Add the data you'd like to send to the server.
                return MyData;
            }
        };

        ExampleRequestQueue.add(get_localidades);
    }









    private int getPersistedItem() {
        String keyName = makePersistedItemKeyName();
        return PreferenceManager.getDefaultSharedPreferences(this).getInt(keyName, 0);
    }

    protected void setPersistedItem(int position) {
        String keyName = makePersistedItemKeyName();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(keyName, position).commit();
    }

    private String makePersistedItemKeyName() {
        return  "_your_key";
    }



}





package com.example.maps;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivityittaqi extends AppCompatActivity {

    TextView ville;
    TextView tmp;
    TextView tmpmin;
    TextView tmpmax;
    TextView txtpression;
    TextView txthumidite;
    TextView txtdate;
    TextView sea;
    TextView wind;


    double lon;
    double lat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ville=findViewById(R.id.txtville);

        tmp=findViewById(R.id.temp);
        tmpmin=findViewById(R.id.tempmin);
        tmpmax=findViewById(R.id.tempmax);
        txtpression=findViewById(R.id.pression);
        txthumidite=findViewById(R.id.humid);
        txtdate=findViewById(R.id.date);

        Bundle b=getIntent().getExtras();
        lon =b.getDouble( "lon");
        lat = b.getDouble("lat" );
      //  lon=-7.3944;
        //lat=33.7066;
        sea=findViewById(R.id.sea);
        wind=findViewById(R.id.wind);
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        String url=" https://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&appid=e457293228d5e1465f30bcbe1aea456b";

        //    String url="http://api.openweathermap.org/data/2.5/weather?q="
        // +query+"&appid=e457293228d5e1465f30bcbe1aea456b";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    Log.i("MyLog","----------------------------------------------");
                    Log.i("MyLog",response);

                    JSONObject jsonObject=new JSONObject(response);

                    Date date=new Date(jsonObject.getLong("dt")*1000);
                    SimpleDateFormat simpleDateFormat=
                     new SimpleDateFormat("dd-MMM-yyyy' T 'HH:mm");
                    String dateString=simpleDateFormat.format(date);


                    JSONObject main=jsonObject.getJSONObject("main");
                    int Temp=(int)(main.getDouble("temp")-273.15);
                    int TempMin=(int)(main.getDouble("temp_min")-273.15);
                    int TempMax=(int)(main.getDouble("temp_max")-273.15);
                    int Pression=(int)(main.getDouble("pressure"));
                    int Humidite=(int)(main.getDouble("humidity"));
                    //sea_level apparient a main omayma ittaqi
                    int Sea=(int)(main.getDouble("sea_level"));
                    //ajouter l'objet wind et recuperer sa degree ittaqi omayma

                    JSONObject windmeteo=jsonObject.getJSONObject("wind");
                    int Wind=(int)(windmeteo.getDouble("deg"));

                    JSONArray weather=jsonObject.getJSONArray("weather");
                    String meteo=weather.getJSONObject(0).getString("main");
                    String Name=weather.getJSONObject(0).getString("name");
                    ville.setText(Name);

                    txtdate.setText(dateString);
                    tmp.setText(String.valueOf(Temp+"°C"));
                    tmpmin.setText(String.valueOf(TempMin)+"°C");
                    tmpmax.setText(String.valueOf(TempMax)+"°C");
                    txtpression.setText(String.valueOf(Pression+" hPa"));
                    txthumidite.setText(String.valueOf(Humidite)+ "%");
                    //ecrire dans les text view les valeur recupeerés
                    wind.setText(String.valueOf(Wind));
                    sea.setText(String.valueOf(Sea));

                    Log.i("Weather","----------------------------------------------");
                    Log.i("Meteo",meteo);

                    //Toast.makeText(getApplicationContext( ), response, Toast.LENGTH_LONG).show( );
                } catch (JSONException e ) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.i("MyLog","-------Connection problem-------------------");
                        Toast.makeText(MainActivityittaqi.this,
                                "City not fond",Toast.LENGTH_LONG).show();


                    }
                });

        queue.add(stringRequest);



        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

    }

}


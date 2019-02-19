package com.example.lenovo.encryption;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

public class Login extends AppCompatActivity{
    AsyncHttpClient client;
    RequestParams params;
    JSONObject object;
   Button reg,logg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        logg=findViewById(R.id.login);
        reg=findViewById(R.id.register);
        client=new AsyncHttpClient();
        params=new RequestParams();
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });
         logg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent j=new Intent(Login.this,Home.class);
                 startActivity(j);
             }
         });
    }
}
package com.example.lenovo.encryption;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity{
    AsyncHttpClient client;
    RequestParams params;
    JSONObject object;
    EditText email,pswrd;
    TextView forgetp;
   Button reg,logg;
   String url="http://srishti-systems.info/projects/organ_donation/donor_login.php?";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();



        email=findViewById(R.id.emailid);
        logg=findViewById(R.id.login);
        reg=findViewById(R.id.register);
        forgetp=findViewById(R.id.forget);
        pswrd=findViewById(R.id.pswdid);
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
                 final String logemail=email.getText().toString();
                 final String logpassword=pswrd.getText().toString();
                 params.put("username",logemail);
                 params.put("password",logpassword);
                 client.get(url,params,new AsyncHttpResponseHandler()
                         {
                             @Override
                             public void onSuccess(String content) {
                                 super.onSuccess(content);
                                 try {
                                     object=new JSONObject(content);
                                       if(logemail.isEmpty())

                                     {
                                         email.setError("Enter Registered email address");
                                     }
                                     if(logpassword.isEmpty())
                                     {
                                         pswrd.setError("Enter Password");
                                     }
                                else if(object.getString("status").equals("Success"))
                                  {
                                      Toast.makeText(Login.this, "" + object.getString("status"), Toast.LENGTH_SHORT).show();

                                      Intent j=new Intent(Login.this,Home.class);
                                      startActivity(j);
                                  }

                                  else
                                      Toast.makeText(Login.this, "" + object.getString("status"), Toast.LENGTH_SHORT).show();

                                 } catch (JSONException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }
                 );

             }
         });
         forgetp.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent ik=new Intent(Login.this,Forget_password.class);
                 startActivity(ik);
             }
         });
    }
}
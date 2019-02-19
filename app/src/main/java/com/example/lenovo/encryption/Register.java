package com.example.lenovo.encryption;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
EditText email,password,conformpswd;
Button reg;
AsyncHttpClient client;
RequestParams params;
JSONObject object;
String url="http://srishti-systems.info/projects/organ_donation/donor_register.php?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        email=findViewById(R.id.editText3);
        password=findViewById(R.id.editText4);
        conformpswd=findViewById(R.id.editText5);
        reg=findViewById(R.id.button);
        client=new AsyncHttpClient();
        params=new RequestParams();


reg.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final String st_email=email.getText().toString();
        final String st_password=password.getText().toString();
        final String st_conform=conformpswd.getText().toString();

        params.put("mailid",st_email);
        params.put("password",st_password);
        params.put("confirmpassword",st_conform);

    // if(st_email!=""||st_password!=""||st_conform!="") {

            client.get(url, params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String content) {
                            super.onSuccess(content);

                            try {
                                object = new JSONObject(content);

                                if (object.getString("status").equals("success")) {
                                    Toast.makeText(Register.this, "" + object.getString("status"), Toast.LENGTH_SHORT).show();

                                    Intent i = new Intent(Register.this, Login.class);
                                    startActivity(i);
                                }
                                else if(st_email.isEmpty())
                                {
                                    email.setError("Enter email address");
                                }
                                else if(st_password.isEmpty())
                                {
                                    password.setError("Enter a password");
                                }
                                else if(st_conform.isEmpty())
                                {
                                    conformpswd.setError("Please re-enter password");
                                }
                                else if(st_password!=st_conform)
                                {
                                    conformpswd.setError("Passwords not matching");
                                }

                            } catch (JSONException e)
                            {
                                Toast.makeText(Register.this, "" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }

            );
      //  }
    }
});

    }
}

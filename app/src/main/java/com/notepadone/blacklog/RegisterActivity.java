package com.notepadone.blacklog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText username,phoneno,email,agencyName,officeAddresses,passwordEditText,passwordAgain;
    Button signUpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try
        {

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        signUpButton = findViewById(R.id.signUpButton);
        username = findViewById(R.id.username);
        phoneno = findViewById(R.id.phoneno);
        agencyName = findViewById(R.id.agencyName);
        email = findViewById(R.id.email);
        officeAddresses = findViewById(R.id.officeAddresses);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordAgain = findViewById(R.id.passwordAgain);
        listeners();

    }

    public void listeners(){
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username1 = username.getText().toString();
                String phoneno1 = phoneno.getText().toString();
                String agencyName1 = agencyName.getText().toString();
                String email1 = email.getText().toString();
                String officeAddresses1 = officeAddresses.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirmPassword = passwordAgain.getText().toString();
                if(username1.equals("") || phoneno1.equals("") || agencyName1.equals("") || email1.equals("") || officeAddresses1.equals("")
                        || password.equals("")|| confirmPassword.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    register();
                }

            }
        });
    }

    public void register(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://api.blacklog.in/user/add_user.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("LOG_VOLLEYResponse", response.toString());
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("LOG_VOLLEYResponseError", error.toString());
                        }
                    }){
                @Override
                protected Map<String,String> getParams(){
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("name",username.getText().toString());

                    params.put("phone",phoneno.getText().toString());
                    params.put("agency_name",agencyName.getText().toString());
                    params.put("address",officeAddresses.getText().toString());
                    params.put("lat","-1");
                    params.put("long","-1");
                    params.put("email_id",email.getText().toString());
                    params.put("user_type","CL");
                    params.put("password",passwordEditText.getText().toString());

                    return params;
                }

            };

            requestQueue.add(stringRequest);
/*
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEYResponse", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEYError", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };


 */

         //   requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.notepadone.blacklog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.notepadone.blacklog.Trucksinfo.TrucksInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView signintext;
    Button loginButton;
    EditText username, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // creating full screen view
        try
        {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        //setting the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        setupID();


        //setting up all listeners
        listeners();
    }

    public void setupID(){
        signintext = findViewById(R.id.signintext);
        String id = "No account yet then Please ";
        String name = " Sign In";
        String sourceString = id + "<b>" + name + "</b> ";

        signintext.setText(Html.fromHtml(sourceString));


        loginButton = findViewById(R.id.loginButton);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

    }
    public void listeners(){
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                register();
                /*Intent intent = new Intent(LoginActivity.this, TrucksInfo.class);
                startActivity(intent);

                 */
            }
        });
        signintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void register(){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "http://api.blacklog.in/user/auth_user.php";

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
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("login_id",username.getText().toString());
                    params.put("password",password.getText().toString());

                    return params;
                }

            };

            requestQueue.add(stringRequest);
            /*
            JSONObject jsonBody = new JSONObject();
           // jsonBody.put("username", username.getText().toString());
            jsonBody.put("login_id", username.getText().toString());
            jsonBody.put("password", password.getText().toString());
//837003945
            final String mRequestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("LOG_VOLLEYRESPONSE", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("LOG_VOLLEYERROR", error.toString());
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
                        Log.i("LOG_VOLLEYRESPONSE", responseString);
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            */


           // requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
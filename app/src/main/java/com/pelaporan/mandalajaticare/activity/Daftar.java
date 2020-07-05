package com.pelaporan.mandalajaticare.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pelaporan.mandalajaticare.Menu;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.controller.AppController;
import com.pelaporan.mandalajaticare.config.PrefManager;
import com.pelaporan.mandalajaticare.config.Server;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class Daftar extends AppCompatActivity implements View.OnClickListener {

    TextView tvLogin;
    Button btnDaftar;

    EditText etUsername,
            etPassword,
            etConfPassword,
            etAlamat,
            etPhone,
            etEmail,
            etNama;
    AnimationDrawable animationDrawable;
    ScrollView scrollView;
    int success;
    private static final String TAG_SUCCESS = "success";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);
        // Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        scrollView = (ScrollView) findViewById(R.id.rootLayout);
        tvLogin = (TextView) findViewById(R.id.daftar);
        tvLogin.setOnClickListener(this);
        btnDaftar = (Button) findViewById(R.id.signin);
        btnDaftar.setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);
        etConfPassword = (EditText) findViewById(R.id.conf_password);
        etAlamat = (EditText) findViewById(R.id.alamat);
//        etPhone = (EditText) findViewById(R.id.phone);
        etNama = (EditText) findViewById(R.id.nama);
        etEmail = (EditText) findViewById(R.id.email);

//        animationDrawable = (AnimationDrawable) scrollView.getBackground();
//        animationDrawable.setExitFadeDuration(3000);
//        animationDrawable.setExitFadeDuration(3000);
//        animationDrawable.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.daftar:
//                Intent it = new Intent(Daftar.this, Login.class);
//                startActivity(it);
                startActivity(new Intent(Daftar.this, Login.class));
                customType(Daftar.this, "right-to-left");
                break;
            case R.id.signin:
                if (etNama.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Masukkan Nama dengan benar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etUsername.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Masukkan Username dengan benar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etPassword.getText().toString().length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password Min 6 Karakter", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    etPassword.setError(null);
                }
                if (!etPassword.getText().toString().equals(etConfPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    etPassword.setError(null);
                }
                if (etAlamat.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Masukkan Alamat dengan benar", Toast.LENGTH_SHORT).show();
                    return;
                }
//                if (etPhone.length() < 1) {
//                    Toast.makeText(getApplicationContext(), "Masukkan No Phone dengan benar", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (isValidEmail(etEmail.getText().toString().trim())) {
//                    Toast.makeText(getApplicationContext(), "Email sudah benar", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Format email salah", Toast.LENGTH_SHORT).show();
                    return;
                }
                Daftar();
                break;
        }
    }

    public static boolean isValidEmail (String email) {
        boolean validate;
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String emailPattern2 = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.+[a-z]+";

        if (email.matches(emailPattern)) {
            validate = true;
        } else if (email.matches(emailPattern2)) {
            validate = true;
        } else {
            validate = false;
        }
        return validate;
    }

    private void Daftar() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Daftar...", " Mohon Tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL + "android/daftar.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                Toast.makeText(Daftar.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(Daftar.this, Login.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(Daftar.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(Daftar.this, "Maaf Ada Kesalahan!!", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();
                String Login = etUsername.getText().toString();
                String Password = etPassword.getText().toString();

                params.put("Login", Login.trim());
                params.put("Password", Password.trim());
                params.put("Alamat", etAlamat.getText().toString());
//                params.put("Phone", etPhone.getText().toString());
                params.put("Nama", etNama.getText().toString());
                params.put("Email", etEmail.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "json");
    }

}


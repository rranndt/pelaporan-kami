package com.pelaporan.mandalajaticare.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.config.Server;
import com.pelaporan.mandalajaticare.controller.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class ResetPassword extends AppCompatActivity {

    EditText etTextEmail;
    Button btnReset, btnBack;
    ProgressDialog pd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resetpassword);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        etTextEmail = (EditText) findViewById(R.id.etTextEmail);
        btnReset = (Button) findViewById(R.id.btnLupa);
        btnBack = (Button) findViewById(R.id.btnBack);
        pd = new ProgressDialog(ResetPassword.this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ResetPassword.this, Login.class));
                customType(ResetPassword.this, "right-to-left");
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String v_email = etTextEmail.getText().toString().trim();
                if (TextUtils.isEmpty(v_email))
                {
                    etTextEmail.setError("Email Tidak Boleh Kosong");
                    etTextEmail.requestFocus();
                    return;
                }
                else
                {
                    reset();
                }
            }
        });
    }

    public void reset() {
//        pd.setMessage("Silakan Tunggu...");
//        pd.setCancelable(true);
//        pd.show();

        final String email = etTextEmail.getText().toString().trim();
        final ProgressDialog loading = ProgressDialog.show(this,"Mengirim Konfirmasi", "Mohon Tunggu...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL + "android/reset_password.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getBoolean("error"))
                            {
                                pd.cancel();
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                pd.cancel();
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(ResetPassword.this, Login.class);
                                startActivity(i);
                                finish();
                            }
                        }
                        catch (JSONException e)
                        {
//                            pd.cancel();
                            e.printStackTrace();
                        }
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pd.cancel();
                        // Menhilangkan progress dialog
                        loading.dismiss();

                        Toast.makeText(ResetPassword.this, "Maaf Ada Kesalahan!!", Toast.LENGTH_LONG).show();
                    }
                })
        {
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("email", email.trim());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}

package com.pelaporan.mandalajaticare.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.pelaporan.mandalajaticare.Menu;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.config.PrefPetugas;
import com.pelaporan.mandalajaticare.controller.AppController;
import com.pelaporan.mandalajaticare.config.PrefManager;
import com.pelaporan.mandalajaticare.config.Server;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static maes.tech.intentanim.CustomIntent.customType;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView tvDaftar, lupaspassword;
    private ImageView ivPetugas;
    private Button btnLogin;
    private EditText etUsername,
            etPassword;
    private SharedPreferences level;
    private SharedPreferences prefssatu, prefpassword;
    private PrefManager prefManager;
    private PrefPetugas prefPetugas;
    private AnimationDrawable animationDrawable;
    private RelativeLayout relativeLayout;

    public static final String SATU = "PREFS_WORLD_READABLE_WRITABLE";
    public static final String SATUU = "PREFS_WORLD_READABLE_WRITABLEE";
    public static final String KEY_SATU = "KEY_WORLD_READ_WRITE";

    public static final String PASSOWRD = "etPassword";
    public static final String KEY_PASSWORD = "key_password";

    public static final String IDUSER = "iduser";
    public static final String KEYIDUSER = "key_iduser";

    public static final String LEVEL = "level";
    public static final String KEYLEVEL = "key_level";

    int success;
    private static final String TAG_SUCCESS = "success";

    private SharedPreferences permissionStatus;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;

    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        // Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        relativeLayout = (RelativeLayout) findViewById(R.id.rootLayout);
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
//        ivPetugas = (ImageView) findViewById(R.id.ivPetugas);
//        ivPetugas.setOnClickListener(this);
        lupaspassword = (TextView) findViewById(R.id.lupapassword);
        lupaspassword.setOnClickListener(this);
        btnLogin = (Button) findViewById(R.id.signin);
        btnLogin.setOnClickListener(this);
        tvDaftar = (TextView) findViewById(R.id.daftar);
        tvDaftar.setOnClickListener(this);
        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.password);

//        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
//        animationDrawable.setEnterFadeDuration(3000);
//        animationDrawable.setExitFadeDuration(3000);
//        animationDrawable.start();

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(false);
            Intent i = new Intent(Login.this, Menu.class);
            startActivity(i);
            finish();
        }

        prefPetugas = new PrefPetugas(this);
        if (!prefPetugas.pasFirstTimeLaunch()) {
            prefPetugas.setIsFirstTimeLaunch(false);
            Intent i = new Intent(Login.this, DashboardPetugas.class);
            startActivity(i);
            finish();
        }
        cekPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.daftar:
//                Intent it = new Intent(Login.this, Daftar.class);
//                startActivity(it);
                startActivity(new Intent(Login.this, Daftar.class));
                customType(Login.this, "left-to-right");
                break;
            case R.id.lupapassword:
                startActivity(new Intent(Login.this, ResetPassword.class));
                customType(Login.this, "left-to-right");
                break;
            case R.id.signin:
                if (etUsername.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Masukkan Username dengan benar", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etPassword.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Masukkan Password dengan benar", Toast.LENGTH_SHORT).show();
                    return;
                }

                prefssatu = getSharedPreferences(Login.SATU,
                        Context.MODE_PRIVATE + Context.MODE_PRIVATE
                                | Context.MODE_PRIVATE);
                SharedPreferences.Editor worldReadWriteEdit = prefssatu.edit();
                worldReadWriteEdit.putString(Login.KEY_SATU, etUsername.getText()
                        .toString());
                worldReadWriteEdit.commit();

                prefpassword = getSharedPreferences(Login.PASSOWRD,
                        Context.MODE_PRIVATE + Context.MODE_PRIVATE
                                | Context.MODE_PRIVATE);
                SharedPreferences.Editor worldReadWriteEdit1 = prefpassword.edit();
                worldReadWriteEdit1.putString(Login.KEY_PASSWORD, etPassword.getText()
                        .toString());
                worldReadWriteEdit1.commit();

                String validasi;
                String bantuan;
                validasi = etUsername.getText().toString().trim();
                char c = validasi.charAt(0);
                Character fix = new Character(c);
                if (fix.equals('@')) {
                    LoginPetugas();
                } else {
                    Login();
                }

                break;
        }
    }

    public void cekPermission() {
        if (ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[2])) {
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(Login.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }
            if (allgranted) {
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[2])) {
//                txtPermissions.setText("Permissions Required");
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Izinkan permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Login.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.show();
            }
        }
    }

    private void Login() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Login...", " Mohon Tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL + "android/login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                Toast.makeText(Login.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
                                String ambillevel;
                                ambillevel = jObj.getString("level");

                                level = getSharedPreferences(Login.LEVEL,
                                        Context.MODE_PRIVATE + Context.MODE_PRIVATE
                                                | Context.MODE_PRIVATE);
                                SharedPreferences.Editor worldReadWriteEdit = level.edit();
                                worldReadWriteEdit.putString(Login.KEYLEVEL, ambillevel);
                                worldReadWriteEdit.commit();
                                prefManager.buatSession(jObj.getString("username"), jObj.getString("nama"), jObj.getString("password"), jObj.getString("alamat"), jObj.getString("foto_profil"));
                                prefManager.setFirstTimeLaunch(false);
                                Intent i = new Intent(Login.this, Menu.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(Login.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Error : " + e, Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(Login.this, "Maaf Ada Kesalahan!!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat paramete
                Map<String, String> params = new HashMap<String, String>();
                String Login = etUsername.getText().toString();
                String Password = etPassword.getText().toString();
                params.put("Login", Login.trim());
                params.put("Password", Password.trim());

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "json");
    }

    private void LoginPetugas() {
        //menampilkan progress dialog
        final ProgressDialog loading = ProgressDialog.show(this, "Login...", " Mohon Tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL + "android/loginpetugas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);
                            if (success == 1) {
                                Toast.makeText(Login.this, jObj.getString("message"), Toast.LENGTH_LONG).show();

                                prefPetugas.setIsFirstTimeLaunch(false);
                                Intent i = new Intent(Login.this, DashboardPetugas.class);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(Login.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(Login.this, "Saya di Catch", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Login.this, "Maaf Ada Kesalahan!!", Toast.LENGTH_LONG).show();
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

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "json");
    }
}

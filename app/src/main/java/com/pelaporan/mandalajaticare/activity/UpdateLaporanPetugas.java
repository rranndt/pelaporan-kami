package com.pelaporan.mandalajaticare.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.SupportMapFragment;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.adapter.HistoryAdapter;
import com.pelaporan.mandalajaticare.adapter.UpdateLaporanAdapter;
import com.pelaporan.mandalajaticare.config.PrefPetugas;
import com.pelaporan.mandalajaticare.config.Server;
import com.pelaporan.mandalajaticare.controller.AppController;
import com.pelaporan.mandalajaticare.model.HistoryModel;
import com.pelaporan.mandalajaticare.model.UpdateLaporanModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static maes.tech.intentanim.CustomIntent.customType;

public class UpdateLaporanPetugas extends AppCompatActivity {

    private static ProgressDialog mProgressDialog;
    private ArrayList<UpdateLaporanModel> dataModelArrayList;
    private UpdateLaporanAdapter rvAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_laporan_petugas);

        getSupportActionBar().setTitle("Update Laporan");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.rvUpdateLaporan);

        getDataHistory();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            setMode(item.getItemId());
        }
        return super.onOptionsItemSelected(item);
    }

    private void setMode(int selectedMode) {
        switch (selectedMode) {
            case R.id.action:
                keluar();
                break;
        }
    }

    private void getDataHistory() {

        showSimpleProgressDialog(UpdateLaporanPetugas.this, "Tunggu...", "Mengambil Data", false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.URL + "android/updatelaporanpetugas.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);

                        try {

                            removeSimpleProgressDialog();

                            JSONObject obj = new JSONObject(response);
                            if (obj.optString("status").equals("true")) {

                                dataModelArrayList = new ArrayList<>();
                                JSONArray dataArray = obj.getJSONArray("data");

                                for (int i = 0; i < dataArray.length(); i++) {

                                    UpdateLaporanModel playerModel = new UpdateLaporanModel();
                                    JSONObject dataobj = dataArray.getJSONObject(i);

//                                    playerModel.setPelapor(dataobj.getString("nama"));
//                                    playerModel.setImgURL(dataobj.getString("Foto1"));
//                                    playerModel.setJenis(dataobj.getString("jenis_pelaporan"));
//                                    playerModel.setDetail(dataobj.getString("kejadian_detail"));
//                                    playerModel.setAlamat(dataobj.getString("alamat"));
//                                    playerModel.setAksi(dataobj.getString("tindak_lanjut"));
                                    playerModel.setUsername(dataobj.getString("username"));
                                    playerModel.setDetail(dataobj.getString("kejadian_detail"));
                                    playerModel.setAksi(dataobj.getString("tindak_lanjut"));
                                    playerModel.setImgURL(dataobj.getString("Foto1"));

                                    dataModelArrayList.add(playerModel);

                                }

                                setupRecycler();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(UpdateLaporanPetugas.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, "json");
    }

    private void setupRecycler() {
        rvAdapter = new UpdateLaporanAdapter(UpdateLaporanPetugas.this, dataModelArrayList);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(UpdateLaporanPetugas.this, LinearLayoutManager.VERTICAL, false));
    }

    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void keluar() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PrefPetugas prefPetugas = new PrefPetugas(getApplicationContext());

                        // make first time launch TRUE
                        prefPetugas.setIsFirstTimeLaunch(true);
                        prefPetugas.logout();
                        startActivity(new Intent(UpdateLaporanPetugas.this, Login.class));
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).show();
    }
}

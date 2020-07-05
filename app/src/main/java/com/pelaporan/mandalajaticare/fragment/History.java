package com.pelaporan.mandalajaticare.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.activity.Login;
import com.pelaporan.mandalajaticare.adapter.HistoryAdapter;
import com.pelaporan.mandalajaticare.config.Server;
import com.pelaporan.mandalajaticare.controller.AppController;
import com.pelaporan.mandalajaticare.model.HistoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {

    //    private String URLstring = "https://admin.mandalajati.online/android/riwayat_tes.php";
    private static ProgressDialog mProgressDialog;
    private ArrayList<HistoryModel> dataModelArrayList;
    private HistoryAdapter rvAdapter;
    private RecyclerView recyclerView;
    SharedPreferences username;

    String ambilusername;

//    private static View view;

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("History Pelaporan");

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = view.findViewById(R.id.rvHistory);

        username = getActivity().getSharedPreferences(Login.SATU, Context.MODE_PRIVATE +
                Context.MODE_PRIVATE | Context.MODE_PRIVATE);
        ambilusername = username.getString(Login.KEY_SATU, "username");
        getDataHistory();

        return view;
    }

    private void getDataHistory() {

        showSimpleProgressDialog(getActivity(), "Tunggu...", "Mengambil Data", false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, Server.URL + "android/riwayat_tes.php?username=" + ambilusername,
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

                                    HistoryModel playerModel = new HistoryModel();
                                    JSONObject dataobj = dataArray.getJSONObject(i);

//                                    playerModel.setPelapor(dataobj.getString("nama"));
                                    playerModel.setImgURL(dataobj.getString("Foto1"));
                                    playerModel.setJenis(dataobj.getString("jenis_pelaporan"));
                                    playerModel.setDetail(dataobj.getString("kejadian_detail"));
//                                    playerModel.setAlamat(dataobj.getString("alamat"));
                                    playerModel.setAksi(dataobj.getString("tindak_lanjut"));

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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        requestQueue.add(stringRequest);
        AppController.getInstance().addToRequestQueue(stringRequest, "json");
    }

    private void setupRecycler() {
        rvAdapter = new HistoryAdapter(getActivity(), dataModelArrayList);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
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
}

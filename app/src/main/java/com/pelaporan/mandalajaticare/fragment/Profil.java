package com.pelaporan.mandalajaticare.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pelaporan.mandalajaticare.Menu;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.activity.Login;
import com.pelaporan.mandalajaticare.config.PrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profil extends Fragment
{

    PrefManager prefManager;
    String mUsername, mNama, mPassword, mAlamat, mFotoProfil;
    private Object Profil;
    EditText nama,username,password,alamat;
//    public View view;
    private Bitmap bitmap;
    CircleImageView foto_profil;
    Button btnUpdate, btnEditFoto;

    public Profil()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        getActivity().setTitle("Update Profil");
        View view = inflater.inflate(R.layout.fragment_profil, container, false);

        prefManager = new PrefManager(getContext());
        HashMap<String, String> warga = prefManager.ambilSession();
        mUsername = warga.get(prefManager.USERNAME);
        mNama = warga.get(prefManager.NAMA);
        mPassword = warga.get(prefManager.PASSWORD);
        mAlamat = warga.get(prefManager.ALAMAT);
        mFotoProfil = warga.get(prefManager.FOTO_PROFIL);

        username = view.findViewById(R.id.etUsername);
        nama = view.findViewById(R.id.etNama);
        password = view.findViewById(R.id.etPassword);
        alamat = view.findViewById(R.id.etAlamat);
        username.setText(mUsername);
        nama.setText(mNama);
        alamat.setText(mAlamat);

        foto_profil = view.findViewById(R.id.img);
        Glide.get(getContext()).clearMemory();
        Glide.with(Profil.this)
                .load(mFotoProfil)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(foto_profil);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        btnEditFoto = view.findViewById(R.id.btnEditFoto);
        btnEditFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihFile();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                SaveEditDetail();
            }
        });

        return view;
    }

    private void SaveEditDetail()
    {
        final String fix_nama = nama.getText().toString().trim();
        final String fix_username = username.getText().toString();
        final String fix_password = password.getText().toString();
        final String fix_alamat = alamat.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://admin.mandalajati.online/android/updateprofil.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            int success = obj.getInt("success");

                            if (success == 1)
                            {
                                Toast.makeText(getContext(),obj.getString("message"), Toast.LENGTH_LONG).show();
                                String strFoto = obj.getString("foto_profil");
                                Glide.with(Profil.this)
                                        .load(strFoto)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(foto_profil);
                            }
                            else
                            {
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("nama",fix_nama);
                params.put("username",fix_username);
                params.put("password",fix_password);
                params.put("alamat",fix_alamat);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void pilihFile()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Foto"), 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(resultCode, resultCode, data);
        if(requestCode ==1 && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            Uri filePath = data.getData();
            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                foto_profil.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            uploadFoto(mUsername, getStringImage(bitmap));

        }
    }

    private void uploadFoto(final String username, final String foto)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://admin.mandalajati.online/android/updatefoto.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);
                            int success = obj.getInt("success");

                            if (success == 1)
                            {
                                Toast.makeText(getContext(),obj.getString("message"), Toast.LENGTH_LONG).show();
                                // Reload current fragment
                                getActivity().finish();
                            }
                            else
                            {
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("foto",foto);
                return params;
            }
        };

        RequestQueue requestQueuee = Volley.newRequestQueue(getContext());
        requestQueuee.add(stringRequest);
    }

    public String getStringImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);

        return encodedImage;
    }
}

package com.pelaporan.mandalajaticare.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.pelaporan.mandalajaticare.activity.Login;

import java.util.HashMap;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "awalLogink";
    private static final String PREF_NAMEE = "awalLoginkk";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NAMA = "nama";
    public static final String ALAMAT = "alamat";
    public static final String FOTO_PROFIL = "foto_profil";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunchk";
    private static final String IS_FIRST_TIME_LAUNCHH = "IsFirstTimeLaunchkk";

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void buatSession(String username, String nama, String password, String alamat, String foto_profil)
    {
        editor.putString(NAMA, nama);
        editor.putString(USERNAME, username);
        editor.putString(PASSWORD, password);
        editor.putString(ALAMAT, alamat);
        editor.putString(FOTO_PROFIL, foto_profil);
        editor.apply();
    }

    public HashMap<String, String> ambilSession()
    {
        HashMap<String, String> warga = new HashMap<>();
        warga.put(USERNAME, pref.getString(USERNAME, null));
        warga.put(PASSWORD, pref.getString(PASSWORD, null));
        warga.put(NAMA, pref.getString(NAMA, null));
        warga.put(ALAMAT, pref.getString(ALAMAT, null));
        warga.put(FOTO_PROFIL, pref.getString(FOTO_PROFIL, null));
        return warga;
    }

    public void logout()
    {
        editor.clear();
        editor.commit();
    }
}

package com.pelaporan.mandalajaticare.config;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefPetugas {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_APP = "awalLogin";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";

    SharedPreferences sp;

    public PrefPetugas(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_APP, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }

    public boolean pasFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void logout()
    {
        editor.clear();
        editor.commit();
    }
}

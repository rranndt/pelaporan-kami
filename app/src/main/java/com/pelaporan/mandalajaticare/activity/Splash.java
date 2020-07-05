package com.pelaporan.mandalajaticare.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pelaporan.mandalajaticare.R;

import org.w3c.dom.Text;


public class Splash extends Activity {

    private long ms = 0, splashTime = 1200;
    private boolean splashActive = true, paused = false;

    TextView tvAppVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
        String version = "1.0";

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvAppVersion.setText("App Version " + version);


        final ConstraintLayout relativeLayout = findViewById(R.id.rL);

        Thread thread = new Thread() {
            public void run() {
                try {
                    while (splashActive && ms < splashTime) {
                        if (!paused)
                            ms = ms + 100;
                        sleep(100);
                    }
                } catch (Exception e) {

                } finally {
                    if (!isOnline()) {
                        Snackbar snackbar = Snackbar
                                .make(relativeLayout, "Tidak Ada Jaringan Internet", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Retry", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        recreate();
                                    }
                                });
                        snackbar.show();
                    } else {
                        goMain();
                    }
                }
            }
        };
        thread.start();
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    private void goMain() {
        Intent intent = new Intent(Splash.this, Login.class);
        startActivity(intent);
        finish();
    }
}

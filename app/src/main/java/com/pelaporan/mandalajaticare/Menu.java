package com.pelaporan.mandalajaticare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.pelaporan.mandalajaticare.activity.Login;
import com.pelaporan.mandalajaticare.config.PrefManager;
import com.pelaporan.mandalajaticare.fragment.History;
import com.pelaporan.mandalajaticare.fragment.Home;
import com.pelaporan.mandalajaticare.fragment.ListKejadian;
import com.pelaporan.mandalajaticare.fragment.Pelaporan;
import com.pelaporan.mandalajaticare.fragment.Profil;
import com.pelaporan.mandalajaticare.fragment.Tentang;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;

public class Menu extends NavigationLiveo implements OnItemClickListener {

    private HelpLiveo mHelpLiveo;
    private SharedPreferences prefssatu;
    PrefManager prefManager;
    String mFotoProfil;

    @Override
    public void onInt(Bundle savedInstanceState) {

        prefssatu = this.getSharedPreferences(
                Login.SATU,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);

        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
//            currentVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
        }

        prefManager = new PrefManager(getApplicationContext());
        HashMap<String, String> warga = prefManager.ambilSession();
        mFotoProfil = warga.get(prefManager.FOTO_PROFIL);


        // User Information
        this.userName.setText(prefssatu.getString(
                Login.KEY_SATU, "ADMIN"));
        this.userEmail.setText("Mandalajati Care");
        this.userPhoto.setImageResource(R.drawable.person);
//        this.userBackground.setImageResource(R.drawable.minimal);

        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.addSeparator(); //Item separator
        // mHelpLiveo.add(getString(R.string.inbox), R.drawable.ic_inbox_black_24dp, 7);
        mHelpLiveo.addSubHeader("Menu"); //Item subHeader
        mHelpLiveo.add("Dashboard", R.drawable.ic_home_black_24dp);
        mHelpLiveo.add("Pelaporan", R.drawable.nilai);
        mHelpLiveo.add("Histori Pelaporan", R.drawable.bayar);
        mHelpLiveo.add("Profil", R.drawable.user);
        mHelpLiveo.add("Tentang Aplikasi", R.drawable.ic_about);

        //{optional} - Header Customization - method customHeader

        with(this).startingPosition(2) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
//                .removeHeader()
                //{optional} - List Customization "If you remove these methods and the list will take his white standard color"
                //.selectorCheck(R.drawable.selector_check) //Inform the background of the selected item color
                //.colorItemDefault(R.color.nliveo_blue_colorPrimary) //Inform the standard color name, icon and counter
                .colorItemSelected(R.color.blueDark) //State the name of the color, icon and meter when it is selected
                //.backgroundList(R.color.nliveo_black_light) //Inform the list of background color
                //.colorLineSeparator(R.color.nliveo_transparent) //Inform the color of the subheader line

                .footerItem("Logout", R.drawable.ic_power_settings_new_black_24dp)
                //{optional} - SubHeader Customization
                //.colorNameSubHeader(R.color.nliveo_blue_colorPrimary)
                //.colorLineSeparator(R.color.nliveo_green_colorPrimaryDark)

                //.removeFooter()
//              .footerItem(R.string.settings, R.drawable.ic_exit_to_app_black_24dp)

                //{optional} - Second footer
                //.footerSecondItem(R.string.settings, R.drawable.ic_settings_black_24dp)

                //{optional} - Header Customization
                //.customHeader(mCustomHeader)

                //{optional} - Footer Customization
                //.footerNameColor(R.color.nliveo_blue_colorPrimary)
                //.footerIconColor(R.color.nliveo_blue_colorPrimary)

                //.footerSecondNameColor(R.color.nliveo_blue_colorPrimary)
                //.footerSecondIconColor(R.color.nliveo_blue_colorPrimary)

                //.footerBackground(R.color.nliveo_white)

                //{optional} - Remove color filter icon
                //.removeColorFilter()

                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)

                //{optional} - Second footer
                //.setOnClickFooterSecond(onClickFooter)
                .build();
        int position = this.getCurrentPosition();
        this.setElevationToolBar(position != 1 ? 15 : 0);
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment = null;
        FragmentManager mFragmentManager = getSupportFragmentManager();
        switch (position) {

            case 2:
                mFragment = new Home();
                break;
            case 3:
                mFragment = new Pelaporan();
                break;
            case 4:
                mFragment = new History();
                break;
            case 5:
                mFragment = new Profil();
                break;
            case 6:
                mFragment = new Tentang();
                break;
//            default:
//                mFragment = MainFragment.newInstance(mHelpLiveo.get(position).getName());
//                break;
        }
        if (mFragment != null) {
            mFragmentManager.beginTransaction().replace(R.id.container, mFragment).commit();
        }
        setElevationToolBar(position != 2 ? 15 : 0);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(android.view.Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeDrawer();
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            keluar();
            closeDrawer();
        }
    };

    public void keluar() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PrefManager prefManager = new PrefManager(getApplicationContext());

                        // make first time launch TRUE
                        prefManager.setFirstTimeLaunch(true);
                        prefManager.logout();
                        startActivity(new Intent(Menu.this, Login.class));
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


package com.pelaporan.mandalajaticare.fragment;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pelaporan.mandalajaticare.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tentang extends Fragment {

    TextView tvAppVersion;


    public Tentang() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tentang, container, false);

        tvAppVersion = (TextView) view.findViewById(R.id.tvAppVer);
        String version = "1.0";

        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        tvAppVersion.setText("v " + version);

        return view;
    }

}

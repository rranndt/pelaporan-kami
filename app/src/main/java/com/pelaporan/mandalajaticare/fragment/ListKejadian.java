package com.pelaporan.mandalajaticare.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.pelaporan.mandalajaticare.activity.Login;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.config.Server;

public class ListKejadian extends Fragment  {


    int success;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";
    WebView wv;
    ProgressBar progressBar;

    SharedPreferences username;



    String ambilusername;

    public ListKejadian() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listkejadian, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        wv = (WebView)view.findViewById(R.id.webView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        username = getActivity().getSharedPreferences(
                Login.SATU,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);
        ambilusername=username.getString(
                Login.KEY_SATU, "Kandidat");

        wv.setWebViewClient(new myWebClient());
        wv.setInitialScale(1);
        wv.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setBuiltInZoomControls(false);
        wv.getSettings().setDisplayZoomControls(false);
        wv.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);

        System.out.println(Server.URL+"android/listkejadian.php?username="+ambilusername);
        wv.loadUrl(Server.URL+"android/listkejadian.php?username="+ambilusername);




        return view;

    }

    public  class myWebClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);

            progressBar.setVisibility(View.GONE);
        }

    }




}

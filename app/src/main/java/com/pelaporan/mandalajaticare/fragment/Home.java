package com.pelaporan.mandalajaticare.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SwitchCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.pelaporan.mandalajaticare.activity.Login;
import com.pelaporan.mandalajaticare.Menu;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.config.GeofenceTransitionService;
import com.pelaporan.mandalajaticare.controller.AppController;
import com.pelaporan.mandalajaticare.config.Server;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ss.com.bannerslider.banners.RemoteBanner;
import ss.com.bannerslider.events.OnBannerClickListener;
import ss.com.bannerslider.views.BannerSlider;
import ss.com.bannerslider.views.indicators.IndicatorShape;


public class Home extends Fragment implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status> {
    private static final String TAG = Menu.class.getSimpleName();

    Double lat2, long2, lat1, long1;
    TelephonyManager telephonyManager;
    TextView alamatt;
    String ambilusername;
    String alamat;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private TextView textLat, textLong;
    private MapFragment mapFragment;
    private static final String TAG_MESSAGE = "message";
    private SharedPreferences prefssatu;
    private boolean dialogShow;
    private BannerSlider bannerSlider;
    private static View view;

    public Home() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NewApi")

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setTitle("Dashboard");

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.home, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }

        prefssatu = this.getActivity().getSharedPreferences(
                Login.SATU,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);

        ambilusername = prefssatu.getString(
                Login.KEY_SATU, "NA");

        bannerSlider = (BannerSlider) view.findViewById(R.id.banner_slider1);
        alamatt = (TextView) view.findViewById(R.id.alamat);
        textLat = (TextView) view.findViewById(R.id.lat);
        textLong = (TextView) view.findViewById(R.id.lon);

        lat2 = 0.0;
        long2 = 0.0;

        loadJson();
        // initialize GoogleMaps
        initGMaps();
        // create GoogleApiClient
        createGoogleApi();

        dialogShow = false;
        return view;
    }

    private void loadJson() {
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, Server.URL + "android/slider.php", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        pd.cancel();
//                        p1.setVisibility(View.GONE);
                        System.out.println(response);
                        if (response == null || response.equals("[]") || response.equals(null) || response.equals("")
                                || response.toString().equals("") || response.toString().equals("[]")) {
                        } else {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject data = response.getJSONObject(i);
                                    bannerSlider.addBanner(new RemoteBanner(Server.URL + "assets/slider/" + data.getString("Slider")
                                    ));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pd.cancel();
//                        p1.setVisibility(View.GONE);
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });
        AppController.getInstance().addToRequestQueue(reqData);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.map);
        if (fragment != null)
            getFragmentManager().beginTransaction().remove(fragment).commit();
    }

    private final int REQ_PERMISSION = 999;

    // Create GoogleApiClient instance
    private void createGoogleApi() {
        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    public void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }

    // Check for permission to access Location
    private boolean checkPermission() {
        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    getLastKnownLocation();

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    // Initialize GoogleMaps
    private void initGMaps() {
        mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Callback called when Map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady()");
        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
        googleMap.clear();
//        map.setMyLocationEnabled(true);
//        polyMap();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.d(TAG, "onMapClick(" + latLng + ")");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d(TAG, "onMarkerClickListener: " + marker.getPosition());
        return false;
    }

    private LocationRequest locationRequest;
    // Defined in mili seconds.
    // This number in extremely low, and should be used only for debug
    private final int UPDATE_INTERVAL = 20000;
    private final int FASTEST_INTERVAL = 15000;

    // Start location Updates
    private void startLocationUpdates() {
        Log.i(TAG, "startLocationUpdates()");
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        UpdateLokasi();

        if (checkPermission())
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                    locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged [" + location + "]");
        lastLocation = location;
        if (!dialogShow && isMockLocationOn(location, getActivity())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Anda Terindikasi Menggunakan Lokasi Palsu!")
                    .setCancelable(false)
                    //.setPositiveButton("Buka Setting", new DialogInterface.OnClickListener() {
                    //    public void onClick(DialogInterface dialog, int id) {
                    //        //do things
                    //        Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                    //        startActivity(intent);
                    //    }
                    //});
                    .setNegativeButton("Buka Setting", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            dialogShow = false;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            dialogShow = false;
                            getActivity().finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            dialogShow = true;
        } else {
            writeActualLocation(location);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected()");
        getLastKnownLocation();
    }

    // GoogleApiClient.ConnectionCallbacks suspended
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "onConnectionSuspended()");
    }

    // GoogleApiClient.OnConnectionFailedListener fail
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "onConnectionFailed()");
    }

    // Get last known location
    private void getLastKnownLocation() {
        Log.d(TAG, "getLastKnownLocation()");
        if (checkPermission()) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                Log.i(TAG, "LasKnown location. " +
                        "Long: " + lastLocation.getLongitude() +
                        " | Lat: " + lastLocation.getLatitude());
                writeLastLocation();
                startLocationUpdates();
            } else {
                Log.w(TAG, "No location retrieved yet");
                startLocationUpdates();
            }
        } else askPermission();
    }

    private void writeActualLocation(Location location) {
        textLat.setText("Latitude: " + location.getLatitude());
        textLong.setText("Longitude: " + location.getLongitude());

        lat1 = location.getLatitude();
        long1 = location.getLongitude();

        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            // Ambil Data ke DB
            alamat = (addresses.get(0).getAddressLine(0));
            // Tampilkan alamat di home
            alamatt.setText((addresses.get(0).getAddressLine(0)));

        } catch (Exception e) {

        }
        markerLocation(new LatLng(location.getLatitude(), location.getLongitude()));
    }

    private void writeLastLocation() {
        writeActualLocation(lastLocation);
    }

    private Marker locationMarker;

    private void markerLocation(LatLng latLng) {
        Log.i(TAG, "markerLocation(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.markerpelapor))
                .title(title);
        if (map != null) {
            if (locationMarker != null)
                locationMarker.remove();
            locationMarker = map.addMarker(markerOptions);
            float zoom = 16f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
//            map.setMyLocationEnabled(true);
            map.animateCamera(cameraUpdate);
            polyMap();
        }
    }

    @Override
    public void onResult(@NonNull Status status) {
        Log.i(TAG, "onResult: " + status);
        if (status.isSuccess()) {
        } else {
        }
    }

    private void polyMap() {
            Polygon polygon = map.addPolygon(new PolygonOptions()
                    .add(new LatLng(-6.90411,107.66661), new LatLng(-6.9041,107.66644), new LatLng(-6.90408,107.66624), new LatLng(-6.90405,107.66605), new LatLng(-6.90398,107.66556), new LatLng(-6.90391,107.6655), new LatLng(-6.90396,107.6654), new LatLng(-6.90388,107.66439), new LatLng(-6.90384,107.66399), new LatLng(-6.90381,107.6637),
                            new LatLng(-6.9037,107.66238), new LatLng(-6.90375,107.66312), new LatLng(-6.90354,107.66164), new LatLng(-6.90325,107.66127), new LatLng(-6.90233,107.6605), new LatLng(-6.90224,107.65811), new LatLng(-6.90216,107.65768), new LatLng(-6.90181,107.65768), new LatLng(-6.90148,107.65769), new LatLng(-6.9011,107.65765),
                            new LatLng(-6.90071,107.65762), new LatLng(-6.90033,107.65755), new LatLng(-6.90015,107.65753), new LatLng(-6.90008,107.65745), new LatLng(-6.90006,107.65733), new LatLng(-6.90002,107.65721), new LatLng(-6.89984,107.65721), new LatLng(-6.89972,107.65722), new LatLng(-6.89963,107.65722), new LatLng(-6.89956,107.65719),
                            new LatLng(-6.8995,107.65709), new LatLng(-6.89933,107.65704), new LatLng(-6.89928,107.65691), new LatLng(-6.89914,107.65685), new LatLng(-6.89907,107.65674), new LatLng(-6.89896,107.65666), new LatLng(-6.89878,107.65659), new LatLng(-6.89874,107.65666), new LatLng(-6.89864,107.65667), new LatLng(-6.89853,107.65661),
                            new LatLng(-6.89847,107.65659), new LatLng(-6.89833,107.65659), new LatLng(-6.89824,107.65662), new LatLng(-6.89805,107.65671), new LatLng(-6.89793,107.65678), new LatLng(-6.89782,107.65686), new LatLng(-6.89763,107.65685), new LatLng(-6.8975,107.65678), new LatLng(-6.89738,107.65683), new LatLng(-6.89717,107.65694),
                            new LatLng(-6.89705,107.65709), new LatLng(-6.89693,107.65722), new LatLng(-6.89683,107.65736), new LatLng(-6.89663,107.65739), new LatLng(-6.89653,107.65742), new LatLng(-6.89646,107.65746), new LatLng(-6.89636,107.65758), new LatLng(-6.89629,107.65775), new LatLng(-6.89618,107.65776), new LatLng(-6.89611,107.65768),
                            new LatLng(-6.89601,107.65768), new LatLng(-6.89588,107.65749), new LatLng(-6.89573,107.65749), new LatLng(-6.89566,107.65757), new LatLng(-6.89561,107.65765), new LatLng(-6.89532,107.65771), new LatLng(-6.89531,107.65783), new LatLng(-6.89515,107.65788), new LatLng(-6.89501,107.65805), new LatLng(-6.8949,107.65805),
                            new LatLng(-6.89473,107.65826), new LatLng(-6.8946,107.65834), new LatLng(-6.89449,107.65839), new LatLng(-6.89427,107.65857), new LatLng(-6.89415,107.65872), new LatLng(-6.89412,107.6588), new LatLng(-6.89414,107.65892), new LatLng(-6.89415,107.65925), new LatLng(-6.89405,107.6596), new LatLng(-6.89403,107.6599),
                            new LatLng(-6.89392,107.66008), new LatLng(-6.8937,107.66026), new LatLng(-6.89334,107.66052), new LatLng(-6.89322,107.66055), new LatLng(-6.89301,107.66055), new LatLng(-6.89211,107.66059), new LatLng(-6.89198,107.66063), new LatLng(-6.8919,107.6607), new LatLng(-6.89167,107.66082), new LatLng(-6.89158,107.66089),
                            new LatLng(-6.89133,107.66103), new LatLng(-6.89124,107.66107), new LatLng(-6.8911,107.66106), new LatLng(-6.89086,107.6611), new LatLng(-6.89058,107.6612), new LatLng(-6.8901,107.66134), new LatLng(-6.88982,107.66139), new LatLng(-6.88916,107.6616), new LatLng(-6.88907,107.66168), new LatLng(-6.88892,107.6618),

                            new LatLng(-6.88884,107.66192), new LatLng(-6.88875,107.66229), new LatLng(-6.88875,107.6624), new LatLng(-6.88883,107.66241), new LatLng(-6.88889,107.66253), new LatLng(-6.8889,107.66267), new LatLng(-6.88883,107.66286), new LatLng(-6.88852,107.6634), new LatLng(-6.88848,107.6638), new LatLng(-6.88836,107.66414),
                            new LatLng(-6.88815,107.66433), new LatLng(-6.88786,107.66452), new LatLng(-6.88765,107.66461), new LatLng(-6.88724,107.66485), new LatLng(-6.88709,107.66512), new LatLng(-6.88683,107.66517), new LatLng(-6.88673,107.66509), new LatLng(-6.88655,107.66497), new LatLng(-6.88646,107.66486), new LatLng(-6.88624,107.66491),
                            new LatLng(-6.88589,107.66504), new LatLng(-6.88547,107.66538), new LatLng(-6.88538,107.66546), new LatLng(-6.88538,107.66554), new LatLng(-6.88525,107.66565), new LatLng(-6.88515,107.66566), new LatLng(-6.88531,107.66587), new LatLng(-6.88553,107.66578), new LatLng(-6.88578,107.66571), new LatLng(-6.88641,107.6656),
                            new LatLng(-6.88673,107.66556), new LatLng(-6.88682,107.66554), new LatLng(-6.88759,107.66519), new LatLng(-6.88774,107.66514), new LatLng(-6.88784,107.6651), new LatLng(-6.88799,107.66508), new LatLng(-6.88805,107.66501), new LatLng(-6.88808,107.66493), new LatLng(-6.88877,107.66463), new LatLng(-6.88883,107.66463),
                            new LatLng(-6.88893,107.66464), new LatLng(-6.88902,107.6647), new LatLng(-6.88907,107.66476), new LatLng(-6.88911,107.66483), new LatLng(-6.88925,107.66483), new LatLng(-6.88934,107.66482), new LatLng(-6.88946,107.66475), new LatLng(-6.88952,107.66469), new LatLng(-6.88963,107.66443), new LatLng(-6.88969,107.6645),
                            new LatLng(-6.88969,107.66473), new LatLng(-6.88972,107.66482), new LatLng(-6.8901,107.66533), new LatLng(-6.88957,107.66571), new LatLng(-6.88939,107.66583), new LatLng(-6.88914,107.66597), new LatLng(-6.88857,107.66623), new LatLng(-6.88829,107.66637), new LatLng(-6.88801,107.6665), new LatLng(-6.8875,107.66685),
                            new LatLng(-6.8872,107.66719), new LatLng(-6.88692,107.66736), new LatLng(-6.88672,107.66749), new LatLng(-6.88669,107.66756), new LatLng(-6.88666,107.66777), new LatLng(-6.88659,107.66786), new LatLng(-6.88653,107.66788), new LatLng(-6.8864,107.66796), new LatLng(-6.8863,107.66802), new LatLng(-6.88621,107.66809),
                            new LatLng(-6.8858,107.66845), new LatLng(-6.88572,107.66849), new LatLng(-6.88566,107.66852), new LatLng(-6.88555,107.66857), new LatLng(-6.88545,107.66868), new LatLng(-6.8853,107.6688), new LatLng(-6.88471,107.6691), new LatLng(-6.88461,107.66913), new LatLng(-6.88456,107.66918), new LatLng(-6.88429,107.66961),
                            new LatLng(-6.884,107.67008), new LatLng(-6.88399,107.67019), new LatLng(-6.88406,107.67026), new LatLng(-6.88428,107.6702), new LatLng(-6.88435,107.67013), new LatLng(-6.88467,107.66999), new LatLng(-6.88478,107.66974), new LatLng(-6.88487,107.66976), new LatLng(-6.88497,107.66975), new LatLng(-6.88504,107.66966),
                            new LatLng(-6.88512,107.66959), new LatLng(-6.8853,107.66949), new LatLng(-6.88539,107.66965), new LatLng(-6.88562,107.66995), new LatLng(-6.8857,107.6699), new LatLng(-6.88579,107.66991), new LatLng(-6.88589,107.66995), new LatLng(-6.88598,107.67), new LatLng(-6.88607,107.67001), new LatLng(-6.88622,107.67),

                            new LatLng(-6.88632,107.66998), new LatLng(-6.8864,107.66996), new LatLng(-6.88705,107.66969), new LatLng(-6.88745,107.66955), new LatLng(-6.88783,107.66937), new LatLng(-6.88803,107.66921), new LatLng(-6.88803,107.66902), new LatLng(-6.88804,107.66895), new LatLng(-6.8883,107.66884), new LatLng(-6.88858,107.66878),
                            new LatLng(-6.88865,107.66876), new LatLng(-6.8889,107.66868), new LatLng(-6.88933,107.66859), new LatLng(-6.88949,107.66853), new LatLng(-6.88983,107.66836), new LatLng(-6.89005,107.66827), new LatLng(-6.89025,107.66819), new LatLng(-6.89095,107.66804), new LatLng(-6.89103,107.66811), new LatLng(-6.89105,107.66829),
                            new LatLng(-6.89114,107.66865), new LatLng(-6.89121,107.66873), new LatLng(-6.8912,107.6689), new LatLng(-6.89116,107.66907), new LatLng(-6.89116,107.66931), new LatLng(-6.89119,107.67045), new LatLng(-6.89125,107.67144), new LatLng(-6.89123,107.6716), new LatLng(-6.89129,107.67175), new LatLng(-6.89132,107.67191),
                            new LatLng(-6.89137,107.67198), new LatLng(-6.89144,107.67208), new LatLng(-6.89154,107.67212), new LatLng(-6.89179,107.67215), new LatLng(-6.89183,107.67223), new LatLng(-6.89182,107.67232), new LatLng(-6.89191,107.67261), new LatLng(-6.89205,107.67281), new LatLng(-6.8921,107.67291), new LatLng(-6.89216,107.67311),
                            new LatLng(-6.89214,107.67319), new LatLng(-6.89204,107.67322), new LatLng(-6.89202,107.67329), new LatLng(-6.89194,107.67334), new LatLng(-6.89195,107.67346), new LatLng(-6.8919,107.67378), new LatLng(-6.89189,107.6739), new LatLng(-6.89194,107.674), new LatLng(-6.89204,107.67451), new LatLng(-6.89212,107.67459),
                            new LatLng(-6.89212,107.67479), new LatLng(-6.89205,107.675), new LatLng(-6.89209,107.67508), new LatLng(-6.89209,107.67527), new LatLng(-6.89213,107.67535), new LatLng(-6.89233,107.67612), new LatLng(-6.8924,107.67633), new LatLng(-6.89243,107.67646), new LatLng(-6.89241,107.67656), new LatLng(-6.89248,107.67679),
                            new LatLng(-6.89251,107.67715), new LatLng(-6.89253,107.67731), new LatLng(-6.89254,107.67741), new LatLng(-6.89269,107.67769), new LatLng(-6.89271,107.67776), new LatLng(-6.89269,107.67784), new LatLng(-6.8927,107.67802), new LatLng(-6.89182,107.67837), new LatLng(-6.89148,107.67843), new LatLng(-6.89134,107.67853),
                            new LatLng(-6.89124,107.67864), new LatLng(-6.89097,107.67922), new LatLng(-6.89085,107.6795), new LatLng(-6.89078,107.67957), new LatLng(-6.89045,107.67957), new LatLng(-6.89018,107.67957), new LatLng(-6.88988,107.67963), new LatLng(-6.88966,107.67973), new LatLng(-6.88966,107.67999), new LatLng(-6.88955,107.68017),
                            new LatLng(-6.88944,107.68043), new LatLng(-6.8894,107.68057), new LatLng(-6.8894,107.68068), new LatLng(-6.88942,107.68077), new LatLng(-6.88948,107.68088), new LatLng(-6.88958,107.68096), new LatLng(-6.88974,107.68104), new LatLng(-6.88999,107.68108), new LatLng(-6.89029,107.68125), new LatLng(-6.89047,107.68114),
                            new LatLng(-6.89062,107.68108), new LatLng(-6.89107,107.68069), new LatLng(-6.89117,107.68077), new LatLng(-6.89118,107.68085), new LatLng(-6.89138,107.68121), new LatLng(-6.89143,107.68147), new LatLng(-6.89155,107.68184), new LatLng(-6.89161,107.682), new LatLng(-6.89164,107.6824), new LatLng(-6.89166,107.6825),

                            new LatLng(-6.89178,107.68263), new LatLng(-6.89187,107.68268), new LatLng(-6.89197,107.68263), new LatLng(-6.89216,107.68253), new LatLng(-6.89233,107.6825), new LatLng(-6.89247,107.68256), new LatLng(-6.89252,107.68262), new LatLng(-6.89255,107.68293), new LatLng(-6.89252,107.68305), new LatLng(-6.89253,107.68318),
                            new LatLng(-6.89256,107.68331), new LatLng(-6.89263,107.68343), new LatLng(-6.89271,107.6835), new LatLng(-6.89275,107.68365), new LatLng(-6.89283,107.6838), new LatLng(-6.89287,107.68397), new LatLng(-6.89289,107.68415), new LatLng(-6.89301,107.68412), new LatLng(-6.89312,107.68408), new LatLng(-6.89328,107.68401),
                            new LatLng(-6.89336,107.68396), new LatLng(-6.89344,107.6839), new LatLng(-6.89351,107.6842), new LatLng(-6.8936,107.68428), new LatLng(-6.8937,107.68431), new LatLng(-6.89409,107.68445), new LatLng(-6.89425,107.68451), new LatLng(-6.89438,107.68458), new LatLng(-6.89448,107.68459), new LatLng(-6.8949,107.68461),
                            new LatLng(-6.89508,107.68461), new LatLng(-6.89543,107.6846), new LatLng(-6.89559,107.68461), new LatLng(-6.89587,107.68537), new LatLng(-6.89613,107.68595), new LatLng(-6.89626,107.68615), new LatLng(-6.89632,107.6862), new LatLng(-6.89639,107.68636), new LatLng(-6.8964,107.68644), new LatLng(-6.89651,107.68655),
                            new LatLng(-6.89654,107.68667), new LatLng(-6.89661,107.68672), new LatLng(-6.89661,107.68684), new LatLng(-6.89671,107.68685), new LatLng(-6.89679,107.68684), new LatLng(-6.89688,107.68684), new LatLng(-6.89694,107.68687), new LatLng(-6.89719,107.68697), new LatLng(-6.89732,107.687), new LatLng(-6.89741,107.68697),
                            new LatLng(-6.89747,107.68693), new LatLng(-6.89766,107.68676), new LatLng(-6.8979,107.68666), new LatLng(-6.89797,107.68667), new LatLng(-6.89825,107.68655), new LatLng(-6.89863,107.68658), new LatLng(-6.89874,107.68656), new LatLng(-6.89883,107.6865), new LatLng(-6.89898,107.68645), new LatLng(-6.89906,107.68646),
                            new LatLng(-6.89928,107.68651), new LatLng(-6.89939,107.68656), new LatLng(-6.8995,107.6865), new LatLng(-6.89964,107.68647), new LatLng(-6.89972,107.6864), new LatLng(-6.89984,107.68639), new LatLng(-6.89997,107.68633), new LatLng(-6.90019,107.6865), new LatLng(-6.90027,107.68648), new LatLng(-6.90051,107.68647),
                            new LatLng(-6.90058,107.68649), new LatLng(-6.90077,107.68649), new LatLng(-6.90097,107.68648), new LatLng(-6.90107,107.68649), new LatLng(-6.90115,107.68647), new LatLng(-6.90129,107.68648), new LatLng(-6.90129,107.68628), new LatLng(-6.90141,107.68629), new LatLng(-6.90155,107.68635), new LatLng(-6.90161,107.6864),
                            new LatLng(-6.90168,107.68646), new LatLng(-6.90183,107.68661), new LatLng(-6.9019,107.68668), new LatLng(-6.90203,107.68684), new LatLng(-6.9021,107.68689), new LatLng(-6.90217,107.68693), new LatLng(-6.90228,107.68704), new LatLng(-6.90238,107.68713), new LatLng(-6.90246,107.6872), new LatLng(-6.90273,107.68732),
                            new LatLng(-6.90285,107.68742), new LatLng(-6.90293,107.68743), new LatLng(-6.90308,107.68747), new LatLng(-6.90319,107.68751), new LatLng(-6.9033,107.68749), new LatLng(-6.90346,107.68737), new LatLng(-6.90362,107.68725), new LatLng(-6.90395,107.68729), new LatLng(-6.90413,107.68725), new LatLng(-6.90428,107.68713),

                            new LatLng(-6.90432,107.68706), new LatLng(-6.90441,107.68691), new LatLng(-6.90453,107.68682), new LatLng(-6.90467,107.68672), new LatLng(-6.90486,107.68663), new LatLng(-6.90496,107.6866), new LatLng(-6.90505,107.68656), new LatLng(-6.90532,107.68641), new LatLng(-6.9057,107.68634), new LatLng(-6.90582,107.68638),
                            new LatLng(-6.90591,107.68636), new LatLng(-6.90606,107.68626), new LatLng(-6.90625,107.68609), new LatLng(-6.90633,107.68597), new LatLng(-6.90715,107.68616), new LatLng(-6.90708,107.68578), new LatLng(-6.90697,107.68553), new LatLng(-6.90628,107.6839), new LatLng(-6.90587,107.68293), new LatLng(-6.90558,107.68233),
                            new LatLng(-6.9055,107.68216), new LatLng(-6.90544,107.68203), new LatLng(-6.90524,107.68154), new LatLng(-6.90507,107.68043), new LatLng(-6.90498,107.67969), new LatLng(-6.90477,107.67787), new LatLng(-6.90473,107.67679), new LatLng(-6.90468,107.67609), new LatLng(-6.90464,107.67579), new LatLng(-6.9046,107.67514),
                            new LatLng(-6.90454,107.67483), new LatLng(-6.90454,107.67463), new LatLng(-6.90457,107.67431), new LatLng(-6.90482,107.67331), new LatLng(-6.90488,107.67302), new LatLng(-6.90487,107.67287), new LatLng(-6.9048,107.67268), new LatLng(-6.90466,107.6724), new LatLng(-6.90441,107.6719), new LatLng(-6.90426,107.67155),
                            new LatLng(-6.90422,107.67111), new LatLng(-6.9042,107.66779), new LatLng(-6.90419,107.66716))
                    .strokeColor(Color.BLUE)
                    .strokeWidth(1.5f)
                    .fillColor(0x220000FF));
    }

    private void UpdateLokasi() {
        System.out.println(Server.URL + "android/updatelokasi.php");

        //menampilkan progress dialog
        //final ProgressDialog loading = ProgressDialog.show(getActivity(), "Update Location...", " Mohon Tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL + "android/updatelokasi.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jObj = new JSONObject(response);

                            Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        //loading.dismiss();

                        //menampilkan toast
                        Toast.makeText(getActivity(), "Pastikan GPS Telah Aktif !!", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                System.out.println(ambilusername);
                System.out.println(long1.toString());
                System.out.println(lat1.toString());

                params.put("username", ambilusername.trim());
                params.put("alamat", alamat.trim());
                params.put("longitude", long1.toString());
                params.put("latitude", lat1.toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "json");
    }

    public boolean isMockLocationOn(Location location, Context context) {
//        return false;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return location.isFromMockProvider();
        } else {
            String mockLocation = "0";
            try {
                mockLocation = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return !mockLocation.equals("0");
        }
    }
}
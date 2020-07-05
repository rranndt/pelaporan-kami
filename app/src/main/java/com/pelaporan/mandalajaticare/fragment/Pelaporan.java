package com.pelaporan.mandalajaticare.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import  android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;
import com.pelaporan.mandalajaticare.activity.Login;
import com.pelaporan.mandalajaticare.Menu;
import com.pelaporan.mandalajaticare.R;
import com.pelaporan.mandalajaticare.config.Category;
import com.pelaporan.mandalajaticare.controller.AppController;
import com.pelaporan.mandalajaticare.config.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class Pelaporan extends Fragment implements AdapterView.OnItemSelectedListener, LocationListener {

    private EditText jenis;

    ImageView imgAnim;
    LinearLayout mainLayout;
    String kode1;
    SharedPreferences prefssatu;
    TextView namaimage1, namaimage2;
    EditText detailkejadian;
    Button pilihfoto1,pilihfoto2,simpan;
    ProgressDialog pd;
    String ambilusername;

    private ArrayList<Category> tahunlist;
    private Spinner tahunspinner;

    int success;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_TAKE_PHOTO = 2;
    private int PICK_IMAGE_REQUEST1 = 3;
    private int REQUEST_TAKE_PHOTO1 = 4;
    public Uri mUri;
    public Bitmap bitmap, bitmap1;
    public ImageView image1, image2;

    private boolean dialogShow;
    private LocationManager locationManager;

    private boolean in_mandalajati;
    private List<LatLng> kec_mandalajati;

    private GuideView mGuideView;
    private GuideView.Builder builder;

    public Pelaporan() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        kec_mandalajati = new ArrayList<>();
        kec_mandalajati.add(new LatLng(-6.90411,107.66661)); kec_mandalajati.add(new LatLng(-6.9041,107.66644)); kec_mandalajati.add(new LatLng(-6.90408,107.66624)); kec_mandalajati.add(new LatLng(-6.90405,107.66605)); kec_mandalajati.add(new LatLng(-6.90398,107.66556)); kec_mandalajati.add(new LatLng(-6.90391,107.6655)); kec_mandalajati.add(new LatLng(-6.90396,107.6654)); kec_mandalajati.add(new LatLng(-6.90388,107.66439)); kec_mandalajati.add(new LatLng(-6.90384,107.66399)); kec_mandalajati.add(new LatLng(-6.90381,107.6637));
        kec_mandalajati.add(new LatLng(-6.9037,107.66238)); kec_mandalajati.add(new LatLng(-6.90375,107.66312)); kec_mandalajati.add(new LatLng(-6.90354,107.66164)); kec_mandalajati.add(new LatLng(-6.90325,107.66127)); kec_mandalajati.add(new LatLng(-6.90233,107.6605)); kec_mandalajati.add(new LatLng(-6.90224,107.65811)); kec_mandalajati.add(new LatLng(-6.90216,107.65768)); kec_mandalajati.add(new LatLng(-6.90181,107.65768)); kec_mandalajati.add(new LatLng(-6.90148,107.65769)); kec_mandalajati.add(new LatLng(-6.9011,107.65765));
        kec_mandalajati.add(new LatLng(-6.90071,107.65762)); kec_mandalajati.add(new LatLng(-6.90033,107.65755)); kec_mandalajati.add(new LatLng(-6.90015,107.65753)); kec_mandalajati.add(new LatLng(-6.90008,107.65745)); kec_mandalajati.add(new LatLng(-6.90006,107.65733)); kec_mandalajati.add(new LatLng(-6.90002,107.65721)); kec_mandalajati.add(new LatLng(-6.89984,107.65721)); kec_mandalajati.add(new LatLng(-6.89972,107.65722)); kec_mandalajati.add(new LatLng(-6.89963,107.65722)); kec_mandalajati.add(new LatLng(-6.89956,107.65719));
        kec_mandalajati.add(new LatLng(-6.8995,107.65709)); kec_mandalajati.add(new LatLng(-6.89933,107.65704)); kec_mandalajati.add(new LatLng(-6.89928,107.65691)); kec_mandalajati.add(new LatLng(-6.89914,107.65685)); kec_mandalajati.add(new LatLng(-6.89907,107.65674)); kec_mandalajati.add(new LatLng(-6.89896,107.65666)); kec_mandalajati.add(new LatLng(-6.89878,107.65659)); kec_mandalajati.add(new LatLng(-6.89874,107.65666)); kec_mandalajati.add(new LatLng(-6.89864,107.65667)); kec_mandalajati.add(new LatLng(-6.89853,107.65661));
        kec_mandalajati.add(new LatLng(-6.89847,107.65659)); kec_mandalajati.add(new LatLng(-6.89833,107.65659)); kec_mandalajati.add(new LatLng(-6.89824,107.65662)); kec_mandalajati.add(new LatLng(-6.89805,107.65671)); kec_mandalajati.add(new LatLng(-6.89793,107.65678)); kec_mandalajati.add(new LatLng(-6.89782,107.65686)); kec_mandalajati.add(new LatLng(-6.89763,107.65685)); kec_mandalajati.add(new LatLng(-6.8975,107.65678)); kec_mandalajati.add(new LatLng(-6.89738,107.65683)); kec_mandalajati.add(new LatLng(-6.89717,107.65694));
        kec_mandalajati.add(new LatLng(-6.89705,107.65709)); kec_mandalajati.add(new LatLng(-6.89693,107.65722)); kec_mandalajati.add(new LatLng(-6.89683,107.65736)); kec_mandalajati.add(new LatLng(-6.89663,107.65739)); kec_mandalajati.add(new LatLng(-6.89653,107.65742)); kec_mandalajati.add(new LatLng(-6.89646,107.65746)); kec_mandalajati.add(new LatLng(-6.89636,107.65758)); kec_mandalajati.add(new LatLng(-6.89629,107.65775)); kec_mandalajati.add(new LatLng(-6.89618,107.65776)); kec_mandalajati.add(new LatLng(-6.89611,107.65768));
        kec_mandalajati.add(new LatLng(-6.89601,107.65768)); kec_mandalajati.add(new LatLng(-6.89588,107.65749)); kec_mandalajati.add(new LatLng(-6.89573,107.65749)); kec_mandalajati.add(new LatLng(-6.89566,107.65757)); kec_mandalajati.add(new LatLng(-6.89561,107.65765)); kec_mandalajati.add(new LatLng(-6.89532,107.65771)); kec_mandalajati.add(new LatLng(-6.89531,107.65783)); kec_mandalajati.add(new LatLng(-6.89515,107.65788)); kec_mandalajati.add(new LatLng(-6.89501,107.65805)); kec_mandalajati.add(new LatLng(-6.8949,107.65805));
        kec_mandalajati.add(new LatLng(-6.89473,107.65826)); kec_mandalajati.add(new LatLng(-6.8946,107.65834)); kec_mandalajati.add(new LatLng(-6.89449,107.65839)); kec_mandalajati.add(new LatLng(-6.89427,107.65857)); kec_mandalajati.add(new LatLng(-6.89415,107.65872)); kec_mandalajati.add(new LatLng(-6.89412,107.6588)); kec_mandalajati.add(new LatLng(-6.89414,107.65892)); kec_mandalajati.add(new LatLng(-6.89415,107.65925)); kec_mandalajati.add(new LatLng(-6.89405,107.6596)); kec_mandalajati.add(new LatLng(-6.89403,107.6599));
        kec_mandalajati.add(new LatLng(-6.89392,107.66008)); kec_mandalajati.add(new LatLng(-6.8937,107.66026)); kec_mandalajati.add(new LatLng(-6.89334,107.66052)); kec_mandalajati.add(new LatLng(-6.89322,107.66055)); kec_mandalajati.add(new LatLng(-6.89301,107.66055)); kec_mandalajati.add(new LatLng(-6.89211,107.66059)); kec_mandalajati.add(new LatLng(-6.89198,107.66063)); kec_mandalajati.add(new LatLng(-6.8919,107.6607)); kec_mandalajati.add(new LatLng(-6.89167,107.66082)); kec_mandalajati.add(new LatLng(-6.89158,107.66089));
        kec_mandalajati.add(new LatLng(-6.89133,107.66103)); kec_mandalajati.add(new LatLng(-6.89124,107.66107)); kec_mandalajati.add(new LatLng(-6.8911,107.66106)); kec_mandalajati.add(new LatLng(-6.89086,107.6611)); kec_mandalajati.add(new LatLng(-6.89058,107.6612)); kec_mandalajati.add(new LatLng(-6.8901,107.66134)); kec_mandalajati.add(new LatLng(-6.88982,107.66139)); kec_mandalajati.add(new LatLng(-6.88916,107.6616)); kec_mandalajati.add(new LatLng(-6.88907,107.66168)); kec_mandalajati.add(new LatLng(-6.88892,107.6618));

        kec_mandalajati.add(new LatLng(-6.88884,107.66192)); kec_mandalajati.add(new LatLng(-6.88875,107.66229)); kec_mandalajati.add(new LatLng(-6.88875,107.6624)); kec_mandalajati.add(new LatLng(-6.88883,107.66241)); kec_mandalajati.add(new LatLng(-6.88889,107.66253)); kec_mandalajati.add(new LatLng(-6.8889,107.66267)); kec_mandalajati.add(new LatLng(-6.88883,107.66286)); kec_mandalajati.add(new LatLng(-6.88852,107.6634)); kec_mandalajati.add(new LatLng(-6.88848,107.6638)); kec_mandalajati.add(new LatLng(-6.88836,107.66414));
        kec_mandalajati.add(new LatLng(-6.88815,107.66433)); kec_mandalajati.add(new LatLng(-6.88786,107.66452)); kec_mandalajati.add(new LatLng(-6.88765,107.66461)); kec_mandalajati.add(new LatLng(-6.88724,107.66485)); kec_mandalajati.add(new LatLng(-6.88709,107.66512)); kec_mandalajati.add(new LatLng(-6.88683,107.66517)); kec_mandalajati.add(new LatLng(-6.88673,107.66509)); kec_mandalajati.add(new LatLng(-6.88655,107.66497)); kec_mandalajati.add(new LatLng(-6.88646,107.66486)); kec_mandalajati.add(new LatLng(-6.88624,107.66491));
        kec_mandalajati.add(new LatLng(-6.88589,107.66504)); kec_mandalajati.add(new LatLng(-6.88547,107.66538)); kec_mandalajati.add(new LatLng(-6.88538,107.66546)); kec_mandalajati.add(new LatLng(-6.88538,107.66554)); kec_mandalajati.add(new LatLng(-6.88525,107.66565)); kec_mandalajati.add(new LatLng(-6.88515,107.66566)); kec_mandalajati.add(new LatLng(-6.88531,107.66587)); kec_mandalajati.add(new LatLng(-6.88553,107.66578)); kec_mandalajati.add(new LatLng(-6.88578,107.66571)); kec_mandalajati.add(new LatLng(-6.88641,107.6656));
        kec_mandalajati.add(new LatLng(-6.88673,107.66556)); kec_mandalajati.add(new LatLng(-6.88682,107.66554)); kec_mandalajati.add(new LatLng(-6.88759,107.66519)); kec_mandalajati.add(new LatLng(-6.88774,107.66514)); kec_mandalajati.add(new LatLng(-6.88784,107.6651)); kec_mandalajati.add(new LatLng(-6.88799,107.66508)); kec_mandalajati.add(new LatLng(-6.88805,107.66501)); kec_mandalajati.add(new LatLng(-6.88808,107.66493)); kec_mandalajati.add(new LatLng(-6.88877,107.66463)); kec_mandalajati.add(new LatLng(-6.88883,107.66463));
        kec_mandalajati.add(new LatLng(-6.88893,107.66464)); kec_mandalajati.add(new LatLng(-6.88902,107.6647)); kec_mandalajati.add(new LatLng(-6.88907,107.66476)); kec_mandalajati.add(new LatLng(-6.88911,107.66483)); kec_mandalajati.add(new LatLng(-6.88925,107.66483)); kec_mandalajati.add(new LatLng(-6.88934,107.66482)); kec_mandalajati.add(new LatLng(-6.88946,107.66475)); kec_mandalajati.add(new LatLng(-6.88952,107.66469)); kec_mandalajati.add(new LatLng(-6.88963,107.66443)); kec_mandalajati.add(new LatLng(-6.88969,107.6645));
        kec_mandalajati.add(new LatLng(-6.88969,107.66473)); kec_mandalajati.add(new LatLng(-6.88972,107.66482)); kec_mandalajati.add(new LatLng(-6.8901,107.66533)); kec_mandalajati.add(new LatLng(-6.88957,107.66571)); kec_mandalajati.add(new LatLng(-6.88939,107.66583)); kec_mandalajati.add(new LatLng(-6.88914,107.66597)); kec_mandalajati.add(new LatLng(-6.88857,107.66623)); kec_mandalajati.add(new LatLng(-6.88829,107.66637)); kec_mandalajati.add(new LatLng(-6.88801,107.6665)); kec_mandalajati.add(new LatLng(-6.8875,107.66685));
        kec_mandalajati.add(new LatLng(-6.8872,107.66719)); kec_mandalajati.add(new LatLng(-6.88692,107.66736)); kec_mandalajati.add(new LatLng(-6.88672,107.66749)); kec_mandalajati.add(new LatLng(-6.88669,107.66756)); kec_mandalajati.add(new LatLng(-6.88666,107.66777)); kec_mandalajati.add(new LatLng(-6.88659,107.66786)); kec_mandalajati.add(new LatLng(-6.88653,107.66788)); kec_mandalajati.add(new LatLng(-6.8864,107.66796)); kec_mandalajati.add(new LatLng(-6.8863,107.66802)); kec_mandalajati.add(new LatLng(-6.88621,107.66809));
        kec_mandalajati.add(new LatLng(-6.8858,107.66845)); kec_mandalajati.add(new LatLng(-6.88572,107.66849)); kec_mandalajati.add(new LatLng(-6.88566,107.66852)); kec_mandalajati.add(new LatLng(-6.88555,107.66857)); kec_mandalajati.add(new LatLng(-6.88545,107.66868)); kec_mandalajati.add(new LatLng(-6.8853,107.6688)); kec_mandalajati.add(new LatLng(-6.88471,107.6691)); kec_mandalajati.add(new LatLng(-6.88461,107.66913)); kec_mandalajati.add(new LatLng(-6.88456,107.66918)); kec_mandalajati.add(new LatLng(-6.88429,107.66961));
        kec_mandalajati.add(new LatLng(-6.884,107.67008)); kec_mandalajati.add(new LatLng(-6.88399,107.67019)); kec_mandalajati.add(new LatLng(-6.88406,107.67026)); kec_mandalajati.add(new LatLng(-6.88428,107.6702)); kec_mandalajati.add(new LatLng(-6.88435,107.67013)); kec_mandalajati.add(new LatLng(-6.88467,107.66999)); kec_mandalajati.add(new LatLng(-6.88478,107.66974)); kec_mandalajati.add(new LatLng(-6.88487,107.66976)); kec_mandalajati.add(new LatLng(-6.88497,107.66975)); kec_mandalajati.add(new LatLng(-6.88504,107.66966));
        kec_mandalajati.add(new LatLng(-6.88512,107.66959)); kec_mandalajati.add(new LatLng(-6.8853,107.66949)); kec_mandalajati.add(new LatLng(-6.88539,107.66965)); kec_mandalajati.add(new LatLng(-6.88562,107.66995)); kec_mandalajati.add(new LatLng(-6.8857,107.6699)); kec_mandalajati.add(new LatLng(-6.88579,107.66991)); kec_mandalajati.add(new LatLng(-6.88589,107.66995)); kec_mandalajati.add(new LatLng(-6.88598,107.67)); kec_mandalajati.add(new LatLng(-6.88607,107.67001)); kec_mandalajati.add(new LatLng(-6.88622,107.67));

        kec_mandalajati.add(new LatLng(-6.88632,107.66998)); kec_mandalajati.add(new LatLng(-6.8864,107.66996)); kec_mandalajati.add(new LatLng(-6.88705,107.66969)); kec_mandalajati.add(new LatLng(-6.88745,107.66955)); kec_mandalajati.add(new LatLng(-6.88783,107.66937)); kec_mandalajati.add(new LatLng(-6.88803,107.66921)); kec_mandalajati.add(new LatLng(-6.88803,107.66902)); kec_mandalajati.add(new LatLng(-6.88804,107.66895)); kec_mandalajati.add(new LatLng(-6.8883,107.66884)); kec_mandalajati.add(new LatLng(-6.88858,107.66878));
        kec_mandalajati.add(new LatLng(-6.88865,107.66876)); kec_mandalajati.add(new LatLng(-6.8889,107.66868)); kec_mandalajati.add(new LatLng(-6.88933,107.66859)); kec_mandalajati.add(new LatLng(-6.88949,107.66853)); kec_mandalajati.add(new LatLng(-6.88983,107.66836)); kec_mandalajati.add(new LatLng(-6.89005,107.66827)); kec_mandalajati.add(new LatLng(-6.89025,107.66819)); kec_mandalajati.add(new LatLng(-6.89095,107.66804)); kec_mandalajati.add(new LatLng(-6.89103,107.66811)); kec_mandalajati.add(new LatLng(-6.89105,107.66829));
        kec_mandalajati.add(new LatLng(-6.89114,107.66865)); kec_mandalajati.add(new LatLng(-6.89121,107.66873)); kec_mandalajati.add(new LatLng(-6.8912,107.6689)); kec_mandalajati.add(new LatLng(-6.89116,107.66907)); kec_mandalajati.add(new LatLng(-6.89116,107.66931)); kec_mandalajati.add(new LatLng(-6.89119,107.67045)); kec_mandalajati.add(new LatLng(-6.89125,107.67144)); kec_mandalajati.add(new LatLng(-6.89123,107.6716)); kec_mandalajati.add(new LatLng(-6.89129,107.67175)); kec_mandalajati.add(new LatLng(-6.89132,107.67191));
        kec_mandalajati.add(new LatLng(-6.89137,107.67198)); kec_mandalajati.add(new LatLng(-6.89144,107.67208)); kec_mandalajati.add(new LatLng(-6.89154,107.67212)); kec_mandalajati.add(new LatLng(-6.89179,107.67215)); kec_mandalajati.add(new LatLng(-6.89183,107.67223)); kec_mandalajati.add(new LatLng(-6.89182,107.67232)); kec_mandalajati.add(new LatLng(-6.89191,107.67261)); kec_mandalajati.add(new LatLng(-6.89205,107.67281)); kec_mandalajati.add(new LatLng(-6.8921,107.67291)); kec_mandalajati.add(new LatLng(-6.89216,107.67311));
        kec_mandalajati.add(new LatLng(-6.89214,107.67319)); kec_mandalajati.add(new LatLng(-6.89204,107.67322)); kec_mandalajati.add(new LatLng(-6.89202,107.67329)); kec_mandalajati.add(new LatLng(-6.89194,107.67334)); kec_mandalajati.add(new LatLng(-6.89195,107.67346)); kec_mandalajati.add(new LatLng(-6.8919,107.67378)); kec_mandalajati.add(new LatLng(-6.89189,107.6739)); kec_mandalajati.add(new LatLng(-6.89194,107.674)); kec_mandalajati.add(new LatLng(-6.89204,107.67451)); kec_mandalajati.add(new LatLng(-6.89212,107.67459));
        kec_mandalajati.add(new LatLng(-6.89212,107.67479)); kec_mandalajati.add(new LatLng(-6.89205,107.675)); kec_mandalajati.add(new LatLng(-6.89209,107.67508)); kec_mandalajati.add(new LatLng(-6.89209,107.67527)); kec_mandalajati.add(new LatLng(-6.89213,107.67535)); kec_mandalajati.add(new LatLng(-6.89233,107.67612)); kec_mandalajati.add(new LatLng(-6.8924,107.67633)); kec_mandalajati.add(new LatLng(-6.89243,107.67646)); kec_mandalajati.add(new LatLng(-6.89241,107.67656)); kec_mandalajati.add(new LatLng(-6.89248,107.67679));
        kec_mandalajati.add(new LatLng(-6.89251,107.67715)); kec_mandalajati.add(new LatLng(-6.89253,107.67731)); kec_mandalajati.add(new LatLng(-6.89254,107.67741)); kec_mandalajati.add(new LatLng(-6.89269,107.67769)); kec_mandalajati.add(new LatLng(-6.89271,107.67776)); kec_mandalajati.add(new LatLng(-6.89269,107.67784)); kec_mandalajati.add(new LatLng(-6.8927,107.67802)); kec_mandalajati.add(new LatLng(-6.89182,107.67837)); kec_mandalajati.add(new LatLng(-6.89148,107.67843)); kec_mandalajati.add(new LatLng(-6.89134,107.67853));
        kec_mandalajati.add(new LatLng(-6.89124,107.67864)); kec_mandalajati.add(new LatLng(-6.89097,107.67922)); kec_mandalajati.add(new LatLng(-6.89085,107.6795)); kec_mandalajati.add(new LatLng(-6.89078,107.67957)); kec_mandalajati.add(new LatLng(-6.89045,107.67957)); kec_mandalajati.add(new LatLng(-6.89018,107.67957)); kec_mandalajati.add(new LatLng(-6.88988,107.67963)); kec_mandalajati.add(new LatLng(-6.88966,107.67973)); kec_mandalajati.add(new LatLng(-6.88966,107.67999)); kec_mandalajati.add(new LatLng(-6.88955,107.68017));
        kec_mandalajati.add(new LatLng(-6.88944,107.68043)); kec_mandalajati.add(new LatLng(-6.8894,107.68057)); kec_mandalajati.add(new LatLng(-6.8894,107.68068)); kec_mandalajati.add(new LatLng(-6.88942,107.68077)); kec_mandalajati.add(new LatLng(-6.88948,107.68088)); kec_mandalajati.add(new LatLng(-6.88958,107.68096)); kec_mandalajati.add(new LatLng(-6.88974,107.68104)); kec_mandalajati.add(new LatLng(-6.88999,107.68108)); kec_mandalajati.add(new LatLng(-6.89029,107.68125)); kec_mandalajati.add(new LatLng(-6.89047,107.68114));
        kec_mandalajati.add(new LatLng(-6.89062,107.68108)); kec_mandalajati.add(new LatLng(-6.89107,107.68069)); kec_mandalajati.add(new LatLng(-6.89117,107.68077)); kec_mandalajati.add(new LatLng(-6.89118,107.68085)); kec_mandalajati.add(new LatLng(-6.89138,107.68121)); kec_mandalajati.add(new LatLng(-6.89143,107.68147)); kec_mandalajati.add(new LatLng(-6.89155,107.68184)); kec_mandalajati.add(new LatLng(-6.89161,107.682)); kec_mandalajati.add(new LatLng(-6.89164,107.6824)); kec_mandalajati.add(new LatLng(-6.89166,107.6825));

        kec_mandalajati.add(new LatLng(-6.89178,107.68263)); kec_mandalajati.add(new LatLng(-6.89187,107.68268)); kec_mandalajati.add(new LatLng(-6.89197,107.68263)); kec_mandalajati.add(new LatLng(-6.89216,107.68253)); kec_mandalajati.add(new LatLng(-6.89233,107.6825)); kec_mandalajati.add(new LatLng(-6.89247,107.68256)); kec_mandalajati.add(new LatLng(-6.89252,107.68262)); kec_mandalajati.add(new LatLng(-6.89255,107.68293)); kec_mandalajati.add(new LatLng(-6.89252,107.68305)); kec_mandalajati.add(new LatLng(-6.89253,107.68318));
        kec_mandalajati.add(new LatLng(-6.89256,107.68331)); kec_mandalajati.add(new LatLng(-6.89263,107.68343)); kec_mandalajati.add(new LatLng(-6.89271,107.6835)); kec_mandalajati.add(new LatLng(-6.89275,107.68365)); kec_mandalajati.add(new LatLng(-6.89283,107.6838)); kec_mandalajati.add(new LatLng(-6.89287,107.68397)); kec_mandalajati.add(new LatLng(-6.89289,107.68415)); kec_mandalajati.add(new LatLng(-6.89301,107.68412)); kec_mandalajati.add(new LatLng(-6.89312,107.68408)); kec_mandalajati.add(new LatLng(-6.89328,107.68401));
        kec_mandalajati.add(new LatLng(-6.89336,107.68396)); kec_mandalajati.add(new LatLng(-6.89344,107.6839)); kec_mandalajati.add(new LatLng(-6.89351,107.6842)); kec_mandalajati.add(new LatLng(-6.8936,107.68428)); kec_mandalajati.add(new LatLng(-6.8937,107.68431)); kec_mandalajati.add(new LatLng(-6.89409,107.68445)); kec_mandalajati.add(new LatLng(-6.89425,107.68451)); kec_mandalajati.add(new LatLng(-6.89438,107.68458)); kec_mandalajati.add(new LatLng(-6.89448,107.68459)); kec_mandalajati.add(new LatLng(-6.8949,107.68461));
        kec_mandalajati.add(new LatLng(-6.89508,107.68461)); kec_mandalajati.add(new LatLng(-6.89543,107.6846)); kec_mandalajati.add(new LatLng(-6.89559,107.68461)); kec_mandalajati.add(new LatLng(-6.89587,107.68537)); kec_mandalajati.add(new LatLng(-6.89613,107.68595)); kec_mandalajati.add(new LatLng(-6.89626,107.68615)); kec_mandalajati.add(new LatLng(-6.89632,107.6862)); kec_mandalajati.add(new LatLng(-6.89639,107.68636)); kec_mandalajati.add(new LatLng(-6.8964,107.68644)); kec_mandalajati.add(new LatLng(-6.89651,107.68655));
        kec_mandalajati.add(new LatLng(-6.89654,107.68667)); kec_mandalajati.add(new LatLng(-6.89661,107.68672)); kec_mandalajati.add(new LatLng(-6.89661,107.68684)); kec_mandalajati.add(new LatLng(-6.89671,107.68685)); kec_mandalajati.add(new LatLng(-6.89679,107.68684)); kec_mandalajati.add(new LatLng(-6.89688,107.68684)); kec_mandalajati.add(new LatLng(-6.89694,107.68687)); kec_mandalajati.add(new LatLng(-6.89719,107.68697)); kec_mandalajati.add(new LatLng(-6.89732,107.687)); kec_mandalajati.add(new LatLng(-6.89741,107.68697));
        kec_mandalajati.add(new LatLng(-6.89747,107.68693)); kec_mandalajati.add(new LatLng(-6.89766,107.68676)); kec_mandalajati.add(new LatLng(-6.8979,107.68666)); kec_mandalajati.add(new LatLng(-6.89797,107.68667)); kec_mandalajati.add(new LatLng(-6.89825,107.68655)); kec_mandalajati.add(new LatLng(-6.89863,107.68658)); kec_mandalajati.add(new LatLng(-6.89874,107.68656)); kec_mandalajati.add(new LatLng(-6.89883,107.6865)); kec_mandalajati.add(new LatLng(-6.89898,107.68645)); kec_mandalajati.add(new LatLng(-6.89906,107.68646));
        kec_mandalajati.add(new LatLng(-6.89928,107.68651)); kec_mandalajati.add(new LatLng(-6.89939,107.68656)); kec_mandalajati.add(new LatLng(-6.8995,107.6865)); kec_mandalajati.add(new LatLng(-6.89964,107.68647)); kec_mandalajati.add(new LatLng(-6.89972,107.6864)); kec_mandalajati.add(new LatLng(-6.89984,107.68639)); kec_mandalajati.add(new LatLng(-6.89997,107.68633)); kec_mandalajati.add(new LatLng(-6.90019,107.6865)); kec_mandalajati.add(new LatLng(-6.90027,107.68648)); kec_mandalajati.add(new LatLng(-6.90051,107.68647));
        kec_mandalajati.add(new LatLng(-6.90058,107.68649)); kec_mandalajati.add(new LatLng(-6.90077,107.68649)); kec_mandalajati.add(new LatLng(-6.90097,107.68648)); kec_mandalajati.add(new LatLng(-6.90107,107.68649)); kec_mandalajati.add(new LatLng(-6.90115,107.68647)); kec_mandalajati.add(new LatLng(-6.90129,107.68648)); kec_mandalajati.add(new LatLng(-6.90129,107.68628)); kec_mandalajati.add(new LatLng(-6.90141,107.68629)); kec_mandalajati.add(new LatLng(-6.90155,107.68635)); kec_mandalajati.add(new LatLng(-6.90161,107.6864));
        kec_mandalajati.add(new LatLng(-6.90168,107.68646)); kec_mandalajati.add(new LatLng(-6.90183,107.68661)); kec_mandalajati.add(new LatLng(-6.9019,107.68668)); kec_mandalajati.add(new LatLng(-6.90203,107.68684)); kec_mandalajati.add(new LatLng(-6.9021,107.68689)); kec_mandalajati.add(new LatLng(-6.90217,107.68693)); kec_mandalajati.add(new LatLng(-6.90228,107.68704)); kec_mandalajati.add(new LatLng(-6.90238,107.68713)); kec_mandalajati.add(new LatLng(-6.90246,107.6872)); kec_mandalajati.add(new LatLng(-6.90273,107.68732));
        kec_mandalajati.add(new LatLng(-6.90285,107.68742)); kec_mandalajati.add(new LatLng(-6.90293,107.68743)); kec_mandalajati.add(new LatLng(-6.90308,107.68747)); kec_mandalajati.add(new LatLng(-6.90319,107.68751)); kec_mandalajati.add(new LatLng(-6.9033,107.68749)); kec_mandalajati.add(new LatLng(-6.90346,107.68737)); kec_mandalajati.add(new LatLng(-6.90362,107.68725)); kec_mandalajati.add(new LatLng(-6.90395,107.68729)); kec_mandalajati.add(new LatLng(-6.90413,107.68725)); kec_mandalajati.add(new LatLng(-6.90428,107.68713));

        kec_mandalajati.add(new LatLng(-6.90432,107.68706)); kec_mandalajati.add(new LatLng(-6.90441,107.68691)); kec_mandalajati.add(new LatLng(-6.90453,107.68682)); kec_mandalajati.add(new LatLng(-6.90467,107.68672)); kec_mandalajati.add(new LatLng(-6.90486,107.68663)); kec_mandalajati.add(new LatLng(-6.90496,107.6866)); kec_mandalajati.add(new LatLng(-6.90505,107.68656)); kec_mandalajati.add(new LatLng(-6.90532,107.68641)); kec_mandalajati.add(new LatLng(-6.9057,107.68634)); kec_mandalajati.add(new LatLng(-6.90582,107.68638));
        kec_mandalajati.add(new LatLng(-6.90591,107.68636)); kec_mandalajati.add(new LatLng(-6.90606,107.68626)); kec_mandalajati.add(new LatLng(-6.90625,107.68609)); kec_mandalajati.add(new LatLng(-6.90633,107.68597)); kec_mandalajati.add(new LatLng(-6.90715,107.68616)); kec_mandalajati.add(new LatLng(-6.90708,107.68578)); kec_mandalajati.add(new LatLng(-6.90697,107.68553)); kec_mandalajati.add(new LatLng(-6.90628,107.6839)); kec_mandalajati.add(new LatLng(-6.90587,107.68293)); kec_mandalajati.add(new LatLng(-6.90558,107.68233));
        kec_mandalajati.add(new LatLng(-6.9055,107.68216)); kec_mandalajati.add(new LatLng(-6.90544,107.68203)); kec_mandalajati.add(new LatLng(-6.90524,107.68154)); kec_mandalajati.add(new LatLng(-6.90507,107.68043)); kec_mandalajati.add(new LatLng(-6.90498,107.67969)); kec_mandalajati.add(new LatLng(-6.90477,107.67787)); kec_mandalajati.add(new LatLng(-6.90473,107.67679)); kec_mandalajati.add(new LatLng(-6.90468,107.67609)); kec_mandalajati.add(new LatLng(-6.90464,107.67579)); kec_mandalajati.add(new LatLng(-6.9046,107.67514));
        kec_mandalajati.add(new LatLng(-6.90454,107.67483)); kec_mandalajati.add(new LatLng(-6.90454,107.67463)); kec_mandalajati.add(new LatLng(-6.90457,107.67431)); kec_mandalajati.add(new LatLng(-6.90482,107.67331)); kec_mandalajati.add(new LatLng(-6.90488,107.67302)); kec_mandalajati.add(new LatLng(-6.90487,107.67287)); kec_mandalajati.add(new LatLng(-6.9048,107.67268)); kec_mandalajati.add(new LatLng(-6.90466,107.6724)); kec_mandalajati.add(new LatLng(-6.90441,107.6719)); kec_mandalajati.add(new LatLng(-6.90426,107.67155));
        kec_mandalajati.add(new LatLng(-6.90422,107.67111)); kec_mandalajati.add(new LatLng(-6.9042,107.66779)); kec_mandalajati.add(new LatLng(-6.90419,107.66716));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Pelaporan");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pelaporan, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setElevation(0);
        prefssatu = this.getActivity().getSharedPreferences(
                Login.SATU,
                MODE_PRIVATE +
                        MODE_PRIVATE | MODE_PRIVATE);
        ambilusername=prefssatu.getString(
                Login.KEY_SATU, "NA");

        pd = new ProgressDialog(getActivity());

        jenis = (EditText) view.findViewById(R.id.tahun);
        jenis.setVisibility(View.GONE);

        mainLayout = (LinearLayout) view.findViewById(R.id.linear);
        tahunspinner = (Spinner) view.findViewById(R.id.spinnertahun);

        tahunlist = new ArrayList<Category>();
        tahunspinner.setOnItemSelectedListener(this);

        imgAnim = (ImageView) view.findViewById(R.id.imgAnim);

        Ambiljenis();

        detailkejadian=(EditText)view.findViewById(R.id.detailkejadian);

        image1=(ImageView) view.findViewById(R.id.gambar1);
        image2=(ImageView) view.findViewById(R.id.gambar2);
        namaimage1=(TextView) view.findViewById(R.id.labelfoto1);
        namaimage2=(TextView) view.findViewById(R.id.labelfoto2);

        pilihfoto1=(Button) view.findViewById(R.id.ambilfoto1);
        pilihfoto2=(Button)view.findViewById(R.id.ambilfoto2);
        simpan=(Button)view.findViewById(R.id.btnsimpan);
        simpan.setEnabled(true);

        pilihfoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showfilechoser1();
            }
        });

        pilihfoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showfilechoser2();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Simpan();
            }
        });
        dialogShow = false;

        SharedPreferences prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firststart", true);
        if (firstStart) {
            Showcase();
        }
        blinkinAnim1();

        return view;
    }

    public void blinkinAnim1() {
        Animation animation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.blinking_anim);
        imgAnim.startAnimation(animation);
    }

    private void Showcase() {
        builder = new GuideView.Builder(getActivity())
                .setTitle("Jenis Pelaporan")
                .setContentText("Pilihlah jenis sesuai dengan\n kategori yang akan anda laporkan")
                .setGravity(Gravity.center)
                .setDismissType(DismissType.outside)
                .setTargetView(tahunspinner)
                .setGuideListener(new GuideListener() {
                    @Override
                    public void onDismiss(View view) {
                        switch (view.getId()) {
                            case R.id.spinnertahun:
                                builder.setTargetView(pilihfoto1).build();
                                builder.setTitle("Ambil Foto Pertama");
                                builder.setContentText("Gunakan untuk mengambil gambar pertama");
                                break;
                            case R.id.ambilfoto1:
                                builder.setTargetView(pilihfoto2).build();
                                builder.setTitle("Ambil Foto Kedua");
                                builder.setContentText("Gunakan untuk mengambil gambar kedua");
                                break;
                            case R.id.ambilfoto2:
                                return;
                        }
                        mGuideView = builder.build();
                        mGuideView.show();
                    }
                });
        mGuideView = builder.build();
        mGuideView.show();

        SharedPreferences prefs = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firststart", false);
        editor.apply();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Pelaporan", "onResume");
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @Override
    public void onPause() {
        super.onPause();

        Log.i("Pelaporan", "onPause");
        locationManager.removeUpdates(this);
    }

    private void showfilechoser1() {
//        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galeri",
//                "Batal"};

        final CharSequence[] items = {"Ambil Foto",
                "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tambah Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ambil Foto")) {
                    Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "pic_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    startActivityForResult(galleryIntent, REQUEST_TAKE_PHOTO);
//                } else if (items[item].equals("Pilih dari Galeri")) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
//        Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Movie_pict" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    private void showfilechoser2() {
//        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galeri",
//                "Batal"};

        final CharSequence[] items = {"Ambil Foto",
                "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tambah Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ambil Foto")) {
                    Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "pic_rumah" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    startActivityForResult(galleryIntent, REQUEST_TAKE_PHOTO1);
//                } else if (items[item].equals("Pilih dari Galeri")) {
//                    Intent intent = new Intent();
//                    intent.setType("image/*");
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST1);
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
//        Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Movie_pict" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            String path = mUri.getPath();
            String filepath = path;
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            image1.setImageBitmap(bitmap);
            namaimage1.setText(filename);

        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri picturePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picturePath);
            } catch (IOException e) {
            } finally {
                File f = new File(String.valueOf(picturePath));
                String filename = f.getName();
                String newfilename = filename.replace("%", "");
                namaimage1.setText(newfilename + ".jpg");
                image1.setImageBitmap(bitmap);
            }

        } else if (requestCode == REQUEST_TAKE_PHOTO1 && resultCode == Activity.RESULT_OK) {
            String path = mUri.getPath();
            String filepath = path;
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            Bundle extras = data.getExtras();
            bitmap1 = (Bitmap) extras.get("data");
            image2.setImageBitmap(bitmap1);
            namaimage2.setText(filename);

        } else if (requestCode == PICK_IMAGE_REQUEST1 && resultCode == Activity.RESULT_OK) {
            Uri picturePath = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picturePath);
            } catch (IOException e) {
            } finally {
                File f = new File(String.valueOf(picturePath));
                String filename = f.getName();
                String newfilename = filename.replace("%", "");
                namaimage2.setText(newfilename + ".jpg");
                image2.setImageBitmap(bitmap1);
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void tahunSpinner() {
        final List<String> tahun = new ArrayList<String>();
        tahun.add("Pilih Jenis Pelaporan");
        for (int i = 0; i < tahunlist.size(); i++) {
            tahun.add(tahunlist.get(i).getName());
        }
        ArrayAdapter<String> spinnerAdaptertahun = new ArrayAdapter<String>(
                getActivity(), R.layout.spinneritem, tahun);
        spinnerAdaptertahun.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        tahunspinner.setAdapter(spinnerAdaptertahun);
        tahunspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg3, View arg0,
                                       int position, long arg1) {
                String thn = (String) (tahunspinner.getSelectedItem());
                jenis.setText(thn);
                kode1 = jenis.getText().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void Ambiljenis() {
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST,
                Server.URL+"android/jenis.php", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Category cat = new Category(data.getInt("id"),
                                        data.getString("jenis"));
                                tahunlist.add(cat);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        tahunSpinner();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Server down, kembalilah lagi nanti...", Toast.LENGTH_LONG).show();
                    }
                });
        AppController.getInstance().addToRequestQueue(reqData);
    }

    void  Simpan(){
        if (!in_mandalajati) {
//            Toast.makeText(getActivity(), "Lengkapi Data dan Pastikan Anda Berada di Wilayah Mandalajati", Toast.LENGTH_LONG).show();
            diluarJangkauan();
            return;
        }

        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Kirim Kejadian...", " Mohon Tunggu...",
                false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL+"android/insert_kejadian.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {

                                sukses();

                                Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getActivity(), jObj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();

                        //menampilkan toast
//                        Toast.makeText(getActivity(), "Maaf Ada Kesalahan!!", Toast.LENGTH_LONG).show();
//                        Toast.makeText(getActivity(), "Pastikan Isi Data Dengan Lengkap", Toast.LENGTH_LONG).show();
                        gagalKirim();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", ambilusername);
                params.put("jenis", kode1);
                params.put("label1", namaimage1.getText().toString());
                params.put("label2", namaimage2.getText().toString());
                params.put("detailkejadian", detailkejadian.getText().toString());
                params.put("image1", getStringImage(bitmap));
                params.put("image2", getStringImage(bitmap1));
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest, "json");
    }

    void sukses(){
        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Info")
//                .setCancelText("Cancel")
                .setContentText("Terima kasih atas laporan anda...!!!")
                .setConfirmText("Oke")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), Menu.class));
                    }
                })
                .show();
    }

    private void diluarJangkauan() {
        SweetAlertDialog pDialogg = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        pDialogg.setTitleText("Diluar Batasa Area");
        pDialogg.setContentText("Pastikan anda berada di area wilayah Mandalajati");
        pDialogg.setConfirmText("Ulangi ?");
        pDialogg.show();

        pDialogg.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackgroundColor(getResources().getColor(R.color.blueLight));
    }

    private void gagalKirim() {
        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE);
        pDialog.setTitleText("Terjadi Kesalahan");
        pDialog.setContentText("Lengkapi data terlebih dulu");
        pDialog.show();

        pDialog.getButton(SweetAlertDialog.BUTTON_CONFIRM).setBackgroundColor(getResources().getColor(R.color.red_btn_bg_color));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("Pelaporan", "Provider " + provider + " has now status: " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Pelaporan", "Provider " + provider + " is enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Pelaporan", "Provider " + provider + " is disabled");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Pelaporan", "onLocationChanged [" + location + "]");

        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        in_mandalajati = PolyUtil.containsLocation(loc, kec_mandalajati, true);
        Log.d("Home", String.valueOf(in_mandalajati));

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
        }
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

package ru.nsrxyz.somegis;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener, OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener {
    GoogleMap mMap;
    Marker marker;
    Marker lmarker;
    Marker pmarker;
    boolean markerClicked;
    boolean pmarkerClicked;
    boolean lmarkerClicked;
    PolylineOptions rectOptions;
    Polyline polyline;
    PolygonOptions polygonOptions;
    Polygon polygon;
    Circle circle;
    PolylineOptions rectOptions0;
    Polyline polyline0;
    PolygonOptions polygonOptions0;
    Polygon polygon0;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    BitmapDescriptor bd;
    Integer scripture;
    private static final String TAG = "123";
    private static final String TAG2 = "avvv";
    private static final String TAG3 = "ccccc";
    private static final String PATH = Environment.getExternalStorageDirectory() + "/file.xml";
    ArrayList<String> mPointsLat = new ArrayList<>();
    ArrayList<String> mPointsLon = new ArrayList<>();
    ArrayList<String> mLinesLat = new ArrayList<>();
    ArrayList<String> mLinesLon = new ArrayList<>();
    ArrayList<String> mPolygonLat = new ArrayList<>();
    ArrayList<String> mPolygonLon = new ArrayList<>();
    ArrayList<LatLng> mPoints = new ArrayList<>();
    ArrayList<LatLng> mLinePoints = new ArrayList<>();
    ArrayList<LatLng> mPolygonPoints = new ArrayList<>();
    String name;
    ArrayList<String> getPoints = new ArrayList<>();
    ArrayList<String> getLinePoints = new ArrayList<>();
    ArrayList<String> getPolygonPoints = new ArrayList<>();
    ArrayList<String> getPointStyle = new ArrayList<>();
    ArrayList<String> getLineStyle = new ArrayList<>();
    ArrayList<String> getPolygonStyle = new ArrayList<>();
    ArrayList<String> getPointColor = new ArrayList<>();
    ArrayList<String> getLineColor = new ArrayList<>();
    ArrayList<String> getPolygonColor = new ArrayList<>();
    ArrayList<Integer> PtColor = new ArrayList<>();
    ArrayList<Integer> LColor = new ArrayList<>();
    ArrayList<Integer> PlColor = new ArrayList<>();
    ArrayList<String> gPoints = new ArrayList<>();
    ArrayList<String> gLinePoints = new ArrayList<>();
    ArrayList<String> gPolygonPoints = new ArrayList<>();
    String nameforobj;
    String ctype;
    String otype;
    ArrayList<String> aTypes = new ArrayList<>();
    ArrayList<Integer> aPos = new ArrayList<>();
    ArrayList<String> aStyleNames = new ArrayList<>();
    ArrayList<Integer> getTypo = new ArrayList<>();
    ArrayList<Integer> mColor = new ArrayList<>();
    String mp;
    String lp;
    String pp;
    ArrayList<Integer> Gap = new ArrayList<>();
    ArrayList<Integer> MGap = new ArrayList<>();
    ArrayList<Integer> LGap = new ArrayList<>();
    ArrayList<Integer> PGap = new ArrayList<>();
    private String up;
    private String del;
    private String upname;
    private String upcolor;
    private int current;
    ArrayList<Marker> mMarker = new ArrayList<>();
    private String a;
    private String b;
    private int current1;
    private String cname;
    private int ccolor;
    Integer v;
    Integer on;
    LatLng CDM;
    String CASM1;
    String CASM2;
    ArrayList<LatLng> PlgnVrtx = new ArrayList<>();
    ArrayList<LatLng> LineVrtx = new ArrayList<>();
    ArrayList<Polygon> PlgnList = new ArrayList<>();
    ArrayList<Polyline> LineList = new ArrayList<>();
    ArrayList<Circle> PtList = new ArrayList<>();
    private int typo;
    LatLng frst;
    LatLng lst;
    Integer frstto;
    Integer lstto;
    private LatLng clckd;
    ArrayList<LatLng> PtVrtx = new ArrayList<>();
    boolean mrkrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mDatabaseHelper = new DatabaseHelper(this, "SomeGIS8.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        Log.d(TAG, "S3");
        Cursor cursor = mSqLiteDatabase.query("Styles", new String[]{DatabaseHelper.NAME, DatabaseHelper._ID, DatabaseHelper.TYPE},
                null, null,
                null, null, null);
        cursor.moveToLast();
        Integer cnt = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
        for (int i = 0; i < cnt; i++) {
            cursor.moveToPosition(i);
            Log.d(TAG, "S4");
            String stylename = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
            aTypes.add(stylename);
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, aTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Layers");
        spinner.setSelection(0);
        Log.d(TAG, "S1");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d(TAG, "S2");
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                aPos.add(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap = mapFragment.getMap();
        if (mMap == null) {
            finish();
            return;
        }
        init();
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMarkerClickListener(this);
        on = 0;
        mrkrs = true;
    }

    private void init() {
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void onClickAdd(View view) {
        Intent intentb = new Intent(MapsActivity.this, AddActivity.class);
        startActivityForResult(intentb, 2);
    }

    public void onClickProperties(View view) {
        Intent intentr = new Intent(MapsActivity.this, PropsActivity.class);
        Cursor cursor = mSqLiteDatabase.query("Styles", new String[]{DatabaseHelper.NAME, DatabaseHelper._ID, DatabaseHelper.COLOR},
                null, null,
                null, null, null);
        cursor.moveToPosition(aPos.get(aPos.size() - 1));
        current1 = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
        cname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
        ccolor = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLOR));
        intentr.putExtra("CurrentName", String.valueOf(cname));
        intentr.putExtra("CurrentColor", String.valueOf(ccolor));
        cursor.close();
        startActivityForResult(intentr, 3);
    }

    public void onClickDelete(View view) {
        on = 0;
        Log.d(TAG, String.valueOf(on));
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                nameforobj = data.getStringExtra("Name");
                ctype = data.getStringExtra("ColorType");
                otype = data.getStringExtra("OType");
                aTypes.add(nameforobj);
                ContentValues newValues = new ContentValues();
                newValues.put("name", nameforobj);
                newValues.put("type", otype);
                newValues.put("color", ctype);
                mSqLiteDatabase.insert("Styles", null, newValues);
            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                up = data.getStringExtra("Update");
                upname = data.getStringExtra("UpName");
                upcolor = data.getStringExtra("UpColor");
                if (Integer.valueOf(up) == 1) {
                    for (int i = 0; i < aStyleNames.size(); i++) {
                        if (cname.equals(aStyleNames.get(i))) {
                            switch (getTypo.get(i)) {
                                case 0:
                                    aStyleNames.set(i, upname);
                                    mColor.set(i, Integer.valueOf(upcolor));
                                    Log.d(TAG, String.valueOf(mColor.get(i)));
                                    break;
                                case 1:
                                    aStyleNames.set(i, upname);
                                    mColor.set(i, Integer.valueOf(upcolor));
                                    Log.d(TAG, String.valueOf(mColor.get(i)));
                                    break;
                                case 2:
                                    aStyleNames.set(i, upname);
                                    mColor.set(i, Integer.valueOf(upcolor));
                                    Log.d(TAG, String.valueOf(mColor.get(i)));
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void onClickStopEdit(View view) {
        if (mrkrs) {
            for (int i = 0; i < mPoints.size(); i++) {
                mMarker.get(i).setVisible(false);
            }
            mrkrs = false;
        }
        else {
            for (int i = 0; i < mPoints.size(); i++) {
                mMarker.get(i).setVisible(true);
            }
            mrkrs = true;
        }
    }

    @Override
    public void onMapClick(LatLng point) {
        switch (aPos.get(aPos.size() - 1)) {
            default:
                Cursor cursor = mSqLiteDatabase.query("Styles", new String[]{DatabaseHelper.NAME,
                                DatabaseHelper.TYPE, DatabaseHelper.COLOR},
                        null, null,
                        null, null, null);

                cursor.moveToPosition(aPos.get(aPos.size() - 1));
                Integer color = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLOR));
                Integer type = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.TYPE));
                String stname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME));
                Log.d(TAG, "D3");
                switch (type) {
                    case 0:
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .visible(true)
                                .draggable(true));
                        mMarker.add(marker);
                        CircleOptions circleOptions = new CircleOptions()
                                .center(point)
                                .radius(10000)
                                .fillColor(color)
                                .strokeColor(color)
                                .strokeWidth(1);
                        circle = mMap.addCircle(circleOptions);
                        mMap.addCircle(circleOptions);
                        mPointsLat.add(String.valueOf(point.latitude));
                        mPointsLon.add(String.valueOf(point.longitude));
                        mPoints.add(point);
                        aStyleNames.add(stname);
                        mColor.add(color);
                        PtColor.add(color);
                        getTypo.add(0);
                        Gap.add(2);
                        PtList.add(circle);
                        PtVrtx.add(marker.getPosition());
                        break;
                    case 1:
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .draggable(true));
                        mMarker.add(marker);
                        mPointsLat.add(String.valueOf(point.latitude));
                        mPointsLon.add(String.valueOf(point.longitude));
                        mPoints.add(point);
                        aStyleNames.add(stname);
                        mColor.add(color);
                        LColor.add(color);
                        getTypo.add(1);
                        String cond;
                        if ((aStyleNames.size() - 2) < 0) {
                            cond = null;
                        } else {
                            cond = (aStyleNames.get(aStyleNames.size() - 2));
                        }
                        if (aStyleNames.get(aStyleNames.size() - 1).equals(cond)) {
                            switch (on) {
                                case 1:
                                    rectOptions.add(marker.getPosition());
                                    rectOptions.color(color);
                                    polyline = mMap.addPolyline(rectOptions);
                                    LineVrtx.add(marker.getPosition());
                                    LineList.add(polyline);
                                    Gap.add(1);
                                    LGap.add(1);
                                    Log.d(TAG, "a");
                                    break;
                                case 0:
                                    rectOptions = new PolylineOptions().add(marker.getPosition());
                                    LineVrtx.add(marker.getPosition());
                                    Gap.add(0);
                                    LGap.add(0);
                                    on = 1;
                                    Log.d(TAG, "b");
                                    break;
                            }
                        } else {
                            rectOptions = new PolylineOptions().add(marker.getPosition());
                            LineVrtx.add(marker.getPosition());
                            Gap.add(0);
                            LGap.add(0);
                            on = 1;
                            Log.d(TAG, "b");
                        }
                        break;
                    case 2:
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(point)
                                .draggable(true));
                        mMarker.add(marker);
                        mPointsLat.add(String.valueOf(point.latitude));
                        mPointsLon.add(String.valueOf(point.longitude));
                        mPoints.add(point);
                        aStyleNames.add(stname);
                        mColor.add(color);
                        PlColor.add(color);
                        getTypo.add(2);
                        String cond2;
                        if ((aStyleNames.size() - 2) < 0) {
                            cond2 = null;
                        } else {
                            cond2 = (aStyleNames.get(aStyleNames.size() - 2));
                        }
                        if (aStyleNames.get(aStyleNames.size() - 1).equals(cond2)) {
                            switch (on) {
                                case 1:
                                    polygonOptions.add(marker.getPosition());
                                    polygonOptions.strokeColor(color);
                                    polygonOptions.fillColor(color);
                                    polygon = mMap.addPolygon(polygonOptions);
                                    PlgnVrtx.add(marker.getPosition());
                                    PlgnList.add(polygon);
                                    Log.d(TAG, "1");
                                    Gap.add(1);
                                    PGap.add(1);
                                    break;
                                case 0:
                                    polygonOptions = new PolygonOptions().add(marker.getPosition());
                                    PlgnVrtx.add(marker.getPosition());
                                    Log.d(TAG, "2");
                                    Gap.add(0);
                                    PGap.add(0);
                                    on = 1;
                                    Log.d(TAG, "on");
                                    break;
                            }
                        } else {
                            polygonOptions = new PolygonOptions().add(marker.getPosition());
                            PlgnVrtx.add(marker.getPosition());
                            Log.d(TAG, "2");
                            Gap.add(0);
                            PGap.add(0);
                            on = 1;
                            Log.d(TAG, "on");
                        }
                        break;
                }
                cursor.close();
        }
    }

    public boolean onMarkerClick(Marker marker) {
        if (markerClicked) {
            Log.d(TAG, String.valueOf(marker.getPosition()));
            clckd = marker.getPosition();
        } else {
            Log.d(TAG, String.valueOf(marker.getPosition()));
            clckd = marker.getPosition();
            markerClicked = true;
        }
        return false;
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d(TAG3, String.valueOf(LineVrtx.size()));
        Log.d(TAG3, String.valueOf(mPointsLat.size()));
        Log.d(TAG, String.valueOf(clckd));
        for (int i = 0; i < mPointsLat.size() - 1; i++) {
            Log.d(TAG, "0");
            Log.d(TAG, String.valueOf((new LatLng(Double.valueOf(mPointsLat.get(i)), Double.valueOf(mPointsLon.get(i))))));
            if (clckd.equals(new LatLng(Double.valueOf(mPointsLat.get(i)), Double.valueOf(mPointsLon.get(i))))) {
                Log.d(TAG, "1");
                int a = getTypo.get(i);
                switch (a) {
                    case 0:
                        CDM = clckd;
                        typo = 0;
                        Log.d(TAG, "0");
                        break;
                    case 1:
                        CDM = clckd;
                        if (mPolygonLat.size() - 1 == 1) {
                            CASM1 = String.valueOf(0);
                        } else {
                            CASM1 = String.valueOf(new LatLng(Double.valueOf(mPointsLat.get(i - 1)), Double.valueOf(mPointsLon.get(i - 1))));
                        }
                        if (mPolygonLat.size() - 1 == i + 1) {
                            CASM2 = String.valueOf(0);
                        } else {
                            CASM2 = String.valueOf(new LatLng(Double.valueOf(mPointsLat.get(i + 1)), Double.valueOf(mPointsLon.get(i + 1))));
                        }
                        typo = 1;
                        Log.d(TAG, "1");
                        break;
                    case 2:
                        CDM = clckd;
                        if (mPolygonLat.size() - 1 == 1) {
                            CASM1 = String.valueOf(0);
                        } else {
                            CASM1 = String.valueOf(new LatLng(Double.valueOf(mPointsLat.get(i - 1)), Double.valueOf(mPointsLon.get(i - 1))));
                        }
                        if (mPolygonLat.size() - 1 == i + 1) {
                            CASM2 = String.valueOf(0);
                        } else {
                            CASM2 = String.valueOf(new LatLng(Double.valueOf(mPointsLat.get(i + 1)), Double.valueOf(mPointsLon.get(i + 1))));
                        }
                        typo = 2;
                        Log.d(TAG, "2");
                        break;
                }
                break;
            }
        }
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, String.valueOf(marker.getPosition()));
        switch (typo) {
            case 0:
                for (int i = 0; i < PtVrtx.size() - 1; i++) {
                    Log.d(TAG, String.valueOf(i));
                    if (String.valueOf(CDM).equals(String.valueOf(PtVrtx.get(i)))) {
                        frst = LineVrtx.get(i);
                        frstto = i;
                        PtList.get(i).remove();
                        PtVrtx.set(i, marker.getPosition());
                        CircleOptions circleOptions = new CircleOptions()
                                .center(PtVrtx.get(i))
                                .radius(10000)
                                .fillColor(PtColor.get(i))
                                .strokeColor(PtColor.get(i))
                                .strokeWidth(1);
                        circle = mMap.addCircle(circleOptions);
                        mMap.addCircle(circleOptions);
                        getTypo.add(0);
                        Gap.add(2);
                        PtList.add(circle);
                        break;
                    }
                    break;
                }
                break;
            case 1:
                Log.d(TAG, "1");
                for (int i = 0; i < LineVrtx.size() - 1; i++) {
                    Log.d(TAG, String.valueOf(i));
                    if (String.valueOf(CDM).equals(String.valueOf(LineVrtx.get(i)))) {
                        for (int v = i; v >= 0; v--) {
                            Log.d(TAG, String.valueOf(v));
                            if (LGap.get(v) == 0) {
                                frst = LineVrtx.get(v);
                                frstto = v;
                                break;
                            }
                        }
                        Log.d(TAG, "2");
                        for (int v = i; v <= LineVrtx.size() - 1; v++) {
                            Log.d(TAG, String.valueOf(v));
                            if (LGap.get(v) == 0) {
                                lst = LineVrtx.get(v - 1);
                                lstto = v;
                            } else {
                                lst = LineVrtx.get(LineVrtx.size() - 1);
                                lstto = LineVrtx.size() - 1;
                            }
                        }
                        Log.d(TAG, "3");
                        for (int w = frstto; w < lstto; w++) {
                            Log.d(TAG, String.valueOf(w));
                            LineList.get(w).remove();
                        }
                        LineVrtx.set(i, marker.getPosition());
                        rectOptions = new PolylineOptions().add(frst);
                        Gap.add(0);
                        LGap.add(0);
                        on = 1;
                        for (int w = frstto + 1; w <= lstto; w++) {
                            rectOptions.add(LineVrtx.get(w));
                            rectOptions.color(LColor.get(w));
                            polyline = mMap.addPolyline(rectOptions);
                            LineList.add(polyline);
                            Gap.add(1);
                            LGap.add(1);
                            Log.d(TAG, "a");
                        }
                    }
                    if (String.valueOf(CDM).equals(String.valueOf(0))) {
                        if (CDM == LineVrtx.get(i)) {
                            for (int v = i; v > 0; v++) {
                                if (LGap.get(v) == 0) {
                                    frst = LineVrtx.get(v);
                                    frstto = v;
                                    break;
                                }
                            }
                            for (int v = i; v < LineVrtx.size() - 1; v++) {
                                if (LGap.get(v) == 0) {
                                    lst = LineVrtx.get(v - 1);
                                    lstto = v;
                                    break;
                                } else {
                                    lst = LineVrtx.get(LineVrtx.size() - 1);
                                    lstto = LineVrtx.size() - 1;
                                }
                            }
                            for (int w = frstto; w <= lstto; w++) {
                                LineList.get(w).remove();
                            }
                            LineVrtx.set(i + 1, marker.getPosition());
                            rectOptions = new PolylineOptions().add(frst);
                            Gap.add(0);
                            LGap.add(0);
                            on = 1;
                            for (int w = frstto + 1; w <= lstto; w++) {
                                rectOptions.add(LineVrtx.get(w));
                                rectOptions.color(LColor.get(w));
                                polyline = mMap.addPolyline(rectOptions);
                                LineList.add(polyline);
                                Gap.add(1);
                                LGap.add(1);
                                Log.d(TAG, "a");
                            }
                        }
                    }
                }
                break;
            case 2:
                Log.d(TAG, "1");
                for (int i = 0; i < PlgnVrtx.size() - 1; i++) {
                    Log.d(TAG, String.valueOf(i));
                    if (String.valueOf(CDM).equals(String.valueOf(PlgnVrtx.get(i)))) {
                        for (int v = i; v >= 0; v--) {
                            Log.d(TAG, String.valueOf(v));
                            if (PGap.get(v) == 0) {
                                frst = PlgnVrtx.get(v);
                                frstto = v;
                                break;
                            }
                        }
                        Log.d(TAG, "2");
                        for (int v = i; v <= PlgnVrtx.size() - 1; v++) {
                            Log.d(TAG, String.valueOf(v));
                            if (LGap.get(v) == 0) {
                                lst = PlgnVrtx.get(v - 1);
                                lstto = v;
                            } else {
                                lst = PlgnVrtx.get(PlgnVrtx.size() - 1);
                                lstto = PlgnVrtx.size() - 1;
                            }
                        }
                        Log.d(TAG, "3");
                        for (int w = frstto; w < lstto; w++) {
                            Log.d(TAG, String.valueOf(w));
                            PlgnList.get(w).remove();
                        }
                        PlgnVrtx.set(i, marker.getPosition());
                        polygonOptions = new PolygonOptions().add(frst);
                        Gap.add(0);
                        PGap.add(0);
                        on = 1;
                        for (int w = frstto + 1; w <= lstto; w++) {
                            polygonOptions.add(PlgnVrtx.get(w));
                            polygonOptions.strokeColor(PlColor.get(w));
                            polygonOptions.fillColor(PlColor.get(w));
                            polygon = mMap.addPolygon(polygonOptions);
                            PlgnList.add(polygon);
                            Gap.add(1);
                            PGap.add(1);
                            Log.d(TAG, "a");
                        }
                        if (String.valueOf(CDM).equals(String.valueOf(0))) {
                            if (CDM == PlgnVrtx.get(i)) {
                                for (int v = i; v > 0; v++) {
                                    if (PGap.get(v) == 0) {
                                        frst = PlgnVrtx.get(v);
                                        frstto = v;
                                        break;
                                    }
                                }
                                for (int v = i; v < PlgnVrtx.size() - 1; v++) {
                                    if (LGap.get(v) == 0) {
                                        lst = PlgnVrtx.get(v - 1);
                                        lstto = v;
                                        break;
                                    } else {
                                        lst = PlgnVrtx.get(PlgnVrtx.size() - 1);
                                        lstto = PlgnVrtx.size() - 1;
                                    }
                                }
                                for (int w = frstto; w <= lstto; w++) {
                                    PlgnList.get(w).remove();
                                }
                                PlgnVrtx.set(i, marker.getPosition());
                                polygonOptions = new PolygonOptions().add(frst);
                                Gap.add(0);
                                PGap.add(0);
                                on = 1;
                                for (int w = frstto + 1; w <= lstto; w++) {
                                    polygonOptions.add(PlgnVrtx.get(w));
                                    polygonOptions.strokeColor(PlColor.get(w));
                                    polygonOptions.fillColor(PlColor.get(w));
                                    polygon = mMap.addPolygon(polygonOptions);
                                    PlgnList.add(polygon);
                                    Gap.add(1);
                                    PGap.add(1);
                                    Log.d(TAG, "a");
                                }
                            }
                        }
                    }
                }
            break;
        }
    }


    public void onClickSave(View view) {
        saveXML("4");
    }

    private void saveXML(String indents) {
        try {
            File file = new File(getExternalFilesDir(null), "file.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            Element rootElement = doc.createElement("kml");
            rootElement.setAttribute("xmlns", "http://www.opengis.net/kml/2.2");
            doc.appendChild(rootElement);
            Element documentElement = doc.createElement("Document");
            rootElement.appendChild(documentElement);

            Element placemarkElement = doc.createElement("Placemark");
            documentElement.appendChild(placemarkElement);

            Element lplacemarkElement = doc.createElement("Placemark");
            documentElement.appendChild(lplacemarkElement);

            Element pplacemarkElement = doc.createElement("Placemark");
            documentElement.appendChild(pplacemarkElement);
            Log.d(TAG, "File phase1!");


            for (int i = 0; i < aTypes.size(); i++) {
                Log.d(TAG, String.valueOf(i));
                for (int v = 0; v < aStyleNames.size(); v++) {
                    Log.d(TAG2, String.valueOf(v));
                    if (aTypes.get(i).equals(aStyleNames.get(v))) {
                        Log.i(TAG, "Check");
                        switch (getTypo.get(v)) {
                            case 0:
                                mp = mPointsLat.get(v) + "," + mPointsLon.get(v) + ",";
                                Integer color = mColor.get(v);
                                Element pointElement = doc.createElement("Point");
                                placemarkElement.appendChild(pointElement);
                                Element coordinatesElement = doc.createElement("Coordinates");
                                pointElement.appendChild(coordinatesElement);
                                coordinatesElement.appendChild(doc.createTextNode(mp));
                                String style = aStyleNames.get(v);
                                Element postyle = doc.createElement("Style");
                                postyle.appendChild(doc.createTextNode(style));
                                pointElement.appendChild(postyle);
                                Element pointcolor = doc.createElement("Color");
                                pointcolor.appendChild(doc.createTextNode(String.valueOf(color)));
                                pointElement.appendChild(pointcolor);
                                break;
                            case 1:
                                if (Gap.get(v) == 1) {
                                    lp = mPointsLat.get(v) + "," + mPointsLon.get(v) + ",";
                                    lplacemarkElement.getLastChild().getLastChild().appendChild(doc.createTextNode(lp));
                                    Log.i(TAG, "Check1");
                                } else {
                                    lp = mPointsLat.get(v) + "," + mPointsLon.get(v) + ",";
                                    Integer lcolor = mColor.get(v);
                                    Element linestringElement = doc.createElement("LineString");
                                    lplacemarkElement.appendChild(linestringElement);
                                    Element linecolor = doc.createElement("Color");
                                    linecolor.appendChild(doc.createTextNode(String.valueOf(lcolor)));
                                    linestringElement.appendChild(linecolor);
                                    String lstyle = aStyleNames.get(v);
                                    Element linestyle = doc.createElement("Style");
                                    linestyle.appendChild(doc.createTextNode(lstyle));
                                    linestringElement.appendChild(linestyle);
                                    Element lcoordinatesElement = doc.createElement("Coordinates");
                                    linestringElement.appendChild(lcoordinatesElement);
                                    lcoordinatesElement.appendChild(doc.createTextNode(lp));
                                    Log.i(TAG, "Check2");
                                }
                                break;
                            case 2:
                                if (Gap.get(v) == 1) {
                                    pp = mPointsLat.get(v) + "," + mPointsLon.get(v) + ",";
                                    pplacemarkElement.getLastChild().getLastChild().appendChild(doc.createTextNode(pp));
                                    Log.i(TAG, "Check3");
                                } else {
                                    pp = mPointsLat.get(v) + "," + mPointsLon.get(v) + ",";
                                    Integer pcolor = mColor.get(v);
                                    Element polygonElement = doc.createElement("Polygon");
                                    pplacemarkElement.appendChild(polygonElement);
                                    Element polygoncolor = doc.createElement("Color");
                                    polygoncolor.appendChild(doc.createTextNode(String.valueOf(pcolor)));
                                    polygonElement.appendChild(polygoncolor);
                                    String pstyle = aStyleNames.get(v);
                                    Element plgnstyle = doc.createElement("Style");
                                    plgnstyle.appendChild(doc.createTextNode(pstyle));
                                    polygonElement.appendChild(plgnstyle);
                                    Element pcoordinatesElement = doc.createElement("Coordinates");
                                    polygonElement.appendChild(pcoordinatesElement);
                                    pcoordinatesElement.appendChild(doc.createTextNode(pp));
                                    Log.i(TAG, "Check4");
                                }
                        }
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc.getDocumentElement());
            StreamResult result = new StreamResult(file);
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indents);
            transformer.transform(source, result);

            Log.d(TAG, "File saved!");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickLoad(View view) {
        File file = new File(getExternalFilesDir(null), "file.xml");
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            Log.d(TAG, "P2");
            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nodeList = doc.getElementsByTagName("Point");
            Log.d(TAG, "P3");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                getPoints.add(getValue(e, "Coordinates"));
                String mp = "";
                for (int v = 0; v < getPoints.size(); v++) {
                    mp = getPoints.get(v);
                }
                Pattern pattern = Pattern.compile(",");
                Matcher matcher = pattern.matcher(mp);
                int n = 0;
                while (matcher.find()) {
                    System.out.println(matcher.group());
                    n++;
                }
                for (int q = 0; q < n; q++) {
                    getPointStyle.add(getValue(e, "Style"));
                    getPointColor.add(getValue(e, "Color"));
                    getPointStyle.add(getValue(e, "Style"));
                    getPointColor.add(getValue(e, "Color"));
                    MGap.add(2);
                    MGap.add(2);
                }
                Log.d(TAG, String.valueOf(n));
                String[] items = mp.split(",");
                for (String item : items) {
                    gPoints.add(item);
                }
            }
            nodeList = doc.getElementsByTagName("LineString");
            Log.d(TAG, "P3");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                getLinePoints.add(getValue(e, "Coordinates"));
                LGap.add(0);
                LGap.add(0);
                String lp = "";
                for (int v = 0; v < getLinePoints.size(); v++) {
                    lp = getLinePoints.get(v);
                }
                String[] items = lp.split(",");
                for (String item : items) {
                    gLinePoints.add(item);
                }
                Pattern pattern = Pattern.compile(",");
                Matcher matcher = pattern.matcher(lp);
                int n = 0;
                while (matcher.find()) {
                    System.out.println(matcher.group());
                    n++;
                }
                for (int q = 0; q < n; q++) {
                    getLineStyle.add(getValue(e, "Style"));
                    getLineColor.add(getValue(e, "Color"));
                }
                for (int z = 2; z < n; z++) {
                    LGap.add(1);
                }
            }
            nodeList = doc.getElementsByTagName("Polygon");
            Log.d(TAG, "P3");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element e = (Element) nodeList.item(i);
                getPolygonPoints.add(getValue(e, "Coordinates"));
                PGap.add(0);
                PGap.add(0);
                String pp = "";
                for (int v = 0; v < getPolygonPoints.size(); v++) {
                    pp = getPolygonPoints.get(v);
                }
                String[] items = pp.split(",");
                for (String item : items) {
                    gPolygonPoints.add(item);
                }
                Pattern pattern = Pattern.compile(",");
                Matcher matcher = pattern.matcher(pp);
                int n = 0;
                while (matcher.find()) {
                    System.out.println(matcher.group());
                    n++;
                }
                for (int q = 0; q < n; q++) {
                    getPolygonStyle.add(getValue(e, "Style"));
                    getPolygonColor.add(getValue(e, "Color"));
                }
                for (int z = 2; z < n; z++) {
                    PGap.add(1);
                }
            }
            for (int i = 0; i < Gap.size(); i++) {
                Log.d(TAG, String.valueOf(Gap.get(i)));
            }
            for (int v = 0; v < gPoints.size(); v++) {
                if (gPoints.get(v).equals("")) {
                    gPoints.remove(v);
                }
                if (gPoints.get(v).equals(",")) {
                    gPoints.remove(v);
                }
            }

            for (int v = 0; v < gLinePoints.size(); v++) {
                if (gLinePoints.get(v).equals("")) {
                    gLinePoints.remove(v);
                }
                if (gLinePoints.get(v).equals(",")) {
                    gLinePoints.remove(v);
                }
            }

            for (int v = 0; v < gPolygonPoints.size(); v++) {
                if (gPolygonPoints.get(v).equals("")) {
                    gPolygonPoints.remove(v);
                }
                if (gPolygonPoints.get(v).equals(",")) {
                    gPolygonPoints.remove(v);
                }
            }

            for (int v = 0; v < getPointStyle.size(); v++) {
                Log.d(TAG, getPointStyle.get(v));
            }
            for (int v = 0; v < getLineStyle.size(); v++) {
                Log.d(TAG2, getLineStyle.get(v));
            }
            for (int v = 0; v < getPolygonStyle.size(); v++) {
                Log.d(TAG3, getPolygonStyle.get(v));
            }

            Log.d(TAG, "P5");
            for (int i = 0; i < gLinePoints.size(); i = i + 2) {
                String a = gLinePoints.get(i);
                String b = gLinePoints.get(i + 1);
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(a), Double.valueOf(b)))
                        .draggable(true));
                mPointsLat.add(String.valueOf(marker.getPosition().latitude));
                mPointsLon.add(String.valueOf(marker.getPosition().longitude));
                mLinePoints.add(new LatLng(Double.valueOf(a), Double.valueOf(b)));
                aStyleNames.add(getLineStyle.get(i));
                mColor.add(Integer.parseInt(getLineColor.get(i)));
                getTypo.add(1);
                Log.d(TAG, String.valueOf(mLinePoints));
                Gap.add(LGap.get(i));
                if (Gap.get(Gap.size() - 1) == 1) {
                    rectOptions.add(marker.getPosition());
                    rectOptions.color(Integer.parseInt(getLineColor.get(i)));
                    polyline = mMap.addPolyline(rectOptions);
                } else {
                    rectOptions = new PolylineOptions().add(marker.getPosition());
                }
            }

            Log.d(TAG, "P5");
            for (int i = 0; i < gPolygonPoints.size(); i = i + 2) {
                String a = gPolygonPoints.get(i);
                String b = gPolygonPoints.get(i + 1);
                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.valueOf(a), Double.valueOf(b)))
                        .draggable(true));
                mPointsLat.add(String.valueOf(marker.getPosition().latitude));
                mPointsLon.add(String.valueOf(marker.getPosition().latitude));
                mPolygonPoints.add(new LatLng(Double.valueOf(a), Double.valueOf(b)));
                aStyleNames.add(getPolygonStyle.get(i));
                Log.i(TAG, String.valueOf(mPolygonPoints));
                mColor.add(Integer.parseInt(getPolygonColor.get(i)));
                getTypo.add(2);
                Gap.add(PGap.get(i));
                if (Gap.get(Gap.size() - 1) == 1) {
                    polygonOptions.add(marker.getPosition());
                    polygonOptions.strokeColor(Integer.parseInt(getPolygonColor.get(i)));
                    polygonOptions.fillColor(Integer.parseInt(getPolygonColor.get(i)));
                    polygon = mMap.addPolygon(polygonOptions);
                    Log.d(TAG, "1");
                } else {
                    polygonOptions = new PolygonOptions().add(marker.getPosition());
                    Log.d(TAG, "2");
                }
            }

            for (int i = 0; i < gPoints.size(); i = i + 2) {
                String a = gPoints.get(i);
                String b = gPoints.get(i + 1);
                CircleOptions circleOptions = new CircleOptions()
                        .center(new LatLng(Double.valueOf(a), Double.valueOf(b)))
                        .radius(100000)
                        .fillColor(Integer.parseInt(getPointColor.get(i)))
                        .strokeColor(Integer.parseInt(getPointColor.get(i)))
                        .strokeWidth(1);
                mMap.addCircle(circleOptions);
                mPointsLat.add(String.valueOf((Double.valueOf(a))));
                mPointsLon.add(String.valueOf(Double.valueOf(b)));
                mPoints.add(new LatLng(Double.valueOf(a), Double.valueOf(b)));
                aStyleNames.add(getPointStyle.get(i));
                mColor.add(Integer.parseInt(getPointColor.get(i)));
                getTypo.add(0);
                Gap.add(MGap.get(i));
            }


        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public String getValue(Element item, String name) {
        NodeList nodes = item.getElementsByTagName(name);
        return this.getTextNodeValue(nodes.item(0));
    }

    private String getTextNodeValue(Node node) {
        Node child;
        if (node != null) {
            if (node.hasChildNodes()) {
                child = node.getFirstChild();
                while (child != null) {
                    if (child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                    child = child.getNextSibling();
                }
            }
        }
        return "";
    }
}



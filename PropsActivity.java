package ru.nsrxyz.somegis;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class PropsActivity extends Activity implements ColorPickerDialog.OnColorChangedListener {

    Button button4;
    ArrayList<Integer> aPaint = new ArrayList<>();
    private static final String TAG = "123";
    private EditText editText3;
    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;
    String PEdit;
    private String upname;
    private String upcolor;
    private String currentname;
    private String currentcolor;
    ArrayList<Integer> aType = new ArrayList<>();
    ArrayList<Integer> aColor = new ArrayList<>();
    ArrayList<String> aName = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_props);
        editText3 = (EditText) findViewById(R.id.editText3);
        currentname = getIntent().getStringExtra("CurrentName");
        currentcolor = getIntent().getStringExtra("CurrentColor");
        mDatabaseHelper = new DatabaseHelper(this, "SomeGIS8.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        editText3.setText(currentname);
    }

    public void onClickColorPick(View view) {
        ColorPickerDialog color = new ColorPickerDialog(PropsActivity.this, PropsActivity.this, "picker",Color.BLACK,Color.WHITE);
        Log.d(TAG, "Yis");
        color.show();
        Log.d(TAG, "Yis2");
    }

    @Override
    public void colorChanged(String key, int color) {
        aPaint.add(color);
        Log.d(TAG, "Yis3");
    }

    public void onClickUpdate(View view) {
        if (aPaint.size() == 0) {
            ContentValues updatedValues = new ContentValues();
            updatedValues.put("name", editText3.getText().toString());
            updatedValues.put("color", currentcolor);
            upcolor = currentcolor;
            mSqLiteDatabase.update("Styles", updatedValues, "NAME = ?", new String[]{currentname});
            Log.d(TAG, "Yis2");
        }
        else {
            ContentValues updatedValues = new ContentValues();
            updatedValues.put("name", editText3.getText().toString());
            updatedValues.put("color", String.valueOf(aPaint.get(aPaint.size() - 1)));
            upcolor = String.valueOf(aPaint.get(aPaint.size() - 1));
            mSqLiteDatabase.update("Styles", updatedValues, "NAME = ?", new String[]{currentname});
            Log.d(TAG, "Yis3");
        }
        PEdit = String.valueOf(1);
        upname = editText3.getText().toString();
        Intent intentb = new Intent();
        intentb.putExtra("Update", PEdit);
        intentb.putExtra("UpName", upname);
        intentb.putExtra("UpColor", upcolor);
        setResult(RESULT_OK, intentb);
        finish();
        Log.d(TAG, "Finish");
    }
}

package ru.nsrxyz.somegis;

import android.app.Activity;
import android.content.Intent;
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

public class AddActivity extends Activity implements ColorPickerDialog.OnColorChangedListener {

    String[] data = {"point", "line", "polygon"};
    Button button4;
    ArrayList<Integer> aPaint = new ArrayList<>();
    ArrayList<Integer> aType = new ArrayList<>();
    private static final String TAG = "123";
    private EditText editText2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        button4 = (Button) findViewById(R.id.button4);
        editText2 = (EditText) findViewById(R.id.editText2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Choose type");
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
                aType.add(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    public void onClickColorPick(View view) {
        ColorPickerDialog color = new ColorPickerDialog(AddActivity.this, AddActivity.this, "picker",Color.BLACK,Color.WHITE);
        Log.d(TAG, "Yis");
        color.show();
        Log.d(TAG, "Yis2");
    }

    @Override
    public void colorChanged(String key, int color) {
        aPaint.add(color);
        Log.d(TAG, "Yis3");
    }

    public void onClickBack(View view) {
        Intent intent = new Intent();
        intent.putExtra("Name", editText2.getText().toString());
        intent.putExtra("ColorType", String.valueOf(aPaint.get(aPaint.size() - 1)));
        intent.putExtra("OType", String.valueOf(aType.get(aType.size() - 1)));
        setResult(RESULT_OK, intent);
        finish();
        Log.d(TAG, "Finish");
    }
}

package com.iteso.eylisten;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

public class activity_equalizer extends AppCompatActivity {

    private int[] normal_data={0,57,0,22,0,71};
    private int[] classical_data={1,85,100,0,80,85};
    private int[] dance_data={2,85,0,66,80,42};
    private int[] flat_data={3,14,0,22,0,28};
    private int[] folk_data={4,57,0,22,0,40,14};
    private int[] metal_data={5,71,33,33,60,28};
    private int[] hip_data={6,85,100,22,20,42};
    private int[] jazz_data={7,71,66,0,40,100};
    private int[] pop_data={8,0,33,77,20,0};
    Activity activity;

    SeekBar bar1;
    SeekBar bar2;
    SeekBar bar3;
    SeekBar bar4;
    SeekBar bar5;
    Spinner spinner;
    String url;
    int preset=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);

        bar1= findViewById(R.id.playerr_eq_progress1);
        bar2= findViewById(R.id.playerr_eq_progress2);
        bar3= findViewById(R.id.playerr_eq_progress3);
        bar4= findViewById(R.id.playerr_eq_progress4);
        bar5= findViewById(R.id.playerr_eq_progress5);
        spinner = (Spinner) findViewById(R.id.eq_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.eq_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        bar1.setProgress(75);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button save_button= findViewById(R.id.eq_save);

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_equalizer.this, activity_settings.class);
                startActivity(intent);
            }
        });
        bar1.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        bar2.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        bar3.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        bar4.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        bar5.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                url = parent.getItemAtPosition(pos).toString();
                updateBars(url);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    private void updateBars(String url){

        switch (url){

            case "Normal":
                bar1.setProgress(normal_data[1]);
                bar2.setProgress(normal_data[2]);
                bar3.setProgress(normal_data[3]);
                bar4.setProgress(normal_data[4]);
                bar5.setProgress(normal_data[5]);
                preset=0;
                break;
            case "Classical":
                bar1.setProgress(classical_data[1]);
                bar2.setProgress(classical_data[2]);
                bar3.setProgress(classical_data[3]);
                bar4.setProgress(classical_data[4]);
                bar5.setProgress(classical_data[5]);
                preset=1;
                break;
            case "Dance":
                bar1.setProgress(dance_data[1]);
                bar2.setProgress(dance_data[2]);
                bar3.setProgress(dance_data[3]);
                bar4.setProgress(dance_data[4]);
                bar5.setProgress(dance_data[5]);
                preset=2;
                break;
            case "Flat":
                bar1.setProgress(flat_data[1]);
                bar2.setProgress(flat_data[2]);
                bar3.setProgress(flat_data[3]);
                bar4.setProgress(flat_data[4]);
                bar5.setProgress(flat_data[5]);
                preset=3;
                break;
            case "Folk":
                bar1.setProgress(folk_data[1]);
                bar2.setProgress(folk_data[2]);
                bar3.setProgress(folk_data[3]);
                bar4.setProgress(folk_data[4]);
                bar5.setProgress(folk_data[5]);
                preset=4;
                break;
            case "Hip Hop":
                bar1.setProgress(hip_data[1]);
                bar2.setProgress(hip_data[2]);
                bar3.setProgress(hip_data[3]);
                bar4.setProgress(hip_data[4]);
                bar5.setProgress(hip_data[5]);
                preset=6;
                break;
            case "Heavy Metal":
                bar1.setProgress(metal_data[1]);
                bar2.setProgress(metal_data[2]);
                bar3.setProgress(metal_data[3]);
                bar4.setProgress(metal_data[4]);
                bar5.setProgress(metal_data[5]);
                preset=5;
                break;
            case "Jazz":
                bar1.setProgress(jazz_data[1]);
                bar2.setProgress(jazz_data[2]);
                bar3.setProgress(jazz_data[3]);
                bar4.setProgress(jazz_data[4]);
                bar5.setProgress(jazz_data[5]);
                preset=7;
                break;
            case "Pop":
                bar1.setProgress(pop_data[1]);
                bar2.setProgress(pop_data[2]);
                bar3.setProgress(pop_data[3]);
                bar4.setProgress(pop_data[4]);
                bar5.setProgress(pop_data[5]);
                preset=8;
                break;
        }

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent homeIntent = new Intent(this, activity_settings.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
        }
        return (super.onOptionsItemSelected(menuItem));
    }


}

package com.cst2335.jone0648;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //hello
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_constraint);




        Button toastButton = findViewById(R.id.button);
        toastButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Lab2_Toast_Message), Toast.LENGTH_LONG).show();
            }
        });
        Switch switch1 = findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton cb, boolean b) {
                Snackbar snackbar = Snackbar
                        .make(findViewById(android.R.id.content), String.format("%s %s", getString(R.string.Lab2_Switch_Text_Toggle), b ? getString(R.string.Lab2_Switch_On_Text) : getString(R.string.Lab2_Switch_Off_Text)), Snackbar.LENGTH_LONG);
                snackbar.setAction( "Undo", click -> cb.setChecked(!b));
                snackbar.show();
            }
        });
    }
}
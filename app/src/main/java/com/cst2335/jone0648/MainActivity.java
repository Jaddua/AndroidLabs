package com.cst2335.jone0648;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public final static String TAG ="MAIN_ACTIVITY";
    public static final String SHAREDPREFS = "sharedPrefs";
    public static final String EMAIL = "userEmail";
    private EditText email;
    private Button login;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.login_layout );

       email = (EditText) findViewById(R.id.editTextEmail);

       login = (Button) findViewById(R.id.login_button);


       login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
                Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                profile.putExtra("EMAIL",email.getText().toString());
                startActivity(profile);
            }
        });
        load();
        update();
        Log.i(TAG, "In onCreate");
    }

    public void save() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL, email.getText().toString());
        editor.apply();
    }

    public void load() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHAREDPREFS, MODE_PRIVATE);
        userEmail = sharedPreferences.getString(EMAIL, "");
    }

    public void update() {
        email.setText(userEmail);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "In onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "In onResume");
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "In onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "In onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "In onPause");
        save();
    }

}
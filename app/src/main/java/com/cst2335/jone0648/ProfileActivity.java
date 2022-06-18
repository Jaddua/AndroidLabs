package com.cst2335.jone0648;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class ProfileActivity extends AppCompatActivity {

    public static final String TAG = "PROFILE_ACTIVITY";
    public static String EMAIL;
    private EditText email;
    private ImageView imageView;
    private ActivityResultLauncher<Intent> myPictureTakerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        email = findViewById(R.id.editTextProfileEmail);
        Intent main = getIntent();
        EMAIL = main.getStringExtra("EMAIL");
        imageView = findViewById(R.id.lab3_imageButton);


        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHAREDPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", EMAIL);

        email.setText(EMAIL);
        editor.apply();

        ImageButton ib = findViewById(R.id.lab3_imageButton);
        ib.setOnClickListener(
                (v) -> dispatchTakePictureIntent()
        );

        myPictureTakerLauncher =
                registerForActivityResult( new ActivityResultContracts.StartActivityForResult()
                        , result -> {
                            if (result.getResultCode() == Activity.RESULT_OK)
                            { Intent data = result.getData();
                                Bitmap imgbitmap = (Bitmap) data.getExtras().get("data");
                                imageView.setImageBitmap(imgbitmap); // the imageButton
                            }
                            else if(result.getResultCode() == Activity.RESULT_CANCELED)
                                Log.i(TAG, "User refused to capture a picture.");
                        });
        Log.e(TAG, "In onCreate");
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            myPictureTakerLauncher.launch(takePictureIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "In onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "In onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "In onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "In onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "In onPause");
    }
}
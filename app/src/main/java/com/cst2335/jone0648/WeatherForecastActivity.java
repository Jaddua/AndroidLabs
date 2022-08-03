package com.cst2335.jone0648;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WeatherForecastActivity extends AppCompatActivity {

    protected static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
    protected static final String URL_IMAGE = "http://openweathermap.org/img/w/";
    protected static final String UV = "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";
    protected static final String ACTIVITY = "WeatherForecastActivity";
    private ProgressBar progressBar;
    private ImageView weatherImageView;
    private TextView currentTempTextView, minTempTextView, maxTempTextView, currentLocationTextView, uvRatingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        currentLocationTextView = findViewById(R.id.currentLocationTextView);
        weatherImageView = findViewById(R.id.currentWeatherImageView);
        currentTempTextView = findViewById(R.id.currentTempTextView);
        minTempTextView = findViewById(R.id.minTempTextView);
        maxTempTextView = findViewById(R.id.maxTempTextView);
        uvRatingTextView = findViewById(R.id.uvRatingTextView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setMax(100);

        new ForecastQuery().execute(null, null, null);


    }


    private class ForecastQuery extends AsyncTask<String, Integer, String> {


        private String currentTemp = null;
        private String minTemp = null;
        private String maxTemp = null;
        private String uvRating = null;
        private String iconFileName = null;
        private Bitmap weatherBitmap = null;
        private String currentLocation = null;

        static private final String TAG = "ForecastQuery";

        protected String doInBackground(String... args) {

            InputStream inputStream = null;

            try {

                URL url = new URL(URL);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                inputStream = conn.getInputStream();


                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(inputStream, null);

                int eventType = parser.getEventType();
                boolean set = false;

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (parser.getName().equalsIgnoreCase("current")) {
                            set = true;
                        } else if (parser.getName().equalsIgnoreCase("city") && set) {
                            currentLocation = parser.getAttributeValue(null, "name");
                        } else if (parser.getName().equalsIgnoreCase("temperature") && set) {
                            currentTemp = parser.getAttributeValue(null, "value");
                            publishProgress(25);
                            minTemp = parser.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemp = parser.getAttributeValue(null, "max");
                            publishProgress(75);

                        } else if (parser.getName().equalsIgnoreCase("weather") && set) {
                            iconFileName = parser.getAttributeValue(null, "icon") + ".png";
                            File file = getBaseContext().getFileStreamPath(iconFileName);
                            if (!file.exists()) {
                                saveImage(iconFileName);
                            } else {
                                Log.i(ACTIVITY, "Saved icon, " + iconFileName + " is displayed.");
                                try {
                                    FileInputStream in = new FileInputStream(file);
                                    weatherBitmap = BitmapFactory.decodeStream(in);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    Log.i(ACTIVITY, "Saved icon, " + iconFileName + " is not found.");
                                }
                            }
                            publishProgress(100);

                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                        if (parser.getName().equalsIgnoreCase("current"))
                            set = false;
                    }
                    eventType = parser.next();
                }

            } catch (IOException e) {
                Log.i(ACTIVITY, "IOException: " + e.getMessage());
            } catch (XmlPullParserException e) {
                Log.i(ACTIVITY, "XmlPullParserException: " + e.getMessage());
            }
            try {


                URL url = new URL(UV);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                InputStream response = urlConnection.getInputStream();


                BufferedReader reader = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();


                JSONObject uvReport = new JSONObject(result);


                uvRating = uvReport.getString("value");

                publishProgress(25);
                Thread.sleep(1000);
                publishProgress(50);
                Log.i(TAG, "Num of entries: " + uvRating);

            } catch (Exception e) {

            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.i(ACTIVITY, "IOException: " + e.getMessage());
                    }
                }
                return null;
            }

        }


        @Override
        public void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
            if (values[0] == 100) {

            }
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentLocationTextView.setText(getString(R.string.lab6_current_location_text) + String.format(" %s", currentLocation));
            currentTempTextView.setText(getString(R.string.lab6_current_temp_text) + String.format(" %.1f", Double.parseDouble(currentTemp)) + "\u00b0");
            minTempTextView.setText(getString(R.string.lab6_min_temp_text) + String.format(" %.1f", Double.parseDouble(minTemp)) + "\u00b0");
            maxTempTextView.setText(getString(R.string.lab6_max_temp_text) + String.format(" %.1f", Double.parseDouble(maxTemp)) + "\u00b0");
            uvRatingTextView.setText(getString(R.string.lab6_uv_rating_text) + String.format(" %s", uvRating));
            weatherImageView.setImageBitmap(weatherBitmap);
            progressBar.setVisibility(View.INVISIBLE);
        }

        private void saveImage(String fname) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL(URL_IMAGE + fname);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    weatherBitmap = BitmapFactory.decodeStream(connection.getInputStream());
                    FileOutputStream outputStream = openFileOutput(fname, Context.MODE_PRIVATE);
                    weatherBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Log.i(ACTIVITY, "Weather icon, " + fname + " is downloaded and displayed.");
                } else
                    Log.i(ACTIVITY, "Can't connect to the weather icon for downloading.");
            } catch (Exception e) {
                Log.i(ACTIVITY, "weather icon download error: " + e.getMessage());
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

            }
        }
    }
}

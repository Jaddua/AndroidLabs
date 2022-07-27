package com.cst2335.jone0648;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecastActivity extends AppCompatActivity {


    //TextView CurrentTemp = findViewById(R.id.Lab6CurrentTempDisplay);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ProgressBar progressBar = findViewById(R.id.lab6_progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ForecastQuery req = new ForecastQuery();
        req.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");

       // req.doInBackground("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric");
        //TextView CurrentTemp = findViewById(R.id.Lab6CurrentTempDisplay);
        //currentTemp.append(String.format(" %s °C", req.currentTemp));
    }
    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        String currentTemp;
        String maxTemp;
        String minTemp;
        String UVRating;
        String iconName;
        Bitmap weatherImage;


        protected String doInBackground(String ... args)
        {


            try {
                //create a URL object of what server to contact:
                URL url = new URL(args[0]);
                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //wait for data:
                InputStream response = urlConnection.getInputStream();



                //From part 3: slide 19
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");



                //From part 3, slide 20
                String parameter = null;

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                            currentTemp = xpp.getAttributeValue(null,    "value");
                            publishProgress(25, 50, 75);
                           Log.i("Current Temperature   ", currentTemp);
                            // CurrentTemp.setText("Current Temperature: " + currentTemp);
                            minTemp = xpp.getAttributeValue(null, "min");
                            publishProgress(25, 50, 75);
                            maxTemp = xpp.getAttributeValue(null, "max");
                            publishProgress(25, 50, 75);
                        }

                        else if(xpp.getName().equals("weather"))
                        {
                            iconName = xpp.getAttributeValue(null, "icon");
                            publishProgress(25, 50, 75);
                            Log.e("Icon Name", iconName);
                        }/*
                        else if(xpp.getName().equals("Weather"))
                        {
                            parameter = xpp.getAttributeValue(null, "outlook"); //this will run for <Weather outlook="parameter"
                            parameter = xpp.getAttributeValue(null, "windy"); //this will run for <Weather windy="paramter"  >
                        }
                        else if(xpp.getName().equals("Temperature"))
                        {
                            xpp.next(); //move the pointer from the opening tag to the TEXT event
                            parameter = xpp.getText(); // this will return  20
                        }*/
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }


            }
            catch (Exception e)
            {
            }


            return "Done";
        }
    }
}
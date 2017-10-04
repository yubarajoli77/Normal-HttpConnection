package com.example.yubaraj.httpconnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {
    private TextView movieName;
    private TextView year;
    private TextView jsonData;
    private Button hitServer;
    private Button getJson;
    private HttpURLConnection httpURLConnection = null;
    private BufferedReader reader = null;
    private URL url;
    private ProgressDialog progressDialog;
    private String mName, mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hitServer = (Button) findViewById(R.id.btn_hit);
        getJson = (Button) findViewById(R.id.btn_json);
        movieName = (TextView) findViewById(R.id.txtView_movieName);
        year = (TextView) findViewById(R.id.txtView_year);
        jsonData = (TextView) findViewById(R.id.txtView_json);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Fetching data...");

        hitServer.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        new FormatedJsonTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
                    }
                }
        );
        //For json view

        getJson.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.show();
                        new JsonTask().execute("https://jsonparsingdemo-cec5b.firebaseapp.com/jsonData/moviesDemoItem.txt");
                    }
                }
        );


    }

    // We are doing this extra work because the httpConnection must done in background because we don't
    // know how much time does it take to fetch data

    public class JsonTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream stream = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String jsonData=buffer.toString();
                JSONObject parentObject=new JSONObject(jsonData);
                JSONArray parentArray= parentObject.getJSONArray("movies");
                JSONObject getData= parentArray.getJSONObject(0);
                mName=getData.getString("movie");
                mYear=getData.getString("year");
                return mName + "-" + mYear;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    progressDialog.dismiss();
                    httpURLConnection.disconnect();
                }
            }
            if (reader != null) {
                try {
                    progressDialog.dismiss();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            jsonData.setText(result.toString());
            super.onPostExecute(result);
        }
    }

    public class FormatedJsonTask extends AsyncTask<String, String, String>{
        @Override
        protected String doInBackground(String... params) {
            try {
                url = new URL(params[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream stream = httpURLConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String jsonData=buffer.toString();
                JSONObject parentObject=new JSONObject(jsonData);
                JSONArray parentArray= parentObject.getJSONArray("movies");
                JSONObject getData= parentArray.getJSONObject(0);
                mName=getData.getString("movie");
                mYear=getData.getString("year");
                return mName + "-" + mYear;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    progressDialog.dismiss();
                    httpURLConnection.disconnect();
                }
            }
            if (reader != null) {
                try {
                    progressDialog.dismiss();
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            //movieName.setText(s);
            movieName.setText(mName);
            year.setText(mYear);
            super.onPostExecute(s);

        }
    }
}


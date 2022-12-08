package com.example.bookshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class addBook extends AppCompatActivity {

    String api = "http://172.20.10.2:42908/api/Book/CreateBook1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        Button btn = findViewById(R.id.button3);
        Button btn_cancel = findViewById(R.id.button5);

        EditText name = findViewById(R.id.editTextTextPersonName);
        EditText author = findViewById(R.id.editTextTextPersonName2);
        EditText price = findViewById(R.id.editTextTextPersonName3);
        EditText description = findViewById(R.id.editTextTextPersonName4);
        EditText publisher = findViewById(R.id.editTextTextPersonName5);
        EditText year = findViewById(R.id.editTextTextPersonName6);
        EditText pages = findViewById(R.id.editTextTextPersonName7);
        EditText img = findViewById(R.id.editTextTextPersonName8);



        btn_cancel.setOnClickListener(v -> {
            Intent data = new Intent();
            setResult(RESULT_CANCELED, data);
            closeActivity();
        });

        btn.setOnClickListener(v -> {
            Log.e("api", api);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(api);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                        connection.setRequestProperty("Accept", "text/json");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);

                        JSONObject params = new JSONObject();
                        params.put("id", 0);
                        params.put("name", "" + name.getText().toString() + "");
                        params.put("author", "" + author.getText().toString() +"");
                        params.put("description", description.getText());
                        params.put("price", price.getText());
                        params.put("publisher", publisher.getText());
                        params.put("year", year.getText());
                        params.put("pages", pages.getText());
                        params.put("img_url", img.getText());

                        DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                        os.writeBytes(params.toString());

                        os.flush();
                        os.close();

                        Log.e("api", connection.getResponseMessage());

                        connection.disconnect();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
                Log.e("api", "Отправвили");
                Intent data = new Intent();
                data.putExtra("id", "add");
                setResult(RESULT_OK, data);
                closeActivity();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        });
    }
    private void closeActivity() {
        this.finish();
    }
}
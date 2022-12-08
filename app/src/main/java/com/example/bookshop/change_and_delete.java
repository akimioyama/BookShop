package com.example.bookshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class change_and_delete extends AppCompatActivity {

    String api = "http://172.20.10.2:42908/api/Book/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_change_and_delete);

        Button btn = findViewById(R.id.button6);
        Button btnDel = findViewById(R.id.button4);
        Button btnPut = findViewById(R.id.button2);
        EditText name = findViewById(R.id.editTextTextPersonName9);
        EditText author = findViewById(R.id.editTextTextPersonName10);
        EditText price = findViewById(R.id.editTextTextPersonName11);
        EditText pages = findViewById(R.id.editTextTextPersonName15);
        EditText description = findViewById(R.id.editTextTextPersonName12);
        EditText publisher = findViewById(R.id.editTextTextPersonName13);
        EditText year = findViewById(R.id.editTextTextPersonName14);
        EditText img = findViewById(R.id.editTextTextPersonName16);

        Bundle extras = getIntent().getExtras();
        int q = extras.getInt("id");
        String qq = Integer.toString(q);
        api = api + qq;
        Log.e("api", api);

        new getPersonAPI().execute(api);


        btn.setOnClickListener(v -> {
            closeActivity();
        });
        btnDel.setOnClickListener(v -> {
            String api = "http://172.20.10.2:42908/api/Book/del/" + qq;
            Log.e("api", api);
            Intent data = new Intent();
            data.putExtra("id", "del");
            setResult(RESULT_OK, data);
            new DeleteAPI().execute(api);
        });
        btnPut.setOnClickListener(v -> {

            String api1 = "http://172.20.10.2:42908/api/Book/post/" + qq;
            Log.e("api", api1);

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(api1);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);

                        JSONObject params = new JSONObject();
                        params.put("id", 0);
                        params.put("name", name.getText().toString());
                        params.put("author", "" + author.getText() + "");
                        params.put("description", "" + description.getText() + "");
                        params.put("price", price.getText().toString());
                        params.put("publisher", "" + publisher.getText() + "");
                        params.put("year", "" + year.getText() + "");
                        params.put("pages", "" + pages.getText() + "");
                        params.put("img_url", "" + img.getText() + "");

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
                data.putExtra("id", "put");
                setResult(RESULT_OK, data);
                closeActivity();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }
    private class DeleteAPI extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            super.onPreExecute();
            Log.e("api", "Ожидаем ответ от сервера");
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.e("api", result);
            Intent data = new Intent();
            data.putExtra("id", "del");
            setResult(RESULT_OK, data);
            closeActivity();
        }
    }
    private class getPersonAPI extends AsyncTask<String, String, String> {

        protected void onPreExecute(){
            super.onPreExecute();
            Log.e("api", "Ожидаем ответ от сервера");
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null)
                    buffer.append(line).append("\n");
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.disconnect();
                try {
                    if (reader != null)
                        reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            Log.e("api", result);

            JSONArray array = null;
            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject singleObject = null;
            try {
                singleObject = array.getJSONObject(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Book temp = new Book(
                        Integer.parseInt(singleObject.getString("id")),
                        singleObject.getString("name"),
                        singleObject.getString("author"),
                        singleObject.getString("description"),
                        singleObject.getString("price"),
                        singleObject.getString("publisher"),
                        singleObject.getString("year"),
                        singleObject.getString("pages"),
                        singleObject.getString("img_url"));

                EditText name = findViewById(R.id.editTextTextPersonName9);
                EditText author = findViewById(R.id.editTextTextPersonName10);
                EditText price = findViewById(R.id.editTextTextPersonName11);
                EditText pages = findViewById(R.id.editTextTextPersonName15);
                EditText description = findViewById(R.id.editTextTextPersonName12);
                EditText publisher = findViewById(R.id.editTextTextPersonName13);
                EditText year = findViewById(R.id.editTextTextPersonName14);
                EditText img = findViewById(R.id.editTextTextPersonName16);

                name.setText(temp.getName());
                author.setText(temp.getAuthor());
                price.setText(temp.getPrice());
                pages.setText(temp.getPages());
                description.setText(temp.getDescription());
                publisher.setText(temp.getPublisher());
                year.setText(temp.getYear());
                img.setText(temp.getImg_url());
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void closeActivity() {
        this.finish();
    }
}
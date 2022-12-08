package com.example.bookshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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

import javax.net.ssl.HttpsURLConnection;

public class oneBook extends AppCompatActivity {

    String api = "http://172.20.10.2:42908/api/Book/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_book);

        Button btn = findViewById(R.id.button7);

        Bundle extras = getIntent().getExtras();
        int q = extras.getInt("id");
        String qq = Integer.toString(q);
        api = api + qq;
        Log.e("api", api);

        btn.setOnClickListener(view -> {
            closeActivity();
        });

        new getPersonAPI().execute(api);

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

                TextView name = findViewById(R.id.textView4);
                TextView author = findViewById(R.id.textView5);
                TextView price = findViewById(R.id.textView8);
                TextView pages = findViewById(R.id.textView11);
                TextView description = findViewById(R.id.textView9);
                TextView publisher = findViewById(R.id.textView7);
                TextView year = findViewById(R.id.textView13);
                ImageView img = findViewById(R.id.imageView2);

                name.setText(temp.getName());
                author.setText(temp.getAuthor());
                description.setText(temp.getDescription());
                price.setText(temp.getPrice());
                publisher.setText(temp.getPublisher());
                year.setText(temp.getYear());
                pages.setText(temp.getYear());
//                img.setBackgroundResource(temp.getImg_url());
                String qq = temp.getImg_url();

                new Thread(new Runnable() {
                    public void run() {
                        try {
                            Bitmap image = getContent(qq);

                            img.post(new Runnable() {
                                @Override
                                public void run() {
                                    img.setImageBitmap(image);
                                }
                            });
                        }
                        catch (IOException e) {
                            Log.e("aaa", e.getLocalizedMessage());
                        }
                        catch (Exception e) {
                            Log.e("aaa", e.getLocalizedMessage());
                        }
                    }
                    private Bitmap getContent(String path) throws IOException {
                        BufferedReader reader=null;
                        InputStream stream = null;
                        HttpsURLConnection connection = null;
                        try {
                            URL url=new URL(path);
                            //connection =(HttpsURLConnection)url.openConnection();
                            connection =(HttpsURLConnection)url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.connect();
                            Bitmap bitmap = null;

                            stream = connection.getInputStream();
                            bitmap = BitmapFactory.decodeStream(stream);
                            Log.println(Log.ERROR,"api","Конец конекта");
                            return bitmap;
                        }
                        finally {
                            if (reader != null) {
                                reader.close();
                            }
                            if (stream != null) {
                                stream.close();
                            }
                            if (connection != null) {
                                connection.disconnect();
                            }
                        }
                    }
                }).start();


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    private void closeActivity() {
        this.finish();
    }

}
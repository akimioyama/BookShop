package com.example.bookshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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
import java.util.HashMap;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    String role = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);
        EditText login = findViewById(R.id.login);
        EditText password = findViewById(R.id.password);

        btn.setOnClickListener(view -> {
            String log = String.valueOf(login.getText());
            String pass = String.valueOf(password.getText());
            String api = "http://172.20.10.2:42908/api/User/" + log + "/" + pass;
            Log.e("api", api);
            new getPersonAPI(this).execute(api);
       });
    }
    private class getPersonAPI extends AsyncTask<String, String, String>{

        Context context;
        private getPersonAPI(Context context){
            this.context = context.getApplicationContext();
        }

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
                    buffer.append(line);
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
            Log.e("api", "result: " + result);

            if(Objects.equals(result, "one")){
                Log.e("api", "1");
                Intent intent = new Intent(context, allBooks.class);
                startActivity(intent);
            }
            else if(result.toString().equals("two")){
                Log.e("api", "2");
                Intent intent = new Intent(context, allBooks_user.class);
                startActivity(intent);
            }
        }
    }
}
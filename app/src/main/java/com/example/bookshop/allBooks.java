package com.example.bookshop;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class allBooks extends AppCompatActivity {
    ArrayList<Book> books = new ArrayList<Book>();
    GridView gridList;
    JSONArray jsonArray;
    String api = "http://172.20.10.2:42908/api/Book";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_book);

        FloatingActionButton btn = findViewById(R.id.fab);

        gridList = findViewById(R.id.gridList);
        StateAdapter stateAdapter = new StateAdapter(this, R.layout.grid_item, books);
        gridList.setAdapter(stateAdapter);

        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK){
                            Intent intent = result.getData();
                            String id = intent.getStringExtra("id");
                            if(Objects.equals(id, "add")){
                                books.clear();
                                new getPersonAPI(allBooks.this).execute(api);
                            }
                            if(Objects.equals(id, "del")){
                                books.clear();
                                new getPersonAPI(allBooks.this).execute(api);
                            }
                            if(Objects.equals(id, "put")){
                                books.clear();
                                new getPersonAPI(allBooks.this).execute(api);
                            }
                        }
                        if(result.getResultCode() == Activity.RESULT_CANCELED){

                        }
                    }
                });

        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("qwe", "onItemClick: i = " + i + " l= " + l);
                int q = books.get(i).getId();
                String name = books.get(i).getName();
                String author = books.get(i).getAuthor();
                String description = books.get(i).getDescription();
                String price = books.get(i).getPrice();
                String publisher = books.get(i).getPublisher();
                String year = books.get(i).getYear();
                String pages = books.get(i).getPages();
                String img_url = books.get(i).getImg_url();


                Log.e("qwe", "ID BOOK = " + q);

                Intent intent = new Intent(allBooks.this, change_and_delete.class);
                intent.putExtra("id", q);
                mStartForResult.launch(intent);
            }
        });

        btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, addBook.class);
            mStartForResult.launch(intent);
        });

        new getPersonAPI(this).execute(api);

    }

    private class getPersonAPI extends AsyncTask<String, String, String> {

        Context context;
        private getPersonAPI(Context context){
            this.context = context.getApplicationContext();
        }

        protected void onPreExecute(){
            super.onPreExecute();
//            Log.e("api", "Ожидаем ответ от сервера");
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

            JSONArray array = null;
            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < array.length(); i++){
                JSONObject singleObject = null;
                try {
                    singleObject = array.getJSONObject(i);
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
                    books.add(temp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonArray = array;
            StateAdapter stateAdapter = new StateAdapter(context, R.layout.grid_item, books);
            GridView gridList = findViewById(R.id.gridList);
            gridList.setAdapter(stateAdapter);
            stateAdapter.notifyDataSetChanged();
        }
    }
}
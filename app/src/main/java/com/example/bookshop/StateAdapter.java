package com.example.bookshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class StateAdapter extends ArrayAdapter<Book> {
    private LayoutInflater inflater;
    private int Layout;
    private List<Book> books;

    public StateAdapter(Context context, int resource, List<Book> books){
        super(context, resource, books);
        this.books = books;
        this.Layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        View view = inflater.inflate(this.Layout, parent, false);

        ImageView bookView = view.findViewById(R.id.bookImg);
        TextView nameView = view.findViewById(R.id.name);
        TextView autherView = view.findViewById(R.id.auther);
        TextView priceView = view.findViewById(R.id.price);

        Book book = books.get(position);

        nameView.setText(book.getName());
        autherView.setText(book.getAuthor());
        priceView.setText(book.getPrice());

        new Thread(new Runnable() {
            public void run() {
                try {
                    Bitmap image = getContent(book.getImg_url());

                    bookView.post(new Runnable() {
                        @Override
                        public void run() {
                            bookView.setImageBitmap(image);
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
        }).start();
        return view;
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
}

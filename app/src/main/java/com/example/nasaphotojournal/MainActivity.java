package com.example.nasaphotojournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//Poner el Cache como Singleton


public class MainActivity extends AppCompatActivity {

    private List<nasaPhoto> nasaPhotoList = new ArrayList<>();
    private RecyclerView recyclerView;
    private nasaPhotoAdapter nasaPhotoAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.idRcView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        nasaPhotoAdapter = new nasaPhotoAdapter(nasaPhotoList, this);
        recyclerView.setAdapter(nasaPhotoAdapter);

        GetNasaPhotoTask getLocalWeatherTask = new GetNasaPhotoTask();
        getLocalWeatherTask.execute("https://photojournal.jpl.nasa.gov/gallery/universe");
    }

    private class GetNasaPhotoTask
            extends AsyncTask<String, Void, List<nasaPhoto>> {

        @Override
        protected List<nasaPhoto> doInBackground(String...params) {
            Document doc = null;
            try {
                doc = Jsoup.connect(params[0]).get();
                Elements thumbs = doc.select("a > img[src]");
                Elements names = doc.select("dd > b");
                Elements url = doc.select("dd > a");
                Elements size = doc.select("dd > font");

                for (int i = 0; i < thumbs.size(); i++) {

                    if (!thumbs.get(i).attr("src").contains("/thumb/PIA")) {
                        thumbs.remove(i);
                    }
                }
                thumbs.remove(0);

                for (int i = 0; i < url.size()-1 ; i++) {

                    if (!url.get(i).attr("href").contains("/jpeg/")) {
                        url.remove(i);
                    }

                }

                for (int i = 0; i < 100 ; i++) {

                    Element t = thumbs.get(i);
                    Element n = names.get(i*2);
                    Element u = url.get(i);
                    Element s = size.get(2*i+1);

                    nasaPhotoList.add(new nasaPhoto(
                            t.attr("src"),
                            n.text(),
                            u.attr("href"),
                            s.text()
                    ));

                }
                return nasaPhotoList;

            } catch (IOException e) {
                Log.d("Error: ", "Error: "+ e);
            }
            return nasaPhotoList;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(List<nasaPhoto> nasaPhotos) {
            super.onPostExecute(nasaPhotos);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, android.R.anim.fade_out));
            nasaPhotoAdapter.notifyDataSetChanged();
        }
    }






}




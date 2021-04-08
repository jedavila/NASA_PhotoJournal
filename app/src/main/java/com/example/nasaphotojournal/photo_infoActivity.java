package com.example.nasaphotojournal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class photo_infoActivity extends AppCompatActivity {
    ImageView imagePhoto;
    TextView txtTitulo;
    TextView txtSize;
    ProgressBar progressBar;

    String urlImage;

    private Map<String, Bitmap> bitmaps = new HashMap<>();//cache

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_info);


        imagePhoto = findViewById(R.id.imgPhoto);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtSize = findViewById(R.id.txtTamano);
        progressBar = findViewById(R.id.progressBar);

        txtTitulo.setText(getIntent().getStringExtra("title"));
        txtSize.setText(getIntent().getStringExtra("tamano"));
        urlImage = getIntent().getStringExtra("ImageUrl");

        if (bitmaps.containsKey(urlImage)) {
            imagePhoto.setImageBitmap(
                    bitmaps.get(urlImage));
        }
        else {
            new photo_infoActivity.LoadImageTask(imagePhoto).execute(
                    urlImage);
        }

    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }


        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;

            try {
                URL url = new URL(params[0]);

                connection = (HttpURLConnection) url.openConnection();

                try (InputStream inputStream = connection.getInputStream()) {
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmapOptions.inSampleSize = 4;


                    bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions );
                    bitmaps.put(params[0], bitmap);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                connection.disconnect();
            }

            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(photo_infoActivity.this, android.R.anim.fade_in));
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
            progressBar.setVisibility(View.GONE);
            progressBar.startAnimation(AnimationUtils.loadAnimation(photo_infoActivity.this, android.R.anim.fade_out));
        }
    }




}

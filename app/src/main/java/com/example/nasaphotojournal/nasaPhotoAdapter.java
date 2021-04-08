package com.example.nasaphotojournal;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class nasaPhotoAdapter extends RecyclerView.Adapter<nasaPhotoAdapter.ViewHolder> {
    private List<nasaPhoto> nasaItems;
    private Context context;

    private Map<String, Bitmap> bitmaps = new HashMap<>();//cache



    public nasaPhotoAdapter(List<nasaPhoto> nasaItems, Context context) {
        this.nasaItems = nasaItems;
        this.context = context;
    }

    @NonNull
    @Override
    public nasaPhotoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_element, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull nasaPhotoAdapter.ViewHolder holder, int position) {
        nasaPhoto nasa = nasaItems.get(position);
        holder.txtTitulo.setText(nasa.getName());
        holder.txtTamano.setText(nasa.getSize());

        if (bitmaps.containsKey(nasa.getIconUrl())) {
            holder.icon.setImageBitmap(
                    bitmaps.get(nasa.getIconUrl()));
        }
        else {
            new LoadImageTask(holder.icon).execute(
                    nasa.getIconUrl());
        }
    }

    @Override
    public int getItemCount() {
        return nasaItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView txtTitulo;
        TextView txtTamano;

        public ViewHolder(@NonNull View view) {
            super(view);
            icon = view.findViewById(R.id.iconImageView);
            txtTitulo = view.findViewById(R.id.txtTitulo);
            txtTamano = view.findViewById(R.id.txtTamano);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            int position = getAdapterPosition();
            Log.d("Click", "onClick: Se ha clickeado ");

            nasaPhoto nasaItem =  nasaItems.get(position);

            Intent intent = new Intent(context, photo_infoActivity.class);
            intent.putExtra("title", nasaItem.getName());
            intent.putExtra("tamano", nasaItem.getSize());
            intent.putExtra("ImageUrl", nasaItem.getImageUrl());

            context.startActivity(intent);
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
                    bitmap = BitmapFactory.decodeStream(inputStream);
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
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}

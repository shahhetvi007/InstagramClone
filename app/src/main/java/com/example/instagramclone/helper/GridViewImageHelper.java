package com.example.instagramclone.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class GridViewImageHelper extends ArrayAdapter<String> {

    Context context;
    LayoutInflater layoutInflater;
    int layoutResource;
    String mAppend;
    ArrayList<String> imageUrls;

    public GridViewImageHelper(@NonNull Context context, int resource, String mAppend, ArrayList<String> imageUrls) {
        super(context, resource, imageUrls);
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.layoutResource = resource;
        this.mAppend = mAppend;
        this.imageUrls = imageUrls;
    }

    public static class Holder{
        ImageView imageView;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            holder = new Holder();
            holder.imageView = convertView.findViewById(R.id.gridImage);
            holder.progressBar = convertView.findViewById(R.id.gridProgressBar);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        String imageUrl = getItem(position);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(mAppend + imageUrl, holder.imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                if (holder.progressBar != null){
                    holder.progressBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (holder.progressBar != null){
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (holder.progressBar != null){
                    holder.progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                if (holder.progressBar != null){
                    holder.progressBar.setVisibility(View.GONE);
                }
            }
        });

        return convertView;
    }
}

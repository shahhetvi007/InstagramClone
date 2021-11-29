package com.example.instagramclone.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.instagramclone.MainActivity;
import com.example.instagramclone.R;
import com.example.instagramclone.helper.GridViewImageHelper;
import com.example.instagramclone.helper.UniversalImageHelper;
import com.example.instagramclone.utils.FileSearch;
import com.example.instagramclone.utils.ImagePath;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

public class SelectPostActivity extends AppCompatActivity {

    private GridView galleryImages;
    private ArrayList<String> directories;
    private ImageView back, right, pic_selected;
    private ProgressBar progressBar;
    private Spinner spinner;
    String mAppend = "file:/";
    private int Num_Grid_Column = 4;
    String selectedImage;

    public static final String TAG = SelectPostActivity.class.getSimpleName();
    private static final int REQUEST_STORAGE_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_post);


        galleryImages = findViewById(R.id.galleryImages);
        pic_selected = findViewById(R.id.pic_selected);

        spinner = findViewById(R.id.spinner);
        progressBar = findViewById(R.id.progressBar);

        directories = new ArrayList<>();
        checkPermission();
        initImageLoader();
//        getFolder();

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectPostActivity.this, MainActivity.class));
                finish();
            }
        });

        right = findViewById(R.id.right);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectPostActivity.this, AddPostActivity.class);
                intent.putExtra("selectedImage", selectedImage);
                Log.d("selectPost", "Selected image" + selectedImage);
                startActivity(intent);

            }
        });

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , REQUEST_STORAGE_PERMISSION);
        } else {
            getFolder();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    getFolder();
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initImageLoader(){
        UniversalImageHelper universalImageHelper = new UniversalImageHelper(this);
        ImageLoader.getInstance().init(universalImageHelper.getConfig());
    }

    private void getFolder(){
        ImagePath imagePath = new ImagePath();
        if (FileSearch.getFolderPath(imagePath.Pictures) != null){
            directories = FileSearch.getFolderPath(imagePath.Pictures);
        }
        directories.add(imagePath.Camera);

        final ArrayList<String> folderName = new ArrayList<>();
        for (int i=0; i<directories.size(); i++){
            String string = directories.get(i).replace("/storage/emulated/0/", "");
            folderName.add(string);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, folderName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemSelected: Image path :- " + directories.get(position));
                try {
                    setUpGridViewImages(directories.get(position));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpGridViewImages(String selectedFolder){
        ArrayList<String> imageUrl = FileSearch.getFilePath(selectedFolder);

        int grid_width = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = grid_width/Num_Grid_Column;
        galleryImages.setColumnWidth(imageWidth);

        GridViewImageHelper gridViewImageHelper = new GridViewImageHelper(
                this,
                R.layout.grid_image_view_layout,
                mAppend,
                imageUrl);
        galleryImages.setAdapter(gridViewImageHelper);
        try {
            setImageUrl(imageUrl.get(0), pic_selected, mAppend);
        } catch (Exception e) {
            e.printStackTrace();
        }

        galleryImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    setImageUrl(imageUrl.get(position), pic_selected, mAppend);
                    selectedImage = imageUrl.get(position);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setImageUrl(String imageUrl, ImageView imageView, String append){
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(append + imageUrl, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}


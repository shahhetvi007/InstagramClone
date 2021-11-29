package com.example.instagramclone.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagramclone.MainActivity;
import com.example.instagramclone.R;
import com.example.instagramclone.helper.UniversalImageHelper;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    ImageView back, profile, postImage;
    TextView share;
    SocialAutoCompleteTextView caption;
    String mAppend = "file:/";
    String imageUrl;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        back = findViewById(R.id.back);
        profile = findViewById(R.id.profile);
        postImage = findViewById(R.id.postImage);
        share = findViewById(R.id.share);
        caption = findViewById(R.id.caption);
        intent = getIntent();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setImage();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });
    }

    private void upload() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading..");
        pd.show();
        imageUrl = intent.getStringExtra("selectedImage");

        if (imageUrl != null){
            final StorageReference filePath = FirebaseStorage.getInstance().getReference("Posts")
                    .child(System.currentTimeMillis() + "." + getFileExtension(Uri.parse(imageUrl)));
            StorageTask uploadTask = filePath.putFile(Uri.fromFile(new File(imageUrl)));
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                    String postId = ref.push().getKey();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("postId", postId);
                    map.put("imageUrl", imageUrl);
                    map.put("caption", caption.getText().toString());
                    map.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                    ref.child(postId).setValue(map);

                    DatabaseReference hashtagRef = FirebaseDatabase.getInstance().getReference().child("HashTags");
                    List<String> hashTags = caption.getHashtags();
                    if (!hashTags.isEmpty()){
                        for (String tag : hashTags){
                            map.clear();
                            map.put("tag", tag.toLowerCase());
                            map.put("postId", postId);

                            hashtagRef.child(tag.toLowerCase()).child(postId).setValue(map);
                        }
                    }
                    pd.dismiss();
                    Toast.makeText(AddPostActivity.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddPostActivity.this, MainActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(AddPostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            pd.dismiss();
            Toast.makeText(this, "No image was selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(uri));

    }

    private void setImage(){
        imageUrl = intent.getStringExtra("selectedImage");
        UniversalImageHelper.setImage(imageUrl, postImage, null, mAppend);
    }
}
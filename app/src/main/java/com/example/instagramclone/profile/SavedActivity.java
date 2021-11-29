package com.example.instagramclone.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.instagramclone.R;
import com.example.instagramclone.home.PostData;
import com.example.instagramclone.profile.posts.PhotoAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SavedActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView savedRV;
    private PhotoAdapter savedAdapter;
    private List<PostData> savedPosts;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Saved");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        onBackPressed();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        savedRV = findViewById(R.id.savedRV);
        savedRV.setHasFixedSize(true);
        savedRV.setLayoutManager(new GridLayoutManager(this, 3));
        savedPosts = new ArrayList<>();
        savedAdapter = new PhotoAdapter(SavedActivity.this, savedPosts);
        savedRV.setAdapter(savedAdapter);

        getSavedPosts();
    }

    private void getSavedPosts() {
        List<String> savedIds = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    savedIds.add(dataSnapshot.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        savedPosts.clear();
                        for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()){
                            PostData post = dataSnapshot1.getValue(PostData.class);
                            for (String id : savedIds){
                                if (post.getPostId().equals(id)){
                                    savedPosts.add(post);
                                }
                            }
                        }
                        savedAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
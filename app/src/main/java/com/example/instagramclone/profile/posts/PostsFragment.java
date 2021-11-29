package com.example.instagramclone.profile.posts;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.R;
import com.example.instagramclone.home.PostData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostsFragment extends Fragment {

    private RecyclerView postsRv;
    private PhotoAdapter adapter;
    private List<PostData> myPosts;
    String profileId;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        postsRv = view.findViewById(R.id.postsRV);
        postsRv.setHasFixedSize(true);
        postsRv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myPosts = new ArrayList<>();
        adapter = new PhotoAdapter(getContext(), myPosts);
        postsRv.setAdapter(adapter);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getContext().getSharedPreferences("prefes", Context.MODE_PRIVATE).getString("profileid", "none");
        Log.d("Profileid" , data);
        if (data.equals("none")) {
            profileId = firebaseUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }

        getPosts();

        return view;
    }

    private void getPosts() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myPosts.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostData postData = dataSnapshot.getValue(PostData.class);
                    if (profileId.equals(postData.getPublisher())){
                        myPosts.add(postData);
                    }
                }
                Collections.reverse(myPosts);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
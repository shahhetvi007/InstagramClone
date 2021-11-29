package com.example.instagramclone.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.R;
import com.example.instagramclone.profile.OpenAddPostDialogFragment;
import com.example.instagramclone.search.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

   private RecyclerView storyRV, postRv;
   private ImageView add_post;
   private StoryAdapter storyAdapter;
   private List<StoryData> storyData;
   private PostAdapter postAdapter;
    private Toolbar toolbar;
    private ImageView dm;

    private List<PostData> postList;
    private List<String> followingList;
    private Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dm = view.findViewById(R.id.dm);
        dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        //setup toolbar
        toolbar = view.findViewById(R.id.toolbar);

        add_post = view.findViewById(R.id.add_post);
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectPostActivity.class);
                startActivity(intent);
            }
        });

        storyData = new ArrayList<>();
        storyRV = view.findViewById(R.id.story_RV);
        storyAdapter = new StoryAdapter(getContext(), storyData);
        storyRV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setStackFromEnd(true);
        storyRV.setLayoutManager(layoutManager);
        storyRV.setAdapter(storyAdapter);

        postList = new ArrayList<>();
        postRv = view.findViewById(R.id.post_RV);
        postAdapter = new PostAdapter(getContext(), postList);
        postRv.setHasFixedSize(true);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        layoutManager1.setStackFromEnd(true);
        postRv.setLayoutManager(layoutManager1);
        postRv.setAdapter(postAdapter);

        followingList = new ArrayList<>();

        checkFollowingUsers();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }


    private void checkFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    followingList.add(dataSnapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
                readStories();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readStories() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Story");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long timeCurrent = System.currentTimeMillis();
                storyData.clear();
                storyData.add(new StoryData("", 0, 0, "", FirebaseAuth.getInstance().getCurrentUser().getUid()));
                for (String id : followingList){
                    int storyCount = 0;
                    StoryData story = null;
                    for (DataSnapshot dataSnapshot : snapshot.child(id).getChildren()){
                        story = dataSnapshot.getValue(StoryData.class);
                        if (timeCurrent > story.getTimeStart() && timeCurrent < story.getTimeEnd()){
                            storyCount++;
                        }
                    }
                    if (storyCount > 0){
                        storyData.add(story);
                    }
                }
                storyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readPosts() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostData postData = dataSnapshot.getValue(PostData.class);

                    for (String id : followingList){
                        if (postData.getPublisher().equals(id)){
                            postList.add(postData);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
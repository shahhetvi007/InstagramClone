package com.example.instagramclone.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.FollowersActivity;
import com.example.instagramclone.R;
import com.example.instagramclone.home.PostData;
import com.example.instagramclone.home.StoryAdapter;
import com.example.instagramclone.home.StoryData;
import com.example.instagramclone.search.UserData;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class ProfileFragment extends Fragment {

//    private RecyclerView highlightsStory_RV;
//    private StoryAdapter storyAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView bottomSheet, add_post, yourProfileIV;
    private Button editProfileBtn;
    private TextView name, username, postsTV, followersTV, followingTV, bioTV;
    FirebaseUser firebaseUser;
    String profileId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        yourProfileIV = view.findViewById(R.id.yourProfileIV);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        postsTV = view.findViewById(R.id.postsTV);
        followersTV = view.findViewById(R.id.followersTV);
        followingTV = view.findViewById(R.id.followingTV);
        bioTV = view.findViewById(R.id.bioTV);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");
        if (data.equals("none")) {
            profileId = firebaseUser.getUid();
        } else {
            profileId = data;
            getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().clear().apply();
        }

        loadProfile();
        getFollowersAndFollowingCount();
        getPostsCount();

        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        if (profileId.equals(firebaseUser.getUid())){
            editProfileBtn.setText("Edit Profile");
        } else {
            checkFollowingStatus();
        }

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btnText = editProfileBtn.getText().toString();
                if (btnText.equals("Edit Profile")) {
                    Intent intent = new Intent(getContext(), EditProfileActivity.class);
                    startActivity(intent);
                } else {
                    if (btnText.equals("Follow")){
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("Following").child(profileId).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("Followers").child(firebaseUser.getUid()).setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("Following").child(profileId).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("Followers").child(firebaseUser.getUid()).removeValue();
                    }
                }
            }
        });

        bottomSheet = view.findViewById(R.id.bottomSheet);
        bottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSettingsDialogFragment openSettingsDialogFragment = OpenSettingsDialogFragment.newInstance();
                openSettingsDialogFragment.show(getFragmentManager(), "open_settings_dialog_fragment");
            }
        });

        add_post = view.findViewById(R.id.add_post);
        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddPostDialogFragment openAddPostDialogFragment = OpenAddPostDialogFragment.newInstance();
                openAddPostDialogFragment.show(getFragmentManager(), "open_add_post_dialog_fragment");
            }
        });

        followersTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", "Followers");
                startActivity(intent);
            }
        });

        followingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", profileId);
                intent.putExtra("title", "Following");
                startActivity(intent);
            }
        });

//        highlightsStory_RV = view.findViewById(R.id.highlightsStory_RV);
//        StoryData[] storyData = new StoryData[]{
//                new StoryData("shah_0211", R.drawable.image1),
//                new StoryData("_hetvishah__", R.drawable.image2),
//                new StoryData("pandey_25", R.drawable.image3),
//                new StoryData("pandey_25", R.drawable.image3),
//                new StoryData("lkoiokn", R.drawable.image3),
//                new StoryData("liuuiyyf", R.drawable.image3),
//                new StoryData("hgkgkji", R.drawable.image3),
//                new StoryData("khkjkjlk", R.drawable.image3),
//                new StoryData("xyz", R.drawable.image4),
//                new StoryData("abc", R.drawable.image1)
//        };
//
//        storyAdapter = new StoryAdapter(getContext(), Arrays.asList(storyData));
//        highlightsStory_RV.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
//        highlightsStory_RV.setLayoutManager(layoutManager);
//        highlightsStory_RV.setAdapter(storyAdapter);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final PagerAdapter adapter = new PagerAdapter(getContext(), getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                getContext().getSharedPreferences("prefes", Context.MODE_PRIVATE).edit().putString("profileid", profileId).apply();
                Log.d("TAB SELECTED", profileId);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void checkFollowingStatus() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(profileId).exists()){
                    editProfileBtn.setText("Following");
                } else {
                    editProfileBtn.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getPostsCount() {
       FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               int counter = 0;
               for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                   PostData postData = dataSnapshot.getValue(PostData.class);
                   if (postData.getPublisher().equals(profileId)) counter++;
               }
               postsTV.setText(String.valueOf(counter));
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    private void getFollowersAndFollowingCount() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId);
        ref.child("Followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followersTV.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("Following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingTV.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadProfile() {
        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData userData = snapshot.getValue(UserData.class);
                if (userData.getImageUrl().equals("default")){
                    yourProfileIV.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getContext()).load(userData.getImageUrl()).into(yourProfileIV);
                }

                bioTV.setText(userData.getBio());
                name.setText(userData.getName());
                username.setText(userData.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
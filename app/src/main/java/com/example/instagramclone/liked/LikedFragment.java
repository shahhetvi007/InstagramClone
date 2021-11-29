package com.example.instagramclone.liked;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LikedFragment extends Fragment {

    private LikedCommentedAdapter likedCommentedAdapter;
    private List<LikedCommentedData> commentedDataList;
    private RequestAdapter requestAdapter;
    private List<RequestData> requestDataList;
    private RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_liked, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        commentedDataList = new ArrayList<>();
        likedCommentedAdapter = new LikedCommentedAdapter(getContext(), commentedDataList);
        recyclerView.setAdapter(likedCommentedAdapter);

        readNotifications();

        return view;
    }

    private void readNotifications() {
        FirebaseDatabase.getInstance().getReference().child("Notifications")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    commentedDataList.add(dataSnapshot.getValue(LikedCommentedData.class));
                }

                Collections.reverse(commentedDataList);
                likedCommentedAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
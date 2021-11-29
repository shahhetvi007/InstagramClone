package com.example.instagramclone.home.requests;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.R;

public class RequestsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Intent intent = new Intent(getContext(), MessageRequestActivity.class);
        startActivity(intent);
        return inflater.inflate(R.layout.fragment_requests, container, false);
    }
}
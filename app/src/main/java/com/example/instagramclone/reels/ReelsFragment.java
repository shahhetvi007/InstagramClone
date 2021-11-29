package com.example.instagramclone.reels;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReelsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReelsFragment extends Fragment {

    public ReelsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reels, container, false);
    }
}
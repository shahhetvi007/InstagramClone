package com.example.instagramclone.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instagramclone.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OpenAddPostDialogFragment extends BottomSheetDialogFragment {

    public static OpenAddPostDialogFragment newInstance() {
        return new OpenAddPostDialogFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_post_bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickListenerItem(view);
    }

    private void onClickListenerItem(View view) {
        view.findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Post", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.reel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "reel", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.story).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "story", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.storyHighlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "storyHighlight", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.igtv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "igtv", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.guide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "guide", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

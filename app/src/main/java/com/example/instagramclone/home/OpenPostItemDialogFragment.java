package com.example.instagramclone.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instagramclone.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class OpenPostItemDialogFragment extends BottomSheetDialogFragment {

    public static OpenPostItemDialogFragment newInstance(){
        return new OpenPostItemDialogFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.post_menu_bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickListenerItem(view);
    }

    private void onClickListenerItem(View view) {
        view.findViewById(R.id.menu_share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "menu_share", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.menu_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "menu_link", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.menu_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "menu_report", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.hide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "hide", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.unfollow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "unfollow", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

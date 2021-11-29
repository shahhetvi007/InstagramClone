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

public class OpenSettingsDialogFragment extends BottomSheetDialogFragment {
    public static OpenSettingsDialogFragment newInstance() {
        return new OpenSettingsDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onClickListenerItem(view);
    }

    private void onClickListenerItem(View view) {

        view.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.saved).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), SavedActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.archive).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "archive", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.yourActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Your Activity", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.qrCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "QR Code", Toast.LENGTH_SHORT).show();
            }
        });
    }


}

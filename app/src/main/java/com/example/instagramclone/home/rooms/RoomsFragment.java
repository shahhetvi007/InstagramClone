package com.example.instagramclone.home.rooms;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instagramclone.R;

import java.util.ArrayList;
import java.util.List;

public class RoomsFragment extends Fragment {

    private RecyclerView roomRV;
    private List<RoomData> roomDataList;
    private RoomAdapter roomAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rooms, container, false);

        roomRV = view.findViewById(R.id.roomRV);
        roomRV.setHasFixedSize(true);
        roomRV.setLayoutManager(new LinearLayoutManager(getContext()));

        roomDataList = new ArrayList<>();
        roomDataList.add(new RoomData(R.drawable.image1, "Shah Hetvi", "_hetvishah___"));
        roomDataList.add(new RoomData(R.drawable.image1, "Raina", "raina_tripathi"));
        roomDataList.add(new RoomData(R.drawable.image1, "Yash Shah", "known__stranger__"));
        roomAdapter = new RoomAdapter(getContext(), roomDataList);
        roomRV.setAdapter(roomAdapter);

        return view;
    }
}
package com.example.instagramclone.home.chats;

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

public class ChatFragment extends Fragment {

    private RecyclerView chatRV;
    private List<ChatData> chatDataList;
    private ChatAdapter chatAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        chatRV = view.findViewById(R.id.chatRV);
        chatRV.setHasFixedSize(true);
        chatRV.setLayoutManager(new LinearLayoutManager(getContext()));

        chatDataList = new ArrayList<>();
        chatDataList.add(new ChatData(R.drawable.image1, "Yash Shah", "Sent 8h ago"));
        chatDataList.add(new ChatData(R.drawable.image2, "Priyank Shah", "Sent 11h ago"));
        chatDataList.add(new ChatData(R.drawable.image3, "Hetvi Shah", "Sent 12h ago"));
        chatDataList.add(new ChatData(R.drawable.image4, "Jayna Shah", "Sent 24h ago"));
        chatAdapter = new ChatAdapter(getContext(), chatDataList);

        chatRV.setAdapter(chatAdapter);

        return view;
    }
}
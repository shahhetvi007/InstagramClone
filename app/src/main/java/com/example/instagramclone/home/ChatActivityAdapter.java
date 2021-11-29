package com.example.instagramclone.home;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.instagramclone.home.chats.ChatFragment;
import com.example.instagramclone.home.requests.MessageRequestActivity;
import com.example.instagramclone.home.requests.RequestsFragment;
import com.example.instagramclone.home.rooms.RoomsFragment;

public class ChatActivityAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public ChatActivityAdapter(Context context, @NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.context = context;
        totalTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 1:
                RoomsFragment roomsFragment = new RoomsFragment();
                return roomsFragment;
            case 2:
                RequestsFragment requestsFragment = new RequestsFragment();
                return requestsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

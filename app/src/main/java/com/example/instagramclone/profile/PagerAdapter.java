package com.example.instagramclone.profile;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.instagramclone.profile.posts.PostsFragment;
import com.example.instagramclone.profile.reels.ProfileReelsFragment;
import com.example.instagramclone.profile.tagged.TaggedFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private int totalTabs;

    public PagerAdapter(@NonNull Context context, FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.context = context;
        totalTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                PostsFragment postsFragment = new PostsFragment();
                return postsFragment;

            case 1:
                ProfileReelsFragment profileReelsFragment = new ProfileReelsFragment();
                return profileReelsFragment;

            case 2:
                TaggedFragment taggedFragment = new TaggedFragment();
                return taggedFragment;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}

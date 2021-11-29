package com.example.instagramclone.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.R;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {

    private Context context;
    private List<String> mTags;
    private List<String> mTagsCount;

    public TagAdapter(Context context, List<String> mTags, List<String> mTagsCount) {
        this.context = context;
        this.mTags = mTags;
        this.mTagsCount = mTagsCount;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.tag_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tag.setText("#" + mTags.get(position));
        holder.noOfPosts.setText(mTagsCount.get(position) + " posts");
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tag, noOfPosts;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tag = itemView.findViewById(R.id.hash_tag);
            noOfPosts = itemView.findViewById(R.id.no_of_posts);
        }
    }

    public void filter(List<String> filterTags, List<String> filterTagsCount){
        this.mTags = filterTags;
        this.mTagsCount = filterTagsCount;

        notifyDataSetChanged();
    }
}

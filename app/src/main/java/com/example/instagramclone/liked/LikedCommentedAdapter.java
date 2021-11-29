package com.example.instagramclone.liked;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagramclone.PostDetailFragmentFragment;
import com.example.instagramclone.R;
import com.example.instagramclone.home.PostData;
import com.example.instagramclone.profile.ProfileFragment;
import com.example.instagramclone.search.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LikedCommentedAdapter extends RecyclerView.Adapter<LikedCommentedAdapter.MyViewHolder> {

    private Context context;
    private List<LikedCommentedData> data;

    public LikedCommentedAdapter(Context context, List<LikedCommentedData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.liked_commented_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LikedCommentedData commentedData = data.get(position);
        holder.likedCommentedTV.setText(commentedData.getText());
        getUser(holder.yourProfileIV, holder.username, commentedData.getUserId());

        if (commentedData.isPost()){
            holder.likedIV.setVisibility(View.VISIBLE);
            getPostImage(holder.likedIV, commentedData.getPostId());
        } else {
            holder.likedIV.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentedData.isPost()){
                    context.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
                            .edit().putString("postId", commentedData.getPostId()).apply();
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new PostDetailFragmentFragment()).commit();
                } else {
                    context.getSharedPreferences("PROFILE", Context.MODE_PRIVATE)
                            .edit().putString("profileId", commentedData.getUserId()).apply();
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new ProfileFragment()).commit();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView yourProfileIV, likedIV;
        private TextView likedCommentedTV, username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            likedCommentedTV = itemView.findViewById(R.id.likedCommentedTV);
            yourProfileIV = itemView.findViewById(R.id.profileIV);
            likedIV = itemView.findViewById(R.id.likedIV);
            username = itemView.findViewById(R.id.username);
        }
    }

    private void getPostImage(ImageView imageView, String postId){
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                PostData post = snapshot.getValue(PostData.class);
                Glide.with(context).load(post.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUser(ImageView imageView, TextView textView, String userId){
        FirebaseDatabase.getInstance().getReference().child("Users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData user = snapshot.getValue(UserData.class);
                if (user.getImageUrl().equals("default")){
                    imageView.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(context).load(user.getImageUrl()).into(imageView);
                }
                textView.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
